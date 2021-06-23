package com.evision.dosage.pojo.entity.vehicle;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evision.dosage.pojo.entity.DosageCommonEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 车厢氡浓度监测数据
 * @author DingZhanYang
 * @date 2020/2/20 11:32
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("vehicle_inner_rn_dosage")
public class VehicleInnerRnDosageEntity extends DosageCommonEntity {
    /**
     * ID
     */
    private int id;
    /**
     * 数据类别
     */
    private String dataCategory;
    /**
     * 地铁位置
     */
    private String location;
    /**
     * 线路名称
     */
    private String route;
    /**
     * 监测数量
     */
    private Integer monitorNumber;
    /**
     * 测量结果最大值范围
     * 单位 Bq/m^3
     */
    private BigDecimal measureResultMax;
    /**
     * 测量结果最小值范围
     * 单位 Bq/m^3
     */
    private BigDecimal measureResultMin;
    /**
     * 测量结果均值范围
     * 单位 Bq/m^3
     */
    private BigDecimal measureResultAverage;
}
