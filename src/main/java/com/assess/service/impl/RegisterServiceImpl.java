package com.assess.service.impl;

import com.assess.dao.SUserMapper;
import com.assess.enums.RoleEnum;
import com.assess.model.SUser;
import com.assess.model.SUserExample;
import com.assess.service.IRegisterService;
import com.assess.util.ResultMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements IRegisterService {

    @Resource
    private SUserMapper sUserMapper;

    @Override
    public ResultMap register(Map<String, String> user) throws Exception {
        ResultMap resultMap = new ResultMap();

        SUserExample example = new SUserExample();
        example.createCriteria().andAccountEqualTo(user.get("account"));
        List<SUser> sUserList = sUserMapper.selectByExample(example);

        if (null != sUserList && !sUserList.isEmpty()){
            resultMap.setCode(-1);
            resultMap.setDesc("用户已存在");
            return resultMap;
        }

        SUser sUser = new SUser();
        sUser.setAccount(user.get("account"));
        sUser.setPassword(user.get("password"));
        sUser.setRole(RoleEnum.REGISTER.getCode());

        sUserMapper.insert(sUser);

        resultMap.setCode(1);
        resultMap.setDesc("注册成功");
        return resultMap;
    }
}
