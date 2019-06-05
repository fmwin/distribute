package com.assess.controller;

import com.assess.enums.RoleEnum;
import com.assess.service.IAppService;
import com.assess.service.IBackstageService;
import com.assess.service.IUserService;
import com.assess.util.Base64Util;
import com.assess.util.CodeUtil;
import com.assess.util.PageUtil;
import com.assess.util.ResultMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class BackstageController {

    private Logger logger = Logger.getLogger(BackstageController.class);

    @Resource
    private IBackstageService backstageService;
    @Resource
    private IUserService userService;
    @Resource
    private IAppService appService;
    public static final String UID_HEAD = "uid_";

    /**
     * 生成url
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/generateUrl")
    public ResultMap generateUrl(HttpServletRequest servletRequest) {
        ResultMap resultMap = new ResultMap();
        if (Objects.isNull(servletRequest)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("信息不全");
            return resultMap;
        }
        String sessionKey = servletRequest.getHeader("sessionKey");
        String createUid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        String usedUid = servletRequest.getParameter("usedUid");
        logger.info(String.format("interface:/back/generateUrl params: usedUid=%s", usedUid));
        if (StringUtils.isEmpty(createUid)){
            resultMap.setCode(CodeUtil.PERMISSION_DENIED);
            resultMap.setDesc("请先登录");
            return resultMap;
        }
        if (StringUtils.isEmpty(usedUid)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请选择使用人");
            return resultMap;
        }


        try {
            resultMap = backstageService.generateUrl(Integer.parseInt(createUid), Integer.parseInt(usedUid));
        }catch (Exception e){
            logger.error(String.format("interface:/back/generateUrl error params: usedUid=%s", usedUid), e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
            return resultMap;
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/back/deleteUrl")
    public ResultMap generateUrl(int urlId, HttpServletRequest request){
        ResultMap resultMap = new ResultMap();
        if (Objects.isNull(urlId)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("信息不全");
            return resultMap;
        }

        logger.info(String.format("interface:/back/deleteUrl params: urlId=%s", urlId));

        String sessionKey = request.getHeader("sessionKey");
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");


        try {
            resultMap = backstageService.deleteUrl(Integer.parseInt(uid), urlId);
        }catch (Exception e){
            logger.error(String.format("interface:/back/deleteUrl error params: urlId=%s", urlId), e);
            resultMap.setCode(-1);
            resultMap.setDesc("内部错误");
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 获取员工列表
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/getWorkerList")
    public ResultMap getWorkerList(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();
        if (Objects.isNull(servletRequest)){
            resultMap.setCode(-1);
            resultMap.setDesc("信息不全");
            return resultMap;
        }
        String sessionKey = servletRequest.getAttribute("sessionKey").toString();
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请先登录");
            return resultMap;
        }
        try {
            resultMap = userService.getWorkerList(Integer.parseInt(uid));
        }catch (Exception e){
            logger.error("interface:/back/getWorkerList error", e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 获取员工链接列表
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/getWorkerCodeList")
    public ResultMap getWorkerCodeList(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();

        String sessionKey = servletRequest.getAttribute("sessionKey").toString();
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(CodeUtil.PERMISSION_DENIED);
            resultMap.setDesc("请先登录");
            return resultMap;
        }
        try {
            resultMap = userService.getWorkerCodeList(Integer.parseInt(uid));
        }catch (Exception e){
            logger.error("interface:/back/getWorkerCodeList error", e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 创建员工账号
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/addWorker")
    public ResultMap addWorker(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();

        String sessionKey = servletRequest.getAttribute("sessionKey").toString();
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        String account = servletRequest.getParameter("account");
        String password = servletRequest.getParameter("password");
        String realName = servletRequest.getParameter("realName");

        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(CodeUtil.PERMISSION_DENIED);
            resultMap.setDesc("请先登录");
            return resultMap;
        }

        if (StringUtils.isEmpty(account)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请输入账号");
            return resultMap;
        }

        if (StringUtils.isEmpty(password)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请输入密码");
            return resultMap;
        }

        if (StringUtils.isEmpty(realName)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请输入姓名");
            return resultMap;
        }

        String role = RoleEnum.WORKER.getCode();
        logger.info(String.format("interface:/back/addWorker params: account=%s, password=%s, realName=%s, role=%s", account, password, realName, role));
        try {
            resultMap = userService.addUser(Integer.parseInt(uid), account, password, realName, role);
        }catch (Exception e){
            logger.error("interface:/back/addWorker error: ", e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/back/deleteWorker")
    public ResultMap deleteWorker(Integer uid, HttpServletRequest request){
        ResultMap resultMap = new ResultMap();

        if (null == uid){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请选择要删除的员工");
            return resultMap;
        }
        if (uid==1){
            resultMap.setCode(CodeUtil.PERMISSION_DENIED);
            resultMap.setDesc("权限不足");
            return resultMap;
        }
        logger.info(String.format("interface:/back/deleteWorker params: uid=", uid));

        String sessionKey = request.getHeader("sessionKey");
        String operatorId = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");
        try {
            resultMap = userService.deleteWorker(uid, Integer.parseInt(operatorId));
        }catch (Exception e){
            logger.error("interface:/back/deleteWorker error: ", e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/back/updateWorker")
    public ResultMap deleteWorker(int uid, String account, String password, String realName, HttpServletRequest request){
        ResultMap resultMap = new ResultMap();
        String sessionKey = request.getHeader("sessionKey");
        String operatorId = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        try {
            resultMap = userService.updateWorker(uid, account, password, realName, Integer.parseInt(operatorId));
        }catch (Exception e){
            logger.error("interface:/back/updateWorker error: ", e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/back/getWorker")
    public ResultMap getWorker(int uid, HttpServletRequest request){
        ResultMap resultMap = new ResultMap();
        String sessionKey = request.getHeader("sessionKey");
        String operatorId = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        try {
            resultMap = userService.getWorker(uid, Integer.parseInt(operatorId));
        }catch (Exception e){
            logger.error("interface:/back/getWorker error: ", e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
        }
        return resultMap;
    }

    /**
     * 获取app列表
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/getAppList")
    public ResultMap getAppList(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();
        String sessionKey = servletRequest.getAttribute("sessionKey").toString();
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(CodeUtil.PERMISSION_DENIED);
            resultMap.setDesc("请先登录");
            return resultMap;
        }

        try {
            resultMap = appService.getAppList();
        }catch (Exception e){
            logger.error("interface:/back/getAppList error: ", e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }

    /**
     * 获取app信息
     * @param appId
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/getApp")
    public ResultMap getApp(int appId){
        ResultMap resultMap = new ResultMap();
        try {
            resultMap = appService.getApp(appId);
        }catch (Exception e){
            logger.error(String.format("interface:/back/getApp error params: appId=%d", appId), e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }

    /**
     * 新增app
     * @param appUrl
     * @param logoUrl
     * @param title
     * @param remark
     * @param indexNumber
     * @param property
     * @param level
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/back/addApp", method = RequestMethod.POST)
    public ResultMap addApp(String appUrl, String logoUrl, String title, @RequestParam(value = "remark", required = false) String remark,@RequestParam(value = "indexNumber", required = false) Integer indexNumber, @RequestParam(value = "property", required = false) String property, Integer level, HttpServletRequest request){
        ResultMap resultMap = new ResultMap();
        String sessionKey = request.getHeader("sessionKey");
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");


        Integer index = 100;
        if (!StringUtils.isEmpty(indexNumber)){
            index = indexNumber;
        }

        if (StringUtils.isEmpty(appUrl)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请正确填写app链接");
            return resultMap;
        }
        if (StringUtils.isEmpty(logoUrl)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请正确填写logo图片链接");
            return resultMap;
        }
        if (StringUtils.isEmpty(title)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请正确填写app标题");
            return resultMap;
        }
        if (StringUtils.isEmpty(property)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请正确填写app特点");
            return resultMap;
        }
        if (null == level || level > 5){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请正确填写app星级");
            return resultMap;
        }

        try {
            resultMap = backstageService.addApp(uid, appUrl, logoUrl, title, remark, index, property, level);
        }catch (Exception e){
            logger.error("interface:/back/addApp error", e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }

    /**
     * 删除app
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/deleteApp")
    public ResultMap deleteApp(Integer id){
        ResultMap resultMap = new ResultMap();
        try {
            if (null == id){
                resultMap.setCode(CodeUtil.EMPTY);
                resultMap.setDesc("请选择要删除的app");
                return resultMap;
            }
            appService.deleteApp(id);
        }catch (Exception e){
            logger.error(String.format("interface:/back/deleteApp error params: appId=%d", id), e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        resultMap.setCode(CodeUtil.SUCCESS);
        resultMap.setDesc("删除app成功");
        return resultMap;
    }

    /**
     * 更新app
     * @param appUrl
     * @param logoUrl
     * @param title
     * @param remark
     * @param indexNumber
     * @param property
     * @param level
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/updateApp")
    public ResultMap updateApp(Integer id, String appUrl, String logoUrl, String title,
                               @RequestParam(value = "remark", required = false) String remark,
                               @RequestParam(value = "indexNumber", required = false) Integer indexNumber,
                               @RequestParam(value = "property", required = false) String property, Integer level,
                               HttpServletRequest request){
        ResultMap resultMap = new ResultMap();
        String sessionKey = request.getHeader("sessionKey");
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        Integer index = 100;
        if (null != indexNumber){
            index = indexNumber;
        }
        if (null == id){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请选择要更新的app");
            return resultMap;
        }

        if (StringUtils.isEmpty(appUrl)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请正确填写app链接");
            return resultMap;
        }
        if (StringUtils.isEmpty(logoUrl)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请正确填写logo图片链接");
            return resultMap;
        }
        if (StringUtils.isEmpty(title)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请正确填写app标题");
            return resultMap;
        }
        if (StringUtils.isEmpty(property)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请正确填写app特点");
            return resultMap;
        }
        if (null == level || level > 5){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("请正确填写app星级");
            return resultMap;
        }

        try {
            resultMap = backstageService.updateApp(id, uid, appUrl, logoUrl, title, remark, index, property, level);
        }catch (Exception e){
            logger.error("interface:/back/updateApp error: ", e);
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }

    /**
     * 登录后台
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/login")
    public ResultMap login(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();

        String account = servletRequest.getParameter("account");
        String password = servletRequest.getParameter("password");
        if (Objects.isNull(servletRequest) || StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写账号密码");
            return resultMap;
        }

        try {
            resultMap = backstageService.login(account, password);
        }catch (Exception e){
            logger.error("interface:/back/login error:", e);
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 获取推广访问量
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/views")
    public ResultMap views(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();

        String sessionKey = servletRequest.getAttribute("sessionKey").toString();
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        //String uid = servletRequest.getParameter("uid");
        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请先登录");
            return resultMap;
        }

        try {
            resultMap = backstageService.getViews(uid);
        }catch (Exception e){
            logger.error("interface:/back/views error:", e);
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 管理员获取推广列表
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/viewsList")
    public ResultMap viewsList(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();
        String sessionKey = servletRequest.getAttribute("sessionKey").toString();
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(CodeUtil.PERMISSION_DENIED);
            resultMap.setDesc("请先登录");
            return resultMap;
        }
        try {
            resultMap = backstageService.getViewsList(uid);
        }catch (Exception e){
            logger.error("interface:/back/viewsList error:", e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
            return resultMap;
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/back/actionList")
    public ResultMap actionList(Integer pageNumber, Integer pageSize, HttpServletRequest request){
        ResultMap resultMap = new ResultMap();

        if (null == pageNumber || pageNumber <= 0){
            pageNumber = PageUtil.pageNumber;
        }
        if (null == pageSize || pageSize <= 0){
            pageSize = PageUtil.pageSize;
        }

        String sessionKey = request.getHeader("sessionKey");
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");
        try{
            resultMap = backstageService.actionList(Integer.parseInt(uid), pageNumber, pageSize);
        }catch (Exception e){
            logger.error("interface:/back/actionList error: ", e);
        }

        return resultMap;
    }

}
