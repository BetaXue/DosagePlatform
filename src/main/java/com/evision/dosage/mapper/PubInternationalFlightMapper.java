package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.PubInternationalFlightEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PubInternationalFlightMapper extends BaseMapper<PubInternationalFlightEntity> {
    List<PubInternationalFlightEntity> selectAll(@Param("incloudDel")Integer incloudDel);

    List<PubInternationalFlightEntity> selectHistory(@Param("disabled") Integer disabled, @Param("deleted") Integer deleted,@Param("departureAirport") String departureAirport, @Param("destinationAirport") String destinationAirport);
}
