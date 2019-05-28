package com.assess.dao;

import com.assess.model.SUrl;
import com.assess.model.SUrlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SUrlMapper {
    long countByExample(SUrlExample example);

    int deleteByExample(SUrlExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SUrl record);

    int insertSelective(SUrl record);

    List<SUrl> selectByExampleWithRowbounds(SUrlExample example, RowBounds rowBounds);

    List<SUrl> selectByExample(SUrlExample example);

    SUrl selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SUrl record, @Param("example") SUrlExample example);

    int updateByExample(@Param("record") SUrl record, @Param("example") SUrlExample example);

    int updateByPrimaryKeySelective(SUrl record);

    int updateByPrimaryKey(SUrl record);
}