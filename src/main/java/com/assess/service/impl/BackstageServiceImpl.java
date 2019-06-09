package com.assess.service.impl;

import com.assess.dao.*;
import com.assess.enums.RoleEnum;
import com.assess.model.*;
import com.assess.response.ActionResponse;
import com.assess.response.ViewsResponse;
import com.assess.service.IBackstageService;
import com.assess.util.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
    private SActionMapper sActionMapper;
    @Resource
    private RedisUtil redisUtil;

    public static final String URL_HEAD = "http://chuangya.tianjiurc.com/hello/index.html?disCode=";
    public static final String UID_HEAD = "uid_";

    private static final String APP_CACHE = "app_cache";

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
        String disCode = account.substring(account.length()-6);
        String url = String.format("%s%s", URL_HEAD, disCode);

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
    public ResultMap deleteUrl(int uid, int urlId) throws Exception {
        ResultMap resultMap = new ResultMap();
        if (!isAdmin(uid)){
            resultMap.setCode(CodeUtil.PERMISSION_DENIED);
            resultMap.setDesc("权限不足");
            return resultMap;
        }
        sUrlMapper.deleteByPrimaryKey(urlId);

        resultMap.setCode(CodeUtil.SUCCESS);
        resultMap.setDesc("删除成功");
        return resultMap;
    }

    @Override
    public ResultMap addApp(String uid, String appUrl, String logoUrl, String title, String remark, Integer index_number, String property, Integer level) throws Exception {
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
        sApp.setLevel(level);
        sApp.setCreateUid(Integer.parseInt(uid));

        if (!StringUtils.isEmpty(property)) {
            sApp.setProperty(property);
        }

        if (!StringUtils.isEmpty(remark)) {
            sApp.setRemark(remark);
        }
        if (null != index_number) {
            sApp.setIndexNumber(index_number);
        }

        sAppMapper.insert(sApp);

        redisUtil.del(0, APP_CACHE);

        resultMap.setCode(1);
        resultMap.setDesc("创建app成功");
        resultMap.setData(sApp.getId());
        return resultMap;
    }

    @Override
    public ResultMap updateApp(Integer id, String uid, String appUrl, String logoUrl, String title, String remark, Integer index_number, String property, Integer level) throws Exception {
        ResultMap resultMap = new ResultMap();
        if (!isAdmin(Integer.parseInt(uid))){
            resultMap.setCode(CodeUtil.PERMISSION_DENIED);
            resultMap.setDesc("只有管理员可添加app");
            return resultMap;
        }

        SApp sApp = sAppMapper.selectByPrimaryKey(id);
        if (Objects.isNull(sApp)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("该app已被删除");
            return resultMap;
        }

        sApp.setAppUrl(appUrl);
        sApp.setLogoUrl(logoUrl);
        sApp.setTitle(title);
        sApp.setLevel(level);
        sApp.setCreateUid(Integer.parseInt(uid));
        sApp.setProperty(property);
        sApp.setRemark(remark);
        sApp.setIndexNumber(index_number);


        sAppMapper.updateByPrimaryKey(sApp);

        resultMap.setCode(CodeUtil.SUCCESS);
        resultMap.setDesc("更新app成功");
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
        sCodeViewsExample.createCriteria().andCodeEqualTo(code).andDateEqualTo(DateUtil.today(DateUtil.YYYY_MM_DD));
        List<SCodeViews> codeViewsList = sCodeViewsMapper.selectByExample(sCodeViewsExample);

        SCodeViewsExample sCodeViewsExampleMonth = new SCodeViewsExample();
        sCodeViewsExampleMonth.createCriteria().andCodeEqualTo(code).andDateBetween(DateUtil.format(DateUtil.getMonthAgo(1), DateUtil.YYYY_MM_DD), DateUtil.format(new Date(), DateUtil.YYYY_MM_DD));
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
            sCodeViewsExample.createCriteria().andDateEqualTo(DateUtil.today(DateUtil.YYYY_MM_DD));
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
                    if (CollectionUtils.isEmpty(sUrlList)){
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
                        if (StringUtils.isEmpty(sUser.getRealName())){
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

    @Override
    public ResultMap actionList(Integer uid, Integer pageNumber, Integer pageSize) throws Exception {
        ResultMap resultMap = new ResultMap();
        Map<String, Object> map = new HashMap<>();
        map.put("total", 0);

        Integer start = 0;
        if (pageNumber > 1) {
            start = (pageNumber - 1) * pageSize;
        }

        List<ActionResponse> actionResponseList = new ArrayList<>();
        if (isAdmin(uid) || isWorker(uid)){
            SActionExample sActionExample = new SActionExample();
            long count = sActionMapper.countByExample(sActionExample);
            if (start > count){
                resultMap.setCode(CodeUtil.PAGE_ERROR);
                resultMap.setDesc("请选择正确的页码");
                return resultMap;
            }

            map.put("total", count);
            sActionExample.setOrderByClause(" create_date desc ");
            RowBounds rowBounds = new RowBounds(start, pageSize);

            List<SAction> actionList = sActionMapper.selectByExampleWithRowbounds(sActionExample, rowBounds);
            if (!CollectionUtils.isEmpty(actionList)){
                for (SAction sAction:actionList){
                    ActionResponse actionResponse = new ActionResponse();
                    SUser register = sUserMapper.selectByPrimaryKey(sAction.getUid());
                    actionResponse.setIp(sAction.getIp());
                    actionResponse.setCreateDate(DateUtil.format(sAction.getCreateDate(), DateUtil.mFormatIso8601Daytime));

                    if (Objects.nonNull(register)) {
                        actionResponse.setAccount(register.getAccount());
                        actionResponse.setRealName(register.getRealName());

                        if (Objects.nonNull(register.getDisUid())) {
                            SUser disUser = sUserMapper.selectByPrimaryKey(register.getDisUid());
                            if (Objects.nonNull(disUser)){
                                actionResponse.setDisName(disUser.getRealName());
                            }else{
                                actionResponse.setDisName("admin");
                            }
                        }else {
                            actionResponse.setDisName("admin");
                        }

                        SApp sApp = sAppMapper.selectByPrimaryKey(sAction.getAppId());
                        if (Objects.nonNull(sApp)){
                            actionResponse.setAppTitle(sApp.getTitle());
                        }else{
                            actionResponse.setAppTitle("未知app");
                        }
                    }
                    actionResponseList.add(actionResponse);
                }
            }
        }

        map.put("list", actionResponseList);
        resultMap.setCode(CodeUtil.SUCCESS);
        resultMap.setDesc("获取用户行为列表成功");
        resultMap.setData(map);
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
