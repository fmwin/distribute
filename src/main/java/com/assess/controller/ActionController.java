package com.assess.controller;

import com.assess.service.IActionService;
import com.assess.service.ICodeViewService;
import com.assess.util.Base64Util;
import com.assess.util.CodeUtil;
import com.assess.util.IpUtil;
import com.assess.util.ResultMap;
import org.apache.log4j.Logger;
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

    private Logger logger = Logger.getLogger(ActionController.class);

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
        //String code = request.getParameter("disCode");
        String ip = IpUtil.getIpAddress(request);
        Integer uidInt = Integer.parseInt(uid);

        logger.info(String.format("interface:action params:appId=%s, ip=%s", appId, ip));

        try {
            //记录用户点击行为
            try {
                if (!StringUtils.isEmpty(uid) && !StringUtils.isEmpty(appId)) {

                    Integer appIdInt = Integer.parseInt(appId);
                    resultMap.setData(actionService.addAction(uidInt, appIdInt, ip));
                }
            }catch (Exception e){
                logger.error("统计用户行为失败", e);
            }
            //推广量增加
            try {
                codeViewService.createOrModifyCodeView(uidInt);
            }catch (Exception e){
                logger.error("加量失败", e);
            }
        }catch (Exception e){
            logger.error("interface:action failed", e);
            resultMap.setCode(0);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        resultMap.setCode(CodeUtil.SUCCESS);
        return resultMap;
    }
}
