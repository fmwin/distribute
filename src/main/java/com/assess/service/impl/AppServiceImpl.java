package com.assess.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.assess.dao.SAppMapper;
import com.assess.model.SApp;
import com.assess.model.SAppExample;
import com.assess.service.IAppService;
import com.assess.util.CodeUtil;
import com.assess.util.RedisUtil;
import com.assess.util.ResultMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AppServiceImpl implements IAppService {

    private Logger logger = org.apache.log4j.Logger.getLogger(AppServiceImpl.class);

    @Resource
    private SAppMapper sAppMapper;
    @Resource
    private RedisUtil redisUtil;

    private static final String APP_CACHE = "app_cache";

    @Override
    public ResultMap getAppList() throws Exception {
        ResultMap resultMap = new ResultMap();
        List<SApp> sAppList = new ArrayList<>();

            SAppExample sAppExample = new SAppExample();
            sAppExample.setOrderByClause("index_number desc");
            sAppList = sAppMapper.selectByExample(sAppExample);
            //String cacheArray = JSONArray.toJSONString(sAppList);
            //redisUtil.set(APP_CACHE, cacheArray, 0);

        resultMap.setCode(1);
        resultMap.setDesc("获取app列表成功");
        resultMap.setData(sAppList);
        return resultMap;
    }

    @Override
    public ResultMap getApp(Integer appId) throws Exception {
        ResultMap resultMap = new ResultMap();
        SApp sApp = sAppMapper.selectByPrimaryKey(appId);
        if (Objects.isNull(sApp)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("app不存在");
            return resultMap;
        }
        resultMap.setCode(CodeUtil.SUCCESS);
        resultMap.setDesc("获取app信息成功");
        resultMap.setData(sApp);
        return resultMap;
    }

    @Override
    public void deleteApp(Integer appId) throws Exception {
        sAppMapper.deleteByPrimaryKey(appId);
        redisUtil.del(0, APP_CACHE);
    }
}
