package com.assess.service.impl;

import com.assess.dao.SUrlMapper;
import com.assess.dao.SUserMapper;
import com.assess.enums.RoleEnum;
import com.assess.model.SUrl;
import com.assess.model.SUrlExample;
import com.assess.model.SUser;
import com.assess.model.SUserExample;
import com.assess.service.IRegisterService;
import com.assess.util.ResultMap;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RegisterServiceImpl implements IRegisterService {

    @Resource
    private SUserMapper sUserMapper;
    @Resource
    private SUrlMapper sUrlMapper;

    @Override
    public ResultMap register(String account, String password, String confirm, String realName, String disCode) throws Exception {
        ResultMap resultMap = new ResultMap();

        SUserExample example = new SUserExample();
        example.createCriteria().andAccountEqualTo(account);
        List<SUser> sUserList = sUserMapper.selectByExample(example);

        if (!CollectionUtils.isEmpty(sUserList)){
            resultMap.setCode(-1);
            resultMap.setDesc("用户已存在");
            return resultMap;
        }

        int disUid = 1;
        if (!StringUtils.isEmpty(disCode)){
            SUrlExample sUrlExample = new SUrlExample();
            sUrlExample.createCriteria().andCodeEqualTo(disCode);
            List<SUrl> urlList = sUrlMapper.selectByExample(sUrlExample);
            if (!CollectionUtils.isEmpty(urlList)){
                disUid = urlList.get(0).getUsedUid();
            }
        }
        SUser sUser = new SUser();
        sUser.setAccount(account);
        sUser.setPassword(password);
        sUser.setRealName(realName);
        sUser.setDisUid(disUid);
        sUser.setRole(RoleEnum.REGISTER.getCode());

        sUserMapper.insert(sUser);

        resultMap.setCode(1);
        resultMap.setDesc("注册成功");
        return resultMap;
    }
}
