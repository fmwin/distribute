package com.assess.service;

import com.assess.util.ResultMap;

import java.util.Map;

public interface IRegisterService {
    public ResultMap register(Map<String, String> user) throws Exception;
}
