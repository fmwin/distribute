package com.assess.service.impl;

import com.assess.dao.SAppMapper;
import com.assess.model.SApp;
import com.assess.model.SAppExample;
import com.assess.service.IAppService;
import com.assess.util.ResultMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppServiceImpl implements IAppService {

    @Resource
    private SAppMapper sAppMapper;

    @Override
    public ResultMap getAppList() throws Exception {
        ResultMap resultMap = new ResultMap();

        SAppExample sAppExample = new SAppExample();
        sAppExample.setOrderByClause("index_number desc");
        List<SApp> sAppList = sAppMapper.selectByExample(sAppExample);

        resultMap.setCode(1);
        resultMap.setDesc("获取app列表成功");
        resultMap.setData(sAppList);
        return resultMap;
    }
}
