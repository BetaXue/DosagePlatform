package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

/**
 * @Classname OccupationalExposureOther
 * @Description 职业照射其他
 * @Date 2020/2/20 下午12:06
 * @Created by liufree
 */
@Data
public class OccupationalExposureOther {

    /**
     * 科学研究
     */
    private Integer scienceStudy;

    /**
     * 其他
     */
    private Integer other;

    /**
     * 教育
     */
    private Integer education;

    /**
     * 合计
     */
    private Integer sum;
}
