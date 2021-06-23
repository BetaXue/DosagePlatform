package com.evision.dosage.constant.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DingZhanYang
 * @date 2020/2/21 16:57
 */
@Getter
@AllArgsConstructor
public enum  MetroTransferStationDosageFieldsEnum {
    SERIAL_NUMBER("序号", 0, "serialNumber", "serialNumber"),
    DATA_CATEGORY("数据类别", 1, "data_category", "dataCategory"),
    STATION("地铁换乘站", 2, "station", "station"),
    FIRST_QUARTER_MEASURE_RESULT("第一季度", 3, "first_quarter_measure_result", "firstQuarterMeasureResult"),
    SECOND_QUARTER_MEASURE_RESULT("第二季度", 4, "second_quarter_measure_result", "secondQuarterMeasureResult"),
    THIRD_QUARTER_MEASURE_RESULT("第三季度", 5, "third_quarter_measure_result", "thirdQuarterMeasureResult"),
    FOURTH_QUARTER_MEASURE_RESULT("第四季度", 6, "fourth_quarter_measure_result", "fourthQuarterMeasureResult");
    private String fieldName;
    private int orderNo;
    private String dbFieldName;
    private String entityName;
    private static Map<String, String> entityToDbNameMap = Arrays.stream(MetroTransferStationDosageFieldsEnum.values())
            .collect(Collectors.toMap(MetroTransferStationDosageFieldsEnum::getEntityName, MetroTransferStationDosageFieldsEnum::getDbFieldName));
    public static String getDbFiledName(String entityName){
        return entityToDbNameMap.getOrDefault(entityName, "data_category");
    }
}
