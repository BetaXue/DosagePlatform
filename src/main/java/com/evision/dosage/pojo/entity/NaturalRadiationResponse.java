package com.evision.dosage.pojo.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 雷亚亚
 * @date 2020/2/19
 * 天然辐射响应
 */
@Setter
@Getter
public class NaturalRadiationResponse {
    /**
     * 然辐射实体
     */
    private List<NaturalRadiation> naturalRadiations;

    /**
     * 年剂量合计
     */
    private double total;
}
