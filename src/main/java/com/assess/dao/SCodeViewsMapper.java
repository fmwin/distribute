package com.assess.dao;

import com.assess.model.SCodeViews;
import com.assess.model.SCodeViewsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SCodeViewsMapper {
    long countByExample(SCodeViewsExample example);

    int deleteByExample(SCodeViewsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SCodeViews record);

    int insertSelective(SCodeViews record);

    List<SCodeViews> selectByExampleWithRowbounds(SCodeViewsExample example, RowBounds rowBounds);

    List<SCodeViews> selectByExample(SCodeViewsExample example);

    SCodeViews selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SCodeViews record, @Param("example") SCodeViewsExample example);

    int updateByExample(@Param("record") SCodeViews record, @Param("example") SCodeViewsExample example);

    int updateByPrimaryKeySelective(SCodeViews record);

    int updateByPrimaryKey(SCodeViews record);
}