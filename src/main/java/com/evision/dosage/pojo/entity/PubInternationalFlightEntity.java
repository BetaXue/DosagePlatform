package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Wei Zhenning
 * @version 1.0
 * @date 2020/2/20 14:51
 */

@Data
@TableName("pub_international_flight")
public class PubInternationalFlightEntity extends DosageCommonEntity{

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private int id;
    /**
     * 出发机场
     */
    private String departureAirport;
    /**
     * 到达机场
     */
    private String destinationAirport;
    /**
     * 飞行时间
     */
    private Integer flyTime;
    /**
     * 有效剂量
     */
    private BigDecimal effectiveDose;
    /**
     * 有效剂量率
     */
    private BigDecimal effectiveDoseRate;

}
