package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.PubIntegratedLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PubIntegratedLevelMapper extends BaseMapper<PubIntegratedLevel> {

    List<PubIntegratedLevel> selectAll(@Param("deleted") Integer deleted);

    List<PubIntegratedLevel> selectHistory(@Param("disabled") Integer disabled, @Param("deleted") Integer deleted,@Param("orders") Integer orders);
    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    List<PubIntegratedLevel> getDataById(@Param("id") int id);
}