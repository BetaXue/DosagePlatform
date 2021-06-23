package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

/**
 * @Classname MedicalApplication
 * @Description 医学应用
 *
 * @Date 2020/2/20 上午11:31
 * @Created by liufree
 */
@Data
public class MedicalApplication {

    /**
     * 诊断放射学
     */
    private Integer diagnosticRadiology;

    /**
     * 放射治疗
     */
    private Integer radiotherapy;

    /**
     * 介入放射学
     */
    private Integer interventionalRadiology;

    /**
     * 核医学
     */
    private Integer nuclearMedicine;

    /**
     * 其他
     */
    private Integer other;

    /**
     * 牙科放射学
     */
    private Integer dentalRadiology;

    /**
     * 总计
     */
    private Integer sum;
}
