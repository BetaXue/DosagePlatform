package com.evision.dosage.constant.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DingZhanYang
 * @date 2020/2/21 9:49
 */
@Getter
@AllArgsConstructor
public enum VehicleDosageFieldsEnum {
    SERIAL_NUMBER("序号", 0, "serialNumber", "serialNumber"),
    DATA_CATEGORY("数据类别", 1, "data_category", "dataCategory"),
    LOCATION("位置", 2, "location", "location"),
    ROAD_NAME("名称", 3, "road_name", "roadName"),
    ROUTE("线路", 4, "route", "route"),
    DRIVING_DATE("行驶状态", 5, "driving_state", "drivingState"),
    MONITOR_NUMBER("监测数量", 6, "monitor_number", "monitorNumber"),
    MEASURE_RESULT_MAX_RANGE("最大值范围", 7, "measure_result_max_range", "measureResultMaxRange"),
    MEASURE_RESULT_MIN_RANGE("最小值范围", 8, "measure_result_min_range", "measureResultMinRange"),
    MEASURE_RESULT_AVERAGE_RANGE("均值范围", 9, "measure_result_average_range", "measureResultAverageRange");
    private String fieldName;
    private int orderNo;
    private String dbFieldName;
    private String entityName;
    private static Map<String, String> entityToDbNameMap = Arrays.stream(VehicleDosageFieldsEnum.values())
            .collect(Collectors.toMap(VehicleDosageFieldsEnum::getEntityName, VehicleDosageFieldsEnum::getDbFieldName));
    public static String getDbFiledName(String entityName){
        return entityToDbNameMap.getOrDefault(entityName, "data_category");
    }
}
