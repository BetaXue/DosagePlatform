package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

/**
 * @Classname Dental
 * @Description 牙科摄影
 * @Date 2020/2/20 上午11:02
 * @Created by liufree
 */
@Data
public class Dental {

    /**
     * 口内片
     */
    private Integer insideTheMouth;

    /**
     * 全息投影
     */
    private Integer hologram;

    /**
     * 口腔CT
     */
    private Integer mouthCT;

    /**
     * 合计
     */
    private Integer sum;

}
