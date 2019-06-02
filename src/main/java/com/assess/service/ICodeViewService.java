package com.assess.service;

import com.assess.util.ResultMap;

public interface ICodeViewService {

    public void createOrModifyCodeView(String code) throws Exception;

    public ResultMap login(String account, String password) throws Exception;

    public void logout(String sessionKey) throws Exception;
}
