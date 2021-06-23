package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Wei Zhenning
 * @version 1.0
 * @date 2020/2/24 12:20
 */
@Data
@TableName("pub_hot_spring_radon_concentration")
public class PubHotSpringRadonConcentrationEntity extends DosageCommonEntity{
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private int id;
    /**
     * 温泉地点
     */
    private String hotSpring;
    /**
     * 平均氡浓度
     */
    private String hotSpringRadonConcentration;

}
