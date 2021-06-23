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
 * @date 2020/2/22
 * 氡、钍射气及子体实体 --- 各地区氡浓度
 */
@Setter
@Getter
@TableName("region_radon_concentration")
public class RegionRadon extends DosageCommonEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 地区
     */
    private String region;
    /**
     * 平均氡浓度
     */
    private String averageRadon;

    /**
     * 误差
     */
    private String mistake;

    /**
     * 平均
     */
    @TableField(exist = false)
    private double average;
}
