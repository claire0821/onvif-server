package com.root.onvif.mbg.mapper;

import com.root.onvif.mbg.model.TbDev;
import com.root.onvif.mbg.model.TbDevExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbDevMapper {
    long countByExample(TbDevExample example);

    int deleteByExample(TbDevExample example);

    int deleteByPrimaryKey(String ip);

    int insert(TbDev record);

    int insertSelective(TbDev record);

    List<TbDev> selectByExample(TbDevExample example);

    TbDev selectByPrimaryKey(String ip);

    int updateByExampleSelective(@Param("record") TbDev record, @Param("example") TbDevExample example);

    int updateByExample(@Param("record") TbDev record, @Param("example") TbDevExample example);

    int updateByPrimaryKeySelective(TbDev record);

    int updateByPrimaryKey(TbDev record);
}