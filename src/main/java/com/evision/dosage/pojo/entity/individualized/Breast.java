package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

/**
 * @Classname Breast
 * @Description 乳腺摄影
 * @Date 2020/2/20 上午11:06
 * @Created by liufree
 */
@Data
public class Breast {

    /**
     * 头尾位
     */
    private Integer headAndTailBit;

    /**
     * 内侧斜位
     */
    private Integer insideSlope;

    /**
     * 合计
     */
    private Integer sum;

}
