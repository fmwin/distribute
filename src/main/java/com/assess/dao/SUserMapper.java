package com.assess.dao;

import com.assess.model.SUser;
import com.assess.model.SUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SUserMapper {
    long countByExample(SUserExample example);

    int deleteByExample(SUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SUser record);

    int insertSelective(SUser record);

    List<SUser> selectByExampleWithRowbounds(SUserExample example, RowBounds rowBounds);

    List<SUser> selectByExample(SUserExample example);

    SUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SUser record, @Param("example") SUserExample example);

    int updateByExample(@Param("record") SUser record, @Param("example") SUserExample example);

    int updateByPrimaryKeySelective(SUser record);

    int updateByPrimaryKey(SUser record);
}