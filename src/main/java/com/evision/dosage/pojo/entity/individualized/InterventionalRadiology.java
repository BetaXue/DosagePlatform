package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

/**
 * @Classname InterventionalRadiology
 * @Description 介入放射学
 * @Date 2020/2/20 上午11:23
 * @Created by liufree
 */
@Data
public class InterventionalRadiology {

    /**
     * 心血管
     */
    private Integer cardiovascular;

    /**
     * 肿瘤介入
     */
    private Integer tumor;

    /**
     * 神经介入
     */
    private Integer nerve;

    /**
     * 外周血管介入
     */
    private Integer pv;

    /**
     * 非血管介入
     */
    private Integer nonVascular;

    /**
     * 诊断介入
     */
    private Integer diagnose;

    /**
     * 其他
     */
    private Integer other;

    /**
     * 合计
     */
    private Integer sum;

}
