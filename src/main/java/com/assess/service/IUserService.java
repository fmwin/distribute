package com.assess.service;

import com.assess.model.SUser;
import com.assess.util.ResultMap;

public interface IUserService {
    public SUser getUserByUid(Integer uid) throws Exception;

    public ResultMap getWorkerList(Integer uid) throws Exception;

    public ResultMap getWorkerCodeList(Integer uid) throws Exception;

    public ResultMap addUser(Integer uid, String account, String password, String realName, String role) throws Exception;

    public ResultMap deleteWorker(Integer uid, Integer operatorId) throws Exception;

    public ResultMap updateWorker(Integer id, String account, String password, String realName, Integer operator) throws Exception;

    public ResultMap getWorker(Integer id, Integer operator) throws Exception;
}
