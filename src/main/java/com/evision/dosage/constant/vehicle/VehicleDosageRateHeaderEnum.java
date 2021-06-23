package com.evision.dosage.constant.vehicle;

import com.evision.dosage.pojo.model.DosageDbHeader;
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
public enum VehicleDosageRateHeaderEnum {
    VEHICLE_CATEGORY("车辆类别", "vehicleCategory", 0, "115"),
    ROUTE("线路", "route", 1, "115"),
    STATION("站点或位置", "station", 0, "115"),
    DOSAGE_RATE_ONE("剂量率1", "dosageRateOne", 1, "120"),
    DOSAGE_RATE_TWO("剂量率2", "dosageRateTwo", 1, "120"),
    DOSAGE_RATE_THREE("剂量率3", "dosageRateThree", 1, "120"),
    DOSAGE_RATE_FOUR("剂量率4", "dosageRateFour", 1, "120"),
    DOSAGE_RATE_FIVE("剂量率5", "dosageRateFive", 1, "120"),
    DOSAGE_RATE_SIX("剂量率6", "dosageRateSix", 1, "120"),
    DOSAGE_RATE_SEVEN("剂量率7", "dosageRateSeven", 1, "120"),
    DOSAGE_RATE_EIGHT("剂量率8", "dosageRateEight", 1, "120"),
    DOSAGE_RATE_NINE("剂量率9", "dosageRateNine", 1, "120"),
    DOSAGE_RATE_TEN("剂量率10", "dosageRateTen", 1, "120"),
    DOSAGE_RATE_AVERAGE("均值", "dosageRateAverage", 1, "120"),
    DOSAGE_RATE_MIN("最小值", "dosageRateMin", 1, "120"),
    DOSAGE_RATE_MAX("最大值", "dosageRateMax", 1, "120"),
    CALIBRATION_FACTOR("校准因子", "calibrationFactor", 1, "120"),
    CALIBRATION_DOSAGE_RATE_AVERAGE("均值*校准因子", "calibrationDosageRateAverage", 1, "170"),
    CALIBRATION_DOSAGE_RATE_MIN("最小值*校准因子", "calibrationDosageRateMin", 1, "170"),
    CALIBRATION_DOSAGE_RATE_MAX("最大值*校准因子", "calibrationDosageRateMax", 1, "170"),
    UPDATE_TIME("修改时间", "updateTime", 0, "180");
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
     * 展示宽度
     */
    private String width;


    /**
     * 获取Header数据集合
     *
     * @return Header
     */
    public static List<DosageHeader> convertHeaderEntity() {
        List<DosageHeader> headerEntities = new ArrayList<>();
        for (VehicleDosageRateHeaderEnum value : VehicleDosageRateHeaderEnum.values()) {
            DosageHeader headerEntity = new DosageHeader();
            headerEntity.setName(value.getName());
            headerEntity.setCode(value.getCode());
            headerEntity.setSupportSort(value.getSupportSort());
            headerEntities.add(headerEntity);
        }
        return headerEntities;
    }

    /**
     * 获取Header数据集合
     *
     * @return Header
     */
    public static List<DosageDbHeader> convertDbHeaderEntity() {
        List<DosageDbHeader> dosageDbHeaders = new ArrayList<>();
        for (VehicleDosageRateHeaderEnum value : VehicleDosageRateHeaderEnum.values()) {
            DosageDbHeader dosageDbHeader = new DosageDbHeader();
            dosageDbHeader.setLabel(value.getName());
            dosageDbHeader.setProp(value.getCode());
            dosageDbHeader.setWidth(value.getWidth());
            if (value.getSupportSort() == 1) {
                dosageDbHeader.setSortable("custom");
            }
            dosageDbHeaders.add(dosageDbHeader);
        }
        return dosageDbHeaders;
    }
}
