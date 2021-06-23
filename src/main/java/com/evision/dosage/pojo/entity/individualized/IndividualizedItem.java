package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Classname IndividualizedResult
 * @Description TODO
 * @Date 2020/2/20 下午2:45
 * @Created by liufree
 */
@Data
public class IndividualizedItem {

    /**
     * 剂量来源
     */
    private String doseSource;

    /**
     * 类别
     */
    private String type;

    /**
     * 年有效剂量
     */
    private String yearlyEffectiveDose;


    /**
     * 分项比例
     */
    private String componentProportion;

    /**
     * 总剂量比例
     */

    private String sumDoseProportion;

    public IndividualizedItem(String doseSource, String type) {
        this.doseSource = doseSource;
        this.type = type;
    }
}
