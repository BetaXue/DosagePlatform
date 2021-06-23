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
public enum VehicleDosageRateFieldsEnum {
    SERIAL_NUMBER("序号", 0, "serialNumber", "serialNumber"),
    VEHICLE_CATEGORY("车辆类别", 1, "vehicle_category", "vehicleCategory"),
    ROUTE("线路", 2, "route", "route"),
    STATION("站点或位置", 3, "station", "station"),
    DOSAGE_RATE_ONE("剂量率1", 4, "dosage_rate_one", "dosageRateOne"),
    DOSAGE_RATE_TWO("剂量率2", 5, "dosage_rate_two", "dosageRateTwo"),
    DOSAGE_RATE_THREE("剂量率3", 6, "dosage_rate_three", "dosageRateThree"),
    DOSAGE_RATE_FOUR("剂量率4", 7, "dosage_rate_four", "dosageRateFour"),
    DOSAGE_RATE_FIVE("剂量率5", 8, "dosage_rate_five", "dosageRateFive"),
    DOSAGE_RATE_SIX("剂量率6", 9, "dosage_rate_six", "dosageRateSix"),
    DOSAGE_RATE_SEVEN("剂量率7", 10, "dosage_rate_seven", "dosageRateSeven"),
    DOSAGE_RATE_EIGHT("剂量率8", 11, "dosage_rate_eight", "dosageRateEight"),
    DOSAGE_RATE_NINE("剂量率9", 12, "dosage_rate_nine", "dosageRateNine"),
    DOSAGE_RATE_TEN("剂量率10", 13, "dosage_rate_ten", "dosageRateTen"),
    DOSAGE_RATE_AVERAGE("均值", 14, "dosage_rate_average", "dosageRateAverage"),
    DOSAGE_RATE_MIN("最小值", 15, "dosage_rate_min", "dosageRateMin"),
    DOSAGE_RATE_MAX("最大值", 16, "dosage_rate_max", "dosageRateMax"),
    CALIBRATION_FACTOR("校准因子", 17, "calibration_factor", "calibrationFactor"),
    CALIBRATION_DOSAGE_RATE_AVERAGE("均值*校准因子", 18, "calibration_dosage_rate_average", "calibrationDosageRateAverage"),
    CALIBRATION_DOSAGE_RATE_MIN("最小值*校准因子", 19, "calibration_dosage_rate_min", "calibrationDosageRateMin"),
    CALIBRATION_DOSAGE_RATE_MAX("最大值*校准因子", 20, "calibration_dosage_rate_max", "calibrationDosageRateMax");
    private String fieldName;
    private int orderNo;
    private String dbFieldName;
    private String entityName;
    private static Map<String, String> entityToDbNameMap = Arrays.stream(VehicleDosageRateFieldsEnum.values())
            .collect(Collectors.toMap(VehicleDosageRateFieldsEnum::getEntityName, VehicleDosageRateFieldsEnum::getDbFieldName));
    public static String getDbFiledName(String entityName){
        return entityToDbNameMap.getOrDefault(entityName, "vehicle_category");
    }
}
