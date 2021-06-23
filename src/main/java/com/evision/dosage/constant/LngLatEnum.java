package com.evision.dosage.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 经纬度
 * @Author: Yu Xiao
 * @Date: 2020/2/20 13:46
 */
@AllArgsConstructor
@Getter
public enum LngLatEnum {
    SOURTH_WEST("sourthwest","115.4","39.5"),
    NORTH_EAST("northeast","117.5","41.05"),
    ;
    private String fieldName;
    //经度
    private String lng;
    //纬度
    private String lat;

}
