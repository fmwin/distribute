package com.assess.controller;

import com.assess.service.IAppService;
import com.assess.service.ICodeViewService;
import com.assess.util.CodeUtil;
import com.assess.util.ResultMap;
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

    /**
     * 用户获取app列表
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/api/index")
    public ResultMap index(HttpServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();

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
     * 用户登录
     * @param account
     * @param password
     * @param checkCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public ResultMap login(String account, String password, @RequestParam(value = "checkCode", required = false) String checkCode){
        ResultMap resultMap = new ResultMap();
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
            resultMap.setCode(CodeUtil.EMPTY);
            resultMap.setDesc("账号或密码为空");
            return resultMap;
        }

        try{
            resultMap = codeViewService.login(account, password);
        }catch (Exception e){
            resultMap.setCode(CodeUtil.INNER_ERROR);
            resultMap.setDesc("内部错误");
        }
        return resultMap;
    }
}
