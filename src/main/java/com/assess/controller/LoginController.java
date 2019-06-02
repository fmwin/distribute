package com.assess.controller;

import com.assess.service.IAppService;
import com.assess.service.ICodeViewService;
import com.assess.util.Base64Util;
import com.assess.util.CodeUtil;
import com.assess.util.ResultMap;
import com.sun.tools.javac.jvm.Code;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Resource
    private IAppService appService;
    @Resource
    private ICodeViewService codeViewService;

    private Logger logger = Logger.getLogger(LoginController.class);
    public static final String UID_HEAD = "uid_";

    /**
     * 用户获取app列表
     *
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/api/index")
    public ResultMap index(HttpServletRequest servletRequest) {
        ResultMap resultMap = new ResultMap();

        try {
            resultMap = appService.getAppList();
        } catch (Exception e) {
            logger.error("interface:/api/index error:", e);
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }

    /**
     * 用户登录
     *
     * @param account
     * @param password
     * @param checkCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public ResultMap login(String account, String password, @RequestParam(value = "checkCode", required = false) String checkCode) {
        ResultMap resultMap = new ResultMap();
        logger.info(String.format("interface:/api/login params: account=%s, password=%s, checkCode=%s", account, password, checkCode));
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("账号或密码为空");
            return resultMap;
        }

        try {
            resultMap = codeViewService.login(account, password);
        } catch (Exception e) {
            logger.error(String.format("interface:/api/login params: account=%s, password=%s, checkCode=%s", account, password, checkCode), e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
        }
        return resultMap;
    }

    /**
     * 退出登录（用户和工作人员使用同一个退出接口）
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/logout")
    public ResultMap login(HttpServletRequest request) {
        ResultMap resultMap = new ResultMap();
        String sessionKey = request.getHeader("sessionKey");
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");
        logger.info(String.format("interface:/api/logout params: sessionKey:%s, uid:%s", sessionKey, uid));

        try {
            codeViewService.logout(sessionKey);
        } catch (Exception e) {
            logger.error(String.format("interface:/api/logout params: sessionKey:%s, uid:%s", sessionKey, uid), e);
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
        }
        resultMap.setCode(CodeUtil.SUCCESS);
        resultMap.setDesc("退出成功");
        return resultMap;
    }
}
