package com.evision.dosage.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 坐标类
 * @Author: Yu Xiao
 * @Date: 2020/2/20 14:03
 */
@Data
@AllArgsConstructor
public class Coordinate {
    //经度
    private BigDecimal lng;
    //纬度
    private BigDecimal lat;
}
