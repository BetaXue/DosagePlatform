package com.evision.dosage.constant.vehicle;

import com.evision.dosage.pojo.model.DosageHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/21 20:47
 */
@Getter
@AllArgsConstructor
public enum VehicleSummaryDosageHeaderEnum {
    VEHICLE_CATEGORY("交通方式", "vehicleCategory", 0),
    EACH_TRAVEL_DURATION("次出行时耗(min)", "eachTravelDuration", 0),
    WORKDAY_TRAVEL_NUMBER("工作日出行量（万人次）", "workdayTravelNumber", 0),
    DAYS_TRAVEL_AVERAGE_PERSON_NUMBER("平均日出行人次", "daysTravelAveragePersonNumber", 0),
    PERSON_DAYS_TRAVEL_AVERAGE_FREQUENCY("每人每日平均出行频次", "personDaysTravelAverageFrequency", 0),
    GAMMA_DOSAGE_RATE("剂量率(nGy/h)", "gammaDosageRate", 0),
    LOAN_DOSAGE_RATE("陆地伽马辐射剂量率(nGy/h)", "landDosageRate", 0),
    DOSAGE_RATE_DIFF("差值", "dosageRateDiff", 0),
    DAYS_DOSAGE_CHANGE("日剂量变化(nGy/d)", "daysDosageChange", 0),
    YEAR_DOSAGE_CHANGE("年剂量变化(μGy/a)", "yearDosageChange", 0),
    YEAR_COLLECTIVE_DOSAGE("年集体剂量(人·Sv)", "yearCollectiveDosage", 0),
    UPDATE_TIME("修改时间", "updateTime", 0);
    /**
     * 中文名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 是否允许排序 1 允许 0 不允许
     */
    private Integer supportSort;


    /**
     * 获取Header数据集合
     *
     * @return Header
     */
    public static List<DosageHeader> convertHeaderEntity() {
        List<DosageHeader> headerEntities = new ArrayList<>();
        for (VehicleSummaryDosageHeaderEnum value : VehicleSummaryDosageHeaderEnum.values()) {
            DosageHeader headerEntity = new DosageHeader();
            headerEntity.setName(value.getName());
            headerEntity.setCode(value.getCode());
            headerEntity.setSupportSort(value.getSupportSort());
            headerEntities.add(headerEntity);
        }
        return headerEntities;
    }
}
