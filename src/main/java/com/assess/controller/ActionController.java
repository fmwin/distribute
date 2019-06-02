package com.assess.controller;

import com.assess.service.IActionService;
import com.assess.service.ICodeViewService;
import com.assess.util.Base64Util;
import com.assess.util.CodeUtil;
import com.assess.util.IpUtil;
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
    @Resource
    private ICodeViewService codeViewService;

    public static final String UID_HEAD = "uid_";

    /**
     * 用户点击动作记录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/api/action")
    public ResultMap action(HttpServletRequest request){
        ResultMap resultMap = new ResultMap();

        String sessionKey = request.getHeader("sessionKey");
        String uid = Base64Util.getOriginString(sessionKey).replace(UID_HEAD, "");

        String appId = request.getParameter("appId");
        String code = request.getParameter("disCode");
        String ip = IpUtil.getIpAddress(request);

        try {
            try {
                if (!StringUtils.isEmpty(uid) && !StringUtils.isEmpty(appId)) {
                    Integer uidInt = Integer.parseInt(uid);
                    Integer appIdInt = Integer.parseInt(appId);
                    actionService.addAction(uidInt, appIdInt, ip);
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("统计用户行为失败");
            }
            if (StringUtils.isEmpty(code)){
                code = "admin";
            }
            try {
                codeViewService.createOrModifyCodeView(code);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("加量失败");
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
