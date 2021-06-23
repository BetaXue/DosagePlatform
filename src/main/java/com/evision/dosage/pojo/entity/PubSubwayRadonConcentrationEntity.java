package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Wei Zhenning
 * @version 1.0
 * @date 2020/2/21 11:01
 */
@Data
@TableName("pub_subway_radon_concentration")
public class PubSubwayRadonConcentrationEntity extends DosageCommonEntity{
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private int id;
    /**
     * 站点名称
     */
    private String subwayName;
    /**
     * 平均氡浓度
     */
    private BigDecimal avgRadonConcentration;
    /**
     * 不确定度
     */
    private BigDecimal uncertainty;
}
