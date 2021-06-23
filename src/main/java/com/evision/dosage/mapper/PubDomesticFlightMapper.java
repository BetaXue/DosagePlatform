package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.PubDomesticFlightEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PubDomesticFlightMapper extends BaseMapper<PubDomesticFlightEntity> {
    List<PubDomesticFlightEntity> selectAll( @Param("deleted") Integer deleted);

    List<PubDomesticFlightEntity> selectHistory(@Param("disabled") Integer disabled, @Param("deleted") Integer deleted, @Param("departureAirport") String departureAirport, @Param("destinationAirport") String destinationAirport);

}
