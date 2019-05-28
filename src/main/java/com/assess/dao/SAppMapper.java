package com.assess.dao;

import com.assess.model.SApp;
import com.assess.model.SAppExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SAppMapper {
    long countByExample(SAppExample example);

    int deleteByExample(SAppExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SApp record);

    int insertSelective(SApp record);

    List<SApp> selectByExampleWithRowbounds(SAppExample example, RowBounds rowBounds);

    List<SApp> selectByExample(SAppExample example);

    SApp selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SApp record, @Param("example") SAppExample example);

    int updateByExample(@Param("record") SApp record, @Param("example") SAppExample example);

    int updateByPrimaryKeySelective(SApp record);

    int updateByPrimaryKey(SApp record);
}