package com.assess.service.impl;

import com.assess.dao.SAppMapper;
import com.assess.dao.SCodeViewsMapper;
import com.assess.dao.SUrlMapper;
import com.assess.dao.SUserMapper;
import com.assess.enums.RoleEnum;
import com.assess.model.*;
import com.assess.response.ViewsResponse;
import com.assess.service.IBackstageService;
import com.assess.util.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BackstageServiceImpl implements IBackstageService {

    @Resource
    private SUserMapper sUserMapper;
    @Resource
    private SUrlMapper sUrlMapper;
    @Resource
    private SAppMapper sAppMapper;
    @Resource
    private SCodeViewsMapper sCodeViewsMapper;
    @Resource
    private RedisUtil redisUtil;

    public static final String URL_HEAD = "http://www.distribute.com/hello/index.html?myCode=";
    public static final String UID_HEAD = "uid_";

    @Override
    public ResultMap generateUrl(int createUid, int usedUid) throws Exception {

        ResultMap resultMap = new ResultMap();

        if (!isAdmin(createUid)){
            resultMap.setCode(-1);
            resultMap.setDesc("只有管理员可生成链接");
            return resultMap;
        }

        SUser sUser = sUserMapper.selectByPrimaryKey(usedUid);
        if (Objects.isNull(sUser)){
            resultMap.setCode(-1);
            resultMap.setDesc("用户不存在");
            return resultMap;
        }

        if (!isWorker(usedUid)){
            resultMap.setCode(-1);
            resultMap.setDesc("只有员工可拥有链接");
            return resultMap;
        }

        String account = sUser.getAccount();
        String url = String.format("%s%s", URL_HEAD, account);

        SUrlExample sUrlExample = new SUrlExample();
        sUrlExample.createCriteria().andUrlEqualTo(url);
        List<SUrl> sUrls = sUrlMapper.selectByExample(sUrlExample);
        if (null != sUrls && !sUrls.isEmpty()){
            resultMap.setCode(-1);
            resultMap.setDesc("该用户已拥有邀请码");
            return resultMap;
        }

        SUrl sUrl = new SUrl();
        sUrl.setCreateUid(createUid);
        sUrl.setUsedUid(usedUid);
        sUrl.setUrl(url);
        sUrl.setCode(account);
        sUrlMapper.insert(sUrl);

        resultMap.setCode(1);
        resultMap.setDesc("生成链接成功");
        resultMap.setData(url);

        return resultMap;
    }

    @Override
    public ResultMap addApp(String uid, String appUrl, String logoUrl, String title, String remark, Integer index_number) throws Exception {
        ResultMap resultMap = new ResultMap();
        if (!isAdmin(Integer.parseInt(uid))){
            resultMap.setCode(-1);
            resultMap.setDesc("只有管理员可添加app");
            return resultMap;
        }

        SApp sApp = new SApp();
        sApp.setAppUrl(appUrl);
        sApp.setLogoUrl(logoUrl);
        sApp.setTitle(title);

        if (!StringUtils.isEmpty(remark)) {
            sApp.setRemark(remark);
        }
        if (null != index_number) {
            sApp.setIndexNumber(index_number);
        }

        sAppMapper.insert(sApp);

        resultMap.setCode(1);
        resultMap.setDesc("创建app成功");
        resultMap.setData(sApp.getId());
        return resultMap;
    }

    /*@Override
    public ResultMap addApp(Map<String, Object> map) throws Exception {
        ResultMap resultMap = new ResultMap();
        if (!isAdmin(Integer.parseInt(map.get("uid").toString()))){
            resultMap.setCode(-1);
            resultMap.setDesc("只有管理员可添加app");
            return resultMap;
        }

        SApp sApp = new SApp();
        sApp.setAppUrl(map.get("appUrl").toString());
        sApp.setLogoUrl(map.get("logoUrl").toString());
        if (!MapUtils.isEmpty(map, "title")) {
            sApp.setTitle(map.get("title").toString());
        }
        if (!MapUtils.isEmpty(map, "remark")) {
            sApp.setRemark(map.get("remark").toString());
        }
        if (!MapUtils.isEmpty(map, "index")) {
            sApp.setIndexNumber(Integer.parseInt(map.get("remark").toString()));
        }

        sAppMapper.insert(sApp);

        resultMap.setCode(1);
        resultMap.setDesc("创建app成功");
        resultMap.setData(sApp.getId());
        return resultMap;
    }*/

    @Override
    public ResultMap login(String account, String password) throws Exception {
        ResultMap resultMap = new ResultMap();
        SUserExample sUserExample = new SUserExample();
        sUserExample.createCriteria().andAccountEqualTo(account).andPasswordEqualTo(password);

        List<SUser> sUserList = sUserMapper.selectByExample(sUserExample);
        if (Objects.isNull(sUserList) || sUserList.isEmpty()){
            resultMap.setCode(-1);
            resultMap.setDesc("账号或密码不正确");
            return resultMap;
        }

        SUser user = sUserList.get(0);
        if (!user.getRole().contains(RoleEnum.WORKER.getCode()) && !user.getRole().contains(RoleEnum.SUPER_ADMIN.getCode())){
            resultMap.setCode(-1);
            resultMap.setDesc("您不是工作人员");
            return resultMap;
        }

        long time = 86400;
        String origin = UID_HEAD+user.getId();
        String sessionKey = Base64Util.getBase64String(origin);
        String sessionValue = RandomUtil.getTimeAndCountRandom(6);
        System.out.println(sessionKey+"------"+sessionValue);
        redisUtil.set(sessionKey, sessionValue, 1);
        redisUtil.expire(sessionKey, time);
        user.setPassword(null);

        Map response = new HashMap();
        response.put("user", user);
        response.put("sessionKey", sessionKey);
        response.put("sessionValue", sessionValue);
        resultMap.setCode(1);
        resultMap.setDesc("登录成功");
        resultMap.setData(response);
        return resultMap;
    }

    @Override
    public ResultMap getViews(String uid) throws Exception {
        ResultMap resultMap = new ResultMap();

        Integer uidInt = Integer.parseInt(uid);
        SUser user = sUserMapper.selectByPrimaryKey(uidInt);

        SUrlExample urlExample = new SUrlExample();
        urlExample.createCriteria().andUsedUidEqualTo(uidInt);
        List<SUrl> urlList = sUrlMapper.selectByExample(urlExample);

        if (Objects.isNull(user) || !user.getRole().contains(RoleEnum.WORKER.getCode())){
            resultMap.setCode(-1);
            resultMap.setDesc("用户不存在");
            return resultMap;
        }

        if ((Objects.isNull(urlList) || urlList.isEmpty())){
            resultMap.setCode(-1);
            resultMap.setDesc("请先申请您的专属链接");
            return resultMap;
        }
        String code = urlList.get(0).getCode();

        SCodeViewsExample sCodeViewsExample = new SCodeViewsExample();
        sCodeViewsExample.createCriteria().andCodeEqualTo(code).andDateEqualTo(new Date());
        List<SCodeViews> codeViewsList = sCodeViewsMapper.selectByExample(sCodeViewsExample);

        SCodeViewsExample sCodeViewsExampleMonth = new SCodeViewsExample();
        sCodeViewsExampleMonth.createCriteria().andCodeEqualTo(code).andDateBetween(DateUtil.getMonthAgo(1), new Date());
        List<SCodeViews> codeViewsListMonth = sCodeViewsMapper.selectByExample(sCodeViewsExampleMonth);

        Map<String, Object> result = new HashMap<>();
        if (Objects.isNull(codeViewsList) || codeViewsList.isEmpty()){
            result.put("todayViews", 0);
        }else{
            result.put("todayViews", codeViewsList.get(0).getViews());
        }

        if (Objects.isNull(codeViewsListMonth) || codeViewsListMonth.isEmpty()){
            result.put("monthViews", 0);
        }else{
            int monthViews = 0;
            for (SCodeViews views: codeViewsListMonth){
                monthViews = monthViews + views.getViews();
            }
            result.put("monthViews", monthViews);
        }

        resultMap.setCode(1);
        resultMap.setDesc("获取推广数据成功");
        resultMap.setData(result);
        return resultMap;
    }

    @Override
    public ResultMap getViewsList(String uid) throws Exception {
        ResultMap resultMap = new ResultMap();
        Integer uidInt = Integer.parseInt(uid);
        if (isAdmin(uidInt)){
            SCodeViewsExample sCodeViewsExample = new SCodeViewsExample();
            sCodeViewsExample.createCriteria().andDateEqualTo(new Date());
            sCodeViewsExample.setOrderByClause("views desc");
            List<SCodeViews> sCodeViewsList = sCodeViewsMapper.selectByExample(sCodeViewsExample);

            if (Objects.isNull(sCodeViewsList) || sCodeViewsList.isEmpty()){
                resultMap.setCode(1);
                resultMap.setDesc("获取今日访问量统计成功");
                return resultMap;
            }

            List<ViewsResponse> viewsResponseList = new ArrayList<>();
            for (SCodeViews sCodeViews:sCodeViewsList){
                ViewsResponse view = new ViewsResponse();

                view.setViews(sCodeViews.getViews());
                view.setDate(sCodeViews.getDate());
                if (sCodeViews.getCode().equals("admin")){
                    view.setUid(1);
                    view.setCode("admin");
                    view.setRealName("admin");
                    view.setAccount("admin");
                }else {

                    SUrlExample sUrlExample = new SUrlExample();
                    sUrlExample.createCriteria().andCodeEqualTo(sCodeViews.getCode());
                    List<SUrl> sUrlList = sUrlMapper.selectByExample(sUrlExample);
                    if (Objects.isNull(sUrlList) || sUrlList.isEmpty()){
                        view.setUid(1);
                        view.setCode("admin");
                        view.setRealName("admin");
                        view.setAccount("admin");
                    }else{
                        SUrl sUrl = sUrlList.get(0);
                        view.setUid(sUrl.getUsedUid());
                        view.setCode(sUrl.getCode());
                        SUser sUser = sUserMapper.selectByPrimaryKey(sUrl.getUsedUid());
                        view.setAccount(sUser.getAccount());
                        if (null == sUser.getRealName()){
                            view.setRealName(sUser.getAccount());
                        }else {
                            view.setRealName(sUser.getRealName());
                        }
                    }
                }

                viewsResponseList.add(view);
            }
            resultMap.setData(viewsResponseList);
        }

        resultMap.setCode(1);
        resultMap.setDesc("获取今日推广数据成功");
        return resultMap;
    }

    private boolean isAdmin(Integer uid){
        SUser createUser = sUserMapper.selectByPrimaryKey(uid);
        if (Objects.isNull(createUser) || !createUser.getRole().contains(RoleEnum.SUPER_ADMIN.getCode())){
            return false;
        }
        return true;
    }

    private boolean isWorker(Integer uid){
        SUser createUser = sUserMapper.selectByPrimaryKey(uid);
        if (Objects.isNull(createUser) || !createUser.getRole().contains(RoleEnum.WORKER.getCode())){
            return false;
        }
        return true;
    }
}
