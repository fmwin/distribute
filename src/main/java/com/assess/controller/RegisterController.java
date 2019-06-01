package com.assess.controller;

import com.assess.service.IRegisterService;
import com.assess.util.MapUtils;
import com.assess.util.ResultMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

@Controller
public class RegisterController {

    @Resource
    private IRegisterService registerService;

    /**
     *用户注册
     * @param account
     * @param password
     * @param confirm
     * @param realName
     * @return
     */
    @RequestMapping("/api/register")
    @ResponseBody
    public ResultMap register(String account, String password, String confirm, String realName, @RequestParam(value = "disCode", required = false) String disCode){
        ResultMap resultMap = new ResultMap();

        if (StringUtils.isEmpty(account)){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写手机号");
            return resultMap;
        }
        if (StringUtils.isEmpty(password) || password.length()<6){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写密码");
            return resultMap;
        }
        if (StringUtils.isEmpty(confirm)){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写密码");
            return resultMap;
        }
        if (!password.equals(confirm)){
            resultMap.setCode(-1);
            resultMap.setDesc("密码不一致");
            return resultMap;
        }
        if (StringUtils.isEmpty(realName)){
            resultMap.setCode(-1);
            resultMap.setDesc("请正确填写真实姓名");
            return resultMap;
        }

        try {
            resultMap = registerService.register(account, password, confirm, realName, disCode);
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(-1);
            resultMap.setDesc("内部错误");
            return resultMap;
        }

        return resultMap;
    }
}
