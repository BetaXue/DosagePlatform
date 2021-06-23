package com.evision.dosage.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 维护需要解析的Excel列表
 *
 * @author DingZhanYang
 * @date 2020/2/14 09:11
 */
@Getter
@AllArgsConstructor
public enum DosageExcelEnum {
    NATURAL_RADIONUCLIDE("天然放射性核素活度浓度","natural"),
    FOOD_RADIONUCLIDE("食材中放射性核素活度浓度", "food"),
    WATER_RADIONUCLIDE("水样中放射性核素活度浓度", "water"),
    AEROSOL_RADIONUCLIDE("气溶胶中放射性核素活度浓度", "aerosol"),
    POPULATION_DOSE("居民剂量首页", "populationDose"),
    GAMMA_RAY("陆地伽马辐射", "gammaRay"),
    VEHICLE_SUMMARY_DOSAGE("车辆汇总表", "vehicleSummaryDosage"),
    VEHICLE_DOSAGE("车辆γ剂量监测数据", "vehicleDosage"),
    VEHICLE_DOSAGE_RATE("车辆γ剂量率","vehicleDosageRate"),
    VEHICLE_INNER_DOSAGE("车厢γ剂量率结果统计","vehicleInnerDosage"),
    VEHICLE_INNER_RN_DOSAGE("车厢氡浓度监测数据", "vehicleInnerRnDosage"),
    METRO_TRANSFER_STATION_DOSAGE("地铁换乘站累积剂量率测量结果", "metroTransferStationDosage"),
    PUB_INTERNATIONAL_FLIGHT("国际航班","pubInternationalFlight"),
    PUB_DOMESTIC_FLIGHT("国内航班","pubDomesticFlight"),
    PUB_SUBWAY_RADON_CONCENTRATION("地铁氡浓度","pubSubwayRadonConcentration"),
    PUB_HOT_SPRING_RADON_CONCENTRATION("温泉设施氡浓度测量结果","pubHotSpringRadonConcentration"),
    NATURAL_RADIATION("天然照射首页","naturalRadiation"),
    AVERAGE_RADON("氡、钍射气及子体-平均氡浓度","averageRadon"),
    REGION_GAS("氡、钍射气及子体-各地区、类型氡浓度","regionGas"),
    REGION_RADON("氡、钍射气及子体-各地区氡浓度","regionRadon")
    ;

    /**
     * Excel名称
     */
    private String excelName;

    /**
     * Excel英文类别
     */
    private String excelType;

    public static DosageExcelEnum of(String excelType) {
        return Arrays.asList(DosageExcelEnum.values()).stream()
                .filter(x -> x.getExcelType().equals(excelType))
                .findFirst().orElseGet(null);
    }
}
