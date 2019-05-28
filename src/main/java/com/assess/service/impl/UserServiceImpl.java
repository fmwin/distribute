package com.assess.service.impl;

import com.assess.dao.SUrlMapper;
import com.assess.dao.SUserMapper;
import com.assess.enums.RoleEnum;
import com.assess.model.SUrl;
import com.assess.model.SUrlExample;
import com.assess.model.SUser;
import com.assess.model.SUserExample;
import com.assess.response.WokerCodeResponse;
import com.assess.service.IUserService;
import com.assess.util.ResultMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private SUserMapper sUserMapper;
    @Resource
    private SUrlMapper sUrlMapper;

    @Override
    public SUser getUserByUid(Integer uid) throws Exception {
        SUser user = sUserMapper.selectByPrimaryKey(uid);
        return user;
    }


    @Override
    public ResultMap getWorkerList(Integer uid) throws Exception {
        ResultMap resultMap = new ResultMap();

        SUser user = sUserMapper.selectByPrimaryKey(uid);
        if (Objects.isNull(user)){
            resultMap.setCode(-1);
            resultMap.setDesc("您不是系统用户，请先注册");
            return resultMap;
        }
        if (!user.getRole().contains(RoleEnum.SUPER_ADMIN.getCode())){
            resultMap.setCode(-1);
            resultMap.setDesc("您不是系统管理员");
            return resultMap;
        }

        SUserExample sUserExample = new SUserExample();
        sUserExample.createCriteria().andRoleLike("%"+RoleEnum.WORKER.getCode()+"%");

        List<SUser> userList = sUserMapper.selectByExample(sUserExample);

        resultMap.setCode(1);
        resultMap.setDesc("获取工作人员列表成功");
        resultMap.setData(userList);

        return resultMap;
    }

    @Override
    public ResultMap getWorkerCodeList(Integer uid) throws Exception {
        ResultMap resultMap = new ResultMap();

        SUser user = sUserMapper.selectByPrimaryKey(uid);
        if (Objects.isNull(user)){
            resultMap.setCode(-1);
            resultMap.setDesc("您不是系统用户，请先注册");
            return resultMap;
        }
        if (!user.getRole().contains(RoleEnum.SUPER_ADMIN.getCode()) && !user.getRole().contains(RoleEnum.WORKER.getCode())){
            resultMap.setCode(-1);
            resultMap.setDesc("您不是系统用户");
            return resultMap;
        }

        if (user.getRole().contains(RoleEnum.SUPER_ADMIN.getCode())) {
            SUserExample sUserExample = new SUserExample();
            sUserExample.createCriteria().andRoleLike("%" + RoleEnum.WORKER.getCode() + "%");

            List<SUser> userList = sUserMapper.selectByExample(sUserExample);
            List<WokerCodeResponse> wokerCodeResponseList = new ArrayList<>();

            for (SUser sUser : userList) {
                WokerCodeResponse wokerCodeResponse = new WokerCodeResponse();
                wokerCodeResponse.setId(sUser.getId());
                wokerCodeResponse.setAccount(sUser.getAccount());
                wokerCodeResponse.setRealName(sUser.getRealName());
                wokerCodeResponse.setCreateDate(sUser.getCreateDate());
                wokerCodeResponse.setRole(sUser.getRole());

                SUrlExample sUrlExample = new SUrlExample();
                sUrlExample.createCriteria().andUsedUidEqualTo(sUser.getId());
                List<SUrl> sUrlList = sUrlMapper.selectByExample(sUrlExample);
                if (!sUrlList.isEmpty()) {
                    SUrl sUrl = sUrlList.get(0);
                    wokerCodeResponse.setCode(sUrl.getCode());
                    wokerCodeResponse.setUrl(sUrl.getUrl());
                }

                wokerCodeResponseList.add(wokerCodeResponse);
            }

            resultMap.setCode(1);
            resultMap.setDesc("获取工作人员列表成功");
            resultMap.setData(wokerCodeResponseList);

            return resultMap;
        }else{
            if (user.getRole().contains(RoleEnum.WORKER.getCode())){
                SUrlExample sUrlExample = new SUrlExample();
                sUrlExample.createCriteria().andUsedUidEqualTo(user.getId());

                List<SUrl> sUrlList = sUrlMapper.selectByExample(sUrlExample);
                List<WokerCodeResponse> wokerCodeResponseList = new ArrayList<>();

                WokerCodeResponse wokerCodeResponse = new WokerCodeResponse();
                wokerCodeResponse.setId(user.getId());
                wokerCodeResponse.setAccount(user.getAccount());
                wokerCodeResponse.setRealName(user.getRealName());
                wokerCodeResponse.setCreateDate(user.getCreateDate());
                wokerCodeResponse.setRole(user.getRole());

                if (!sUrlList.isEmpty()){
                    wokerCodeResponse.setCode(sUrlList.get(0).getCode());
                    wokerCodeResponse.setUrl(sUrlList.get(0).getUrl());
                }
                wokerCodeResponseList.add(wokerCodeResponse);

                resultMap.setCode(1);
                resultMap.setDesc("获取链接列表成功");
                resultMap.setData(wokerCodeResponseList);
                return resultMap;
            }
        }
        resultMap.setCode(0);
        resultMap.setDesc("您没有访问权限");
        return resultMap;
    }

    @Override
    public ResultMap addUser(Integer uid, String account, String password, String realName, String role) throws Exception {
        ResultMap resultMap = new ResultMap();

        SUser sUser = new SUser();
        sUser.setAccount(account);
        sUser.setPassword(password);
        sUser.setRealName(realName);
        sUser.setRole(role);

        sUserMapper.insert(sUser);

        resultMap.setCode(1);
        resultMap.setDesc("创建用户成功");
        resultMap.setData(sUser.getId());

        return resultMap;
    }
}
