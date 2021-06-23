package com.evision.dosage.pojo.model;

import lombok.Data;

/**
 * 百度返回的经纬度
 *
 * @author: AubreyXue
 * @date: 2020-03-10 20:32
 **/
@Data
public class BaiDuEntity {

    /**
     * 经度
     */
    private String x;

    /**
     * 维度
     */
    private String y;
}
