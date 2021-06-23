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
public enum VehicleInnerDosageFieldsEnum {
    SERIAL_NUMBER("序号", 0, "serialNumber", "serialNumber"),
    DATA_CATEGORY("数据类别", 1, "data_category", "dataCategory"),
    ROAD_TYPE("公路类型", 2, "road_type", "roadType"),
    VEHICLE_TYPE("车辆类型", 3, "vehicle_type", "vehicleType"),
    MONITOR_NUMBER("车厢监测数量", 4, "monitor_number", "monitorNumber"),
    FRONT_RESULT_RANGE("车厢前侧", 5, "front_result_range", "frontResultRange"),
    MIDDLE_RESULT_RANGE("车厢中部", 6, "middle_result_range", "middleResultRange"),
    BACK_RESULT_RANGE("车厢后侧", 7, "back_result_range", "backResultRange"),
    ENTIRE_RESULT_RANGE("车厢范围", 8, "entirety_result_range", "entiretyResultRange");
    private String fieldName;
    private int orderNo;
    private String dbFieldName;
    private String entityName;
    private static Map<String, String> entityToDbNameMap = Arrays.stream(VehicleInnerDosageFieldsEnum.values())
            .collect(Collectors.toMap(VehicleInnerDosageFieldsEnum::getEntityName, VehicleInnerDosageFieldsEnum::getDbFieldName));
    public static String getDbFiledName(String entityName){
        return entityToDbNameMap.getOrDefault(entityName, "data_category");
    }
}
