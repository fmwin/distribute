package com.assess.controller;

import com.assess.service.IActionService;
import com.assess.util.Base64Util;
import com.assess.util.CodeUtil;
import com.assess.util.ResultMap;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ActionController {

    @Resource
    private IActionService actionService;

    public static final String UID_HEAD = "uid_";

    @ResponseBody
    @RequestMapping("/api/action")
    public ResultMap action(HttpServletRequest request){
        ResultMap resultMap = new ResultMap();

        String sessionKey = request.getHeader("sessionKey");
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        String appId = request.getParameter("appId");

        try {
            if (!StringUtils.isEmpty(uid) && !StringUtils.isEmpty(appId)){
                Integer uidInt = Integer.parseInt(uid);
                Integer appIdInt = Integer.parseInt(appId);
                actionService.addAction(uidInt, appIdInt);
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        resultMap.setCode(CodeUtil.SUCCESS);
        return resultMap;
    }
}
