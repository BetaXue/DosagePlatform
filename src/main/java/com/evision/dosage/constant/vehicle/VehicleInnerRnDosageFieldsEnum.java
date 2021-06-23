package com.evision.dosage.constant.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DingZhanYang
 * @date 2020/2/21 16:58
 */
@Getter
@AllArgsConstructor
public enum  VehicleInnerRnDosageFieldsEnum {
    SERIAL_NUMBER("序号", 0, "serialNumber", "serialNumber"),
    DATA_CATEGORY("数据类别", 1, "data_category", "dataCategory"),
    LOCATION("地铁位置", 2, "location", "location"),
    ROUTE("线路名称", 3, "route", "route"),
    MONITOR_NUMBER("监测数量", 4, "monitor_number", "monitorNumber"),
    MEASURE_RESULT_MAX("最大值", 5, "measure_result_max", "measureResultMax"),
    MEASURE_RESULT_MIN("最小值", 6, "measure_result_min", "measureResultMin"),
    MEASURE_RESULT_AVERAGE("均值", 7, "measure_result_average", "measureResultAverage");
    private String fieldName;
    private int orderNo;
    private String dbFieldName;
    private String entityName;
    private static Map<String, String> entityToDbNameMap = Arrays.stream(VehicleInnerRnDosageFieldsEnum.values())
            .collect(Collectors.toMap(VehicleInnerRnDosageFieldsEnum::getEntityName, VehicleInnerRnDosageFieldsEnum::getDbFieldName));
    public static String getDbFiledName(String entityName){
        return entityToDbNameMap.getOrDefault(entityName, "data_category");
    }
}
