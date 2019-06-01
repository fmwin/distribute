package com.assess.service;

import com.assess.util.ResultMap;


public interface IRegisterService {
    public ResultMap register(String account, String password, String confirm, String realName, String disCode) throws Exception;
}
