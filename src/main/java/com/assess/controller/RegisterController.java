package com.assess.controller;

import com.assess.service.IRegisterService;
import com.assess.util.MapUtils;
import com.assess.util.ResultMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

@Controller
public class RegisterController {

    @Resource
    private IRegisterService registerService;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping("/api/register")
    @ResponseBody
    public ResultMap register(@RequestBody HashMap<String, String> user){
        ResultMap resultMap = new ResultMap();

        if (Objects.isNull(user)){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写信息");
            return resultMap;
        }
        if (MapUtils.isEmpty(user, "account")){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写手机号");
            return resultMap;
        }
        if (MapUtils.isEmpty(user, "password")){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写密码");
            return resultMap;
        }
        if (MapUtils.isEmpty(user, "code")){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写验证码");
            return resultMap;
        }

        if (!user.get("code").equals("666888")){
            resultMap.setCode(-1);
            resultMap.setDesc("验证码错误");
            return resultMap;
        }
        try {
            resultMap = registerService.register(user);
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(-1);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }
}
