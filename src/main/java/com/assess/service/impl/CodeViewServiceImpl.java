package com.assess.service.impl;

import com.assess.dao.SCodeViewsMapper;
import com.assess.dao.SUrlMapper;
import com.assess.model.SCodeViews;
import com.assess.model.SCodeViewsExample;
import com.assess.model.SUrl;
import com.assess.model.SUrlExample;
import com.assess.service.ICodeViewService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CodeViewServiceImpl implements ICodeViewService {

    @Resource
    private SCodeViewsMapper sCodeViewsMapper;
    @Resource
    private SUrlMapper sUrlMapper;

    @Override
    public void createOrModifyCodeView(String code) throws Exception {

        /** 查询是否存在code，没有的话，直接将code置为admin */
        if (!code.equals("admin")){
            SUrlExample sUrlExample = new SUrlExample();
            sUrlExample.createCriteria().andCodeEqualTo(code);
            List<SUrl> sUrlList = sUrlMapper.selectByExample(sUrlExample);
            if (Objects.isNull(sUrlList) || sUrlList.isEmpty()){
                code = "admin";
            }
        }

        SCodeViewsExample sCodeViewsExample = new SCodeViewsExample();
        sCodeViewsExample.createCriteria().andCodeEqualTo(code).andDateEqualTo(new Date());
        List<SCodeViews> sCodeViews = sCodeViewsMapper.selectByExample(sCodeViewsExample);

        if (Objects.isNull(sCodeViews) || sCodeViews.isEmpty()){
            SCodeViews codeView = new SCodeViews();
            codeView.setCode(code);
            codeView.setViews(1);

            sCodeViewsMapper.insert(codeView);
        }else{
            SCodeViews codeView = sCodeViews.get(0);
            codeView.setViews(codeView.getViews()+1);

            sCodeViewsMapper.updateByPrimaryKey(codeView);
        }
    }
}
