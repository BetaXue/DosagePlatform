package com.evision.dosage.pojo.entity.vehicle;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evision.dosage.pojo.entity.DosageCommonEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 车辆伽马剂量率
 * @author DingZhanYang
 * @date 2020/2/20 14:00
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("vehicle_dosage_rate")
public class VehicleDosageRateEntity extends DosageCommonEntity {
    /**
     * ID
     */
    private int id;
    /**
     * 车辆类别
     */
    private String vehicleCategory;
    /**
     * 线路
     */
    private String route;
    /**
     * 站点或位置
     */
    private String station;
    /**
     * 剂量率1
     */
    private BigDecimal dosageRateOne;
    /**
     * 剂量率2
     */
    private BigDecimal dosageRateTwo;
    /**
     * 剂量率3
     */
    private BigDecimal dosageRateThree;
    /**
     * 剂量率4
     */
    private BigDecimal dosageRateFour;
    /**
     * 剂量率5
     */
    private BigDecimal dosageRateFive;
    /**
     * 剂量率6
     */
    private BigDecimal dosageRateSix;
    /**
     * 剂量率7
     */
    private BigDecimal dosageRateSeven;
    /**
     * 剂量率8
     */
    private BigDecimal dosageRateEight;
    /**
     * 剂量率9
     */
    private BigDecimal dosageRateNine;
    /**
     * 剂量率10
     */
    private BigDecimal dosageRateTen;
    /**
     * 均值
     * 公式：AVERAGE(dosageRateOne to dosageRateTen)
     */
    private BigDecimal dosageRateAverage;
    /**
     * 最小值
     * 公式：MIN(dosageRateOne to dosageRateTen)
     */
    private BigDecimal dosageRateMin;
    /**
     * 最大值
     * 公式：MAX(dosageRateOne to dosageRateTen)
     */
    private BigDecimal dosageRateMax;
    /**
     * 校准因子
     */
    private BigDecimal calibrationFactor;
    /**
     * 均值*校准因子
     */
    private BigDecimal calibrationDosageRateAverage;
    /**
     * 最小值*校准因子
     */
    private BigDecimal calibrationDosageRateMin;
    /**
     * 最大值*校准因子
     */
    private BigDecimal calibrationDosageRateMax;
}
