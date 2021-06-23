package com.evision.dosage.constant.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DingZhanYang
 * @date 2020/2/24 10:20
 */
@Getter
@AllArgsConstructor
public enum  VehicleSummaryDosageConstantsEnum {
    BUS("公交车", "常规公交", 52., 630, 614.48, 0.29,  -1., 61.33),
    METRO("地铁", "轨道交通", 55.2, 635, 619.36, 0.29, -1., 61.33),
    TAXI("小轿车", "出租车", 34.3, 103, 100.46, 0.05, -1., 61.33),
    CAR("小轿车AND越野车", "小汽车", 33.3, 916, 893.44, 0.41, -1., 61.33),
    BICYCLE("", "自行车", 25.5, 453, 441.84, 0.21, 61.33, 61.33),
    WALK("", "步行", 11.7, 1145, 1116.8, 0.52, 61.33, 61.33)
    ;
    /**
     * 对应数据表中vehicle_category名
     */
    private String name;
    /**
     * 交通方式
     */
    private String vehicleCategory;
    /**
     * 次出行时耗
     * 单位：min
     */
    private Double eachTravelDuration;
    /**
     * 工作日出行量
     * 单位：万人次
     */
    private Integer workdayTravelNumber;
    /**
     * 平均日出行人次
     */
    private Double daysTravelAveragePersonNumber;
    /**
     * 每人每日平均出行频次
     */
    private Double personDaysTravelAverageFrequency;
    /**
     * 剂量率
     * 公式，均值（交通工具对应的原始数据表的剂量率）
     * 单位：nGy/h
     */
    private Double gammaDosageRate;
    /**
     * 陆地伽马辐射剂量率
     * 单位：nGy/h
     */
    private Double landDosageRate;

    private static Map<String, String> nameAndCategoryMap = Arrays.stream(VehicleSummaryDosageConstantsEnum.values())
            .collect(Collectors.toMap(VehicleSummaryDosageConstantsEnum::getVehicleCategory, VehicleSummaryDosageConstantsEnum::getName));

    public static String getNameWithCategory(String vehicleCategory){
        return nameAndCategoryMap.get(vehicleCategory);
    }
}
