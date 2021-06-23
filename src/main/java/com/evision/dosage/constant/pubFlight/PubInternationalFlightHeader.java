package com.evision.dosage.constant.pubFlight;

import com.evision.dosage.pojo.model.DosageDbHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 国际航班 表头
 *
 * @author: liufree
 * @date: 2020-02-29 19:09
 **/
@Getter
@AllArgsConstructor
public enum PubInternationalFlightHeader {

    DEPARTURE_AIRPORT("出发机场", "departureAirport"),

    DESTINATION_AIRPORT("到达机场", "destinationAirport"),

    ACQUISITION_RATE("平均航班日频率", "effectiveDose"),

    FLY_TIME("飞行时间", "flyTime"),

    EFFECTIVE_DOSE_RATE("有效剂量率", "effectiveDoseRate"),

    UPDATE_TIME("最后修改日期", "updateTime");

    private String label;

    private String prop;


    public static List<DosageDbHeader> getDosageDbHeader() {
        List<DosageDbHeader> headerEntities = new ArrayList<>();
        for (PubInternationalFlightHeader value : PubInternationalFlightHeader.values()) {
            DosageDbHeader headerEntity = new DosageDbHeader();
            headerEntity.setLabel(value.getLabel());
            headerEntity.setProp(value.getProp());
            headerEntities.add(headerEntity);
        }
        return headerEntities;
    }

}
