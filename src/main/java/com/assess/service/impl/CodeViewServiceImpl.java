package com.assess.service.impl;

import com.assess.dao.SCodeViewsMapper;
import com.assess.dao.SUrlMapper;
import com.assess.dao.SUserMapper;
import com.assess.enums.RoleEnum;
import com.assess.model.*;
import com.assess.service.ICodeViewService;
import com.assess.util.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CodeViewServiceImpl implements ICodeViewService {

    @Resource
    private SCodeViewsMapper sCodeViewsMapper;
    @Resource
    private SUrlMapper sUrlMapper;
    @Resource
    private SUserMapper sUserMapper;
    @Resource
    private RedisUtil redisUtil;

    public static final String UID_HEAD = "uid_";

    @Override
    public void createOrModifyCodeView(Integer uid) throws Exception {

        /** 查询是否存在code，没有的话，直接将code置为admin */
        SUser user = sUserMapper.selectByPrimaryKey(uid);
        if (Objects.nonNull(user)){
            Integer disUid = user.getDisUid();
            SUrlExample sUrlExample = new SUrlExample();
            sUrlExample.createCriteria().andUsedUidEqualTo(disUid);

            List<SUrl> sUrlList = sUrlMapper.selectByExample(sUrlExample);
            if (!CollectionUtils.isEmpty(sUrlList)){
                SUrl sUrl = sUrlList.get(0);
                String code = sUrl.getCode();
                SCodeViewsExample sCodeViewsExample = new SCodeViewsExample();
                sCodeViewsExample.createCriteria().andCodeEqualTo(code).andDateEqualTo(DateUtil.today(DateUtil.YYYY_MM_DD));
                List<SCodeViews> sCodeViews = sCodeViewsMapper.selectByExample(sCodeViewsExample);

                if (Objects.isNull(sCodeViews) || sCodeViews.isEmpty()){
                    SCodeViews codeView = new SCodeViews();
                    codeView.setCode(code);
                    codeView.setViews(1);

                    sCodeViewsMapper.insert(codeView);
                }else{
                    SCodeViews codeView = sCodeViews.get(0);
                    codeView.setViews(codeView.getViews()+1);

                    sCodeViewsMapper.updateByPrimaryKey(codeView);
                }
            }
        }



    }

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

        long time = 86400;
        String origin = UID_HEAD+user.getId();
        String sessionKey = Base64Util.getBase64String(origin);
        String sessionValue = RandomUtil.getTimeAndCountRandom(6);
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
    public void logout(String sessionKey) throws Exception {
        redisUtil.del(1, sessionKey);
    }
}
