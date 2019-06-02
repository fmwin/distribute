package com.assess.service;

import com.assess.util.ResultMap;

public interface IAppService {

    public ResultMap getAppList() throws Exception;

    public ResultMap getApp(Integer appId) throws Exception;

    public void deleteApp(Integer appId) throws Exception;
}
