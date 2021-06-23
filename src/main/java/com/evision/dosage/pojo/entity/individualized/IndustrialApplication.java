package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

/**
 * @Classname IndustrialApplication
 * @Description 工业应用
 * @Date 2020/2/20 上午11:55
 * @Created by liufree
 */
@Data
public class IndustrialApplication {

    /**
     * 工业探伤
     */
    private Integer industrial;

    /**
     * 加速器运行
     */
    private Integer acceleratorRunning;

    /**
     * 工业辐照
     */
    private Integer industrialIrradiation;

    /**
     * 其他
     */
    private Integer other;

    /**
     * 测井
     */
    private Integer wellLog;

    /**
     * 放射性同位素生产
     */
    private Integer radioisotopeProduction;

    /**
     * 合计
     */
    private Integer sum;
}
