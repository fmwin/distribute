package com.assess.service;

import com.assess.util.ResultMap;

public interface IBackstageService {
    public ResultMap generateUrl(int createUid, int usedUid) throws Exception;

    public ResultMap deleteUrl(int uid, int urlId) throws Exception ;

    public ResultMap addApp(String uid, String appUrl, String logoUrl, String title, String remark, Integer index_number, String property, Integer level) throws Exception;

    public ResultMap updateApp(Integer id, String uid, String appUrl, String logoUrl, String title, String remark, Integer index_number, String property, Integer level) throws Exception;

    public ResultMap login(String account, String password) throws Exception;

    public ResultMap getViews(String uid) throws Exception;

    public ResultMap getViewsList(String uid) throws Exception;
}
