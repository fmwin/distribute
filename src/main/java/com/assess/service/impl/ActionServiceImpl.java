package com.assess.service.impl;

import com.assess.dao.SActionMapper;
import com.assess.dao.SAppMapper;
import com.assess.model.SAction;
import com.assess.model.SApp;
import com.assess.service.IActionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class ActionServiceImpl implements IActionService {

    @Resource
    private SActionMapper sActionMapper;
    @Resource
    private SAppMapper sAppMapper;

    /**
     * 新增用户点击动作记录
     * @param uid
     * @param appId
     * @throws Exception
     */
    @Override
    public void addAction(Integer uid, Integer appId, String ip) throws Exception {

        SApp sApp = sAppMapper.selectByPrimaryKey(appId);

        if (Objects.nonNull(sApp)) {
            SAction sAction = new SAction();
            sAction.setUid(uid);
            sAction.setAppId(appId);
            if (ip.length()>15){
                ip = ip.substring(0,15);
            }
            sAction.setIp(ip);

            sActionMapper.insert(sAction);
        }
    }
}
