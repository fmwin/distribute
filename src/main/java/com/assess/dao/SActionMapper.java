package com.assess.dao;

import com.assess.model.SAction;
import com.assess.model.SActionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SActionMapper {
    long countByExample(SActionExample example);

    int deleteByExample(SActionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SAction record);

    int insertSelective(SAction record);

    List<SAction> selectByExampleWithRowbounds(SActionExample example, RowBounds rowBounds);

    List<SAction> selectByExample(SActionExample example);

    SAction selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SAction record, @Param("example") SActionExample example);

    int updateByExample(@Param("record") SAction record, @Param("example") SActionExample example);

    int updateByPrimaryKeySelective(SAction record);

    int updateByPrimaryKey(SAction record);
}