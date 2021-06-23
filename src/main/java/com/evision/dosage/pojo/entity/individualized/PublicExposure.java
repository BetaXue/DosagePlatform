package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

/**
 * @Classname PublicExposure
 * @Description 公众照射
 * @Date 2020/2/20 下午12:11
 * @Created by liufree
 */
@Data
public class PublicExposure {


    /**
     * 每天抽烟数
     */
    private Integer dailyCigarette;

    /**
     * 每年坐国内航班
     */
    private Integer yearlyDomesticFlight;

    /**
     * 每年国际航班
     */
    private Integer yearlyInternationalFlight;


    /**
     * 每天坐公交小时数
     */
    private Double dailyBus;

    /**
     * 每天坐地铁小时数
     */
    private Double dailySubway;

    /**
     * 每天坐出租车小时数
     */
    private Double dailyTaxi;

    /**
     * 每天坐小汽车小时数
     */
    private Double dailyCar;

    /**
     * 年洗温泉
     */
    private Integer yearlyHotSpring;

    /**
     * 年使用天然气做饭天数
     */
    private Integer yearlyGasCooking;

    /**
     * 年吃香蕉
     */
    private Integer yearlyBanana;
}
