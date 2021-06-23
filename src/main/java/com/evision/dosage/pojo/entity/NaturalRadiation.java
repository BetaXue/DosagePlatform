package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author 雷亚亚
 * @date 2020/2/19
 * 天然辐射实体
 */
@Setter
@Getter
@TableName("natural_radiation")
public class NaturalRadiation extends DosageCommonEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 列号
     */
    private int colNumber;
    /**
     * 剂量率
     */
    private String doseRate;
    /**
     * 年剂量
     */
    private String annualDose;

    /**
     * 年剂量合计
     */
    @TableField(exist = false)
    private double total;
}
