package com.assess.controller;

import com.assess.enums.RoleEnum;
import com.assess.model.SUser;
import com.assess.service.IAppService;
import com.assess.service.IBackstageService;
import com.assess.service.IUserService;
import com.assess.util.MapUtils;
import com.assess.util.ResultMap;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Objects;

@Controller
public class BackstageController {

    @Resource
    private IBackstageService backstageService;
    @Resource
    private IUserService userService;
    @Resource
    private IAppService appService;

    /**
     * 生成url
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/generateUrl")
    public ResultMap generateUrl(ServletRequest servletRequest) {
        ResultMap resultMap = new ResultMap();
        if (Objects.isNull(servletRequest)){
            resultMap.setCode(-1);
            resultMap.setDesc("信息不全");
            return resultMap;
        }
        String createUid = servletRequest.getParameter("createUid");
        String usedUid = servletRequest.getParameter("usedUid");
        if (StringUtils.isEmpty(createUid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请先登录");
            return resultMap;
        }
        if (StringUtils.isEmpty(usedUid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请选择使用人");
            return resultMap;
        }


        try {
            resultMap = backstageService.generateUrl(Integer.parseInt(createUid), Integer.parseInt(usedUid));
        }catch (Exception e){
            e.printStackTrace();
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
        String uid = servletRequest.getParameter("uid");
        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请先登录");
            return resultMap;
        }
        try {
            resultMap = userService.getWorkerList(Integer.parseInt(uid));
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/back/getWorkerCodeList")
    public ResultMap getWorkerCodeList(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();

        String uid = servletRequest.getParameter("uid");
        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请先登录");
            return resultMap;
        }
        try {
            resultMap = userService.getWorkerCodeList(Integer.parseInt(uid));
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(0);
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

        String uid = servletRequest.getParameter("uid");
        String account = servletRequest.getParameter("account");
        String password = servletRequest.getParameter("password");
        String realName = servletRequest.getParameter("realName");

        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请先登录");
            return resultMap;
        }

        if (StringUtils.isEmpty(account)){
            resultMap.setCode(-1);
            resultMap.setDesc("请输入账号");
            return resultMap;
        }

        if (StringUtils.isEmpty(password)){
            resultMap.setCode(-1);
            resultMap.setDesc("请输入密码");
            return resultMap;
        }

        if (StringUtils.isEmpty(realName)){
            resultMap.setCode(-1);
            resultMap.setDesc("请输入姓名");
            return resultMap;
        }

        String role = RoleEnum.WORKER.getCode();
        try {
            resultMap = userService.addUser(Integer.parseInt(uid), account, password, realName, role);
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/back/getAppList")
    public ResultMap getAppList(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();
        String uid = servletRequest.getParameter("uid");
        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请先登录");
            return resultMap;
        }

        try {
            resultMap = appService.getAppList();
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }

    /**
     * 增加app
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/back/addApp")
    public ResultMap addApp(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();
        String uid = servletRequest.getParameter("uid");
        String appUrl = servletRequest.getParameter("appUrl");
        String logoUrl = servletRequest.getParameter("logoUrl");
        String title = servletRequest.getParameter("title");
        String remark = servletRequest.getParameter("remark");
        String index_number = servletRequest.getParameter("indexNumber");
        Integer index = 100;

        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请先登录");
            return resultMap;
        }

        if (StringUtils.isEmpty(appUrl)){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写app链接");
            return resultMap;
        }
        if (StringUtils.isEmpty(logoUrl)){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写logo图片链接");
            return resultMap;
        }
        if (StringUtils.isEmpty(title)){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写app标题");
            return resultMap;
        }

        if (null != index_number){
          index = Integer.parseInt(index_number);
        }
        try {
            resultMap = backstageService.addApp(uid, appUrl, logoUrl, title, remark, index);
        }catch (Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
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
        String uid = servletRequest.getParameter("uid");
        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请先登录");
            return resultMap;
        }

        try {
            resultMap = backstageService.getViews(uid);
        }catch (Exception e){
            e.printStackTrace();
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
        String uid = servletRequest.getParameter("uid");
        if (StringUtils.isEmpty(uid)){
            resultMap.setCode(-1);
            resultMap.setDesc("请先登录");
            return resultMap;
        }
        try {
            resultMap = backstageService.getViewsList(uid);
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }
        return resultMap;
    }


}
