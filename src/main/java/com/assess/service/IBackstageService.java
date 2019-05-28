package com.assess.service;

import com.assess.util.ResultMap;

import java.util.Map;

public interface IBackstageService {
    public ResultMap generateUrl(int createUid, int usedUid) throws Exception;

    public ResultMap addApp(String uid, String appUrl, String logoUrl, String title, String remark, Integer index_number) throws Exception;

    public ResultMap login(String account, String password) throws Exception;

    public ResultMap getViews(String uid) throws Exception;

    public ResultMap getViewsList(String uid) throws Exception;
}
