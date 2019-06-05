package com.assess.filter;

import com.assess.util.RedisUtil;
import com.assess.util.ResultMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class UserInterceptor implements HandlerInterceptor {
    //Logger logger = Logger.getLogger(UserInterceptor.class);
    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String sessionKey = request.getHeader("sessionKey");
        String sessionValue = request.getHeader("sessionValue");
        String url = request.getRequestURL().toString();

        //logger.info("access url: "+ url +"; sessionKey: "+ sessionKey +"; sessionValue: "+sessionValue);

        if(url.contains("user/login") || url.contains("/back/login")
                || url.contains("/api/register") || url.contains("/api/index") || url.contains("/api/login")
                || url.contains("/api/logout")){
            return true;
        }
		/*if(url.contains("fund/")){
			return true;
		}*/
        ResultMap result = new ResultMap();
        ObjectMapper om = new ObjectMapper();
        if(StringUtils.isEmpty(sessionKey) || StringUtils.isEmpty(sessionValue)){
			/*result.setCode(Constance.RESPONSE_USER_ERROR);
			result.setMsg("请登录！");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(om.writeValueAsString(result));
			return false;*/
            response.sendError(401, "fail");
            return false;
        }
        Object redisValue = redisUtil.get(sessionKey, 1);
        if(Objects.isNull(redisValue)){
			/*result.setCode(Constance.RESPONSE_USER_ERROR);
			result.setMsg("请登录");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(om.writeValueAsString(result));*/
            response.sendError(401, "fail");
            return false;
        }
        String redisValueStr = redisValue.toString();
        if(!redisValueStr.equals(sessionValue)){
			/*result.setCode(Constance.RESPONSE_USER_ERROR);
			result.setMsg("请登录");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(om.writeValueAsString(result));*/
            response.sendError(401, "fail");
            return false;
        }
        long time = 86400;
        redisUtil.set(sessionKey, sessionValue, time);
        request.setAttribute("sessionKey", sessionKey);
        request.setAttribute("sessionValue", sessionValue);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }

}
