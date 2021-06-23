package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

import java.util.List;

/**
 * @Classname IndividualizedItemJson
 * @Description TODO
 * @Date 2020/2/22 上午1:04
 * @Created by liufree
 */
@Data
public class IndividualizedResultJson {


    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 输入表单
     */
    private IndividualizedDoseConvert input;

    /**
     * 计算结果
     */
    private List<IndividualizedItem> output;
}
