package com.evision.dosage.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Yu Xiao
 * @Date: 2020/2/17 19:25
 */
@Getter
@AllArgsConstructor
public enum UniverseRayEnum {
    GRID_NUMBER("网格编号", 0, "grid_number", "gridNumber"),
    IN_BJ("是否在北京范围内（1是，0不是）", 1, "in_bj", "inBj"),
    AREA_CODE("区", 2, "area_code", "areaCode"),
    ALTITUDE("海拔", 3, "altitude", "altitude"),
    IONIZATION_DOSE_RATE("宇宙射线电离成分剂量率（nSv/h）", 4, "ionization_dose_rate", "ionizationDoseRate"),
    NEUTRON_DOSE_RATE("宇宙射线中子成分剂量率（nSv/h）", 5, "neutron_dose_rate", "neutronDoseRate"),
    COSMOGENIC_DOSE_RATE("宇生放射性核素剂量率（nSv/h）", 6, "cosmogenic_dose_rate", "cosmogenicDoseRate"),
    TOTAL_DOSE_RATE("宇宙射线总剂量率（nSv/h）", 7, "total_dose_rate", "totalDoseRate");
    private String fieldName;
    private int orderNo;
    private String dbFieldName;
    private String entityName;
}
