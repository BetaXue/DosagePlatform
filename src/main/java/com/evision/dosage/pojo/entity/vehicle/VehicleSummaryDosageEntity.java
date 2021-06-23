package com.evision.dosage.pojo.entity.vehicle;

import com.evision.dosage.pojo.entity.DosageCommonEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 车辆剂量数据汇总表
 * @author DingZhanYang
 * @date 2020/2/19 14:05
 */
@Getter
@Setter
@NoArgsConstructor
public class VehicleSummaryDosageEntity extends DosageCommonEntity {
    /**
     * ID
     */
    private int id;
    /**
     * 交通方式
     */
    private String vehicleCategory;
    /**
     * 次出行时耗
     * 单位：min
     */
    private BigDecimal eachTravelDuration;
    /**
     * 工作日出行量
     * 单位：万人次
     */
    private Integer workdayTravelNumber;
    /**
     * 平均日出行人次
     */
    private BigDecimal daysTravelAveragePersonNumber;
    /**
     * 每人每日平均出行频次
     */
    private BigDecimal personDaysTravelAverageFrequency;
    /**
     * 剂量率
     * 公式，均值（交通工具对应的原始数据表的剂量率）
     * 单位：nGy/h
     */
    private BigDecimal gammaDosageRate;
    /**
     * 陆地伽马辐射剂量率
     * 单位：nGy/h
     */
    private BigDecimal landDosageRate;
    /**
     * 差值
     * 公式：gammaDosageRate - landDosageRate
     * 单位：nGy/h
     */
    private BigDecimal dosageRateDiff;
    /**
     * 日剂量变化
     * 公式：(gammaDosageRate - landDosageRate)*
     *      eachTravelDuration*personDaysTravelAverageFrequency/60
     * 单位：nGy/h
     */
    private BigDecimal daysDosageChange;
    /**
     * 年剂量变化
     * 公式：daysDosageChange*0.365
     * μGy/a
     */
    private BigDecimal yearDosageChange;
    /**
     * 年集体剂量
     * 公式：yearDosageChange*21.542
     * 单位：人·Sv
     */
    private BigDecimal yearCollectiveDosage;

}
