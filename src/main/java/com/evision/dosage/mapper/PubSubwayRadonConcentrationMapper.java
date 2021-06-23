package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.PubSubwayRadonConcentrationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PubSubwayRadonConcentrationMapper extends BaseMapper<PubSubwayRadonConcentrationEntity> {
    List<PubSubwayRadonConcentrationEntity> selectAll(@Param("disabled") Integer disabled, @Param("deleted") Integer deleted);

    List<PubSubwayRadonConcentrationEntity> selectHistory(@Param("disabled") Integer disabled, @Param("deleted") Integer deleted,@Param("subwayName") String subwayName);
}
