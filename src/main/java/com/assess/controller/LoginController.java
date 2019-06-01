package com.assess.controller;

import com.assess.service.IAppService;
import com.assess.service.ICodeViewService;
import com.assess.util.ResultMap;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

@Controller
public class LoginController {

    @Resource
    private IAppService appService;
    @Resource
    private ICodeViewService codeViewService;

    @ResponseBody
    @RequestMapping("/api/index")
    public ResultMap index(ServletRequest servletRequest){
        ResultMap resultMap = new ResultMap();
        String code = servletRequest.getParameter("disCode");

        try {
            if (StringUtils.isEmpty(code)){
                code = "admin";
            }
            codeViewService.createOrModifyCodeView(code);
            resultMap = appService.getAppList();
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }
}
