package com.assess.service;

import com.assess.model.SUser;
import com.assess.util.ResultMap;

public interface IUserService {
    public SUser getUserByUid(Integer uid) throws Exception;

    public ResultMap getWorkerList(Integer uid) throws Exception;

    public ResultMap getWorkerCodeList(Integer uid) throws Exception;

    public ResultMap addUser(Integer uid, String account, String password, String realName, String role) throws Exception;
}
