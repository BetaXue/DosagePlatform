package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author 雷亚亚
 * @date 2020/2/22
 * 氡、钍射气及子体实体 --- 各地区、类型氡浓度
 */
@Setter
@Getter
@TableName("radon_gas")
public class RegionGas extends DosageCommonEntity {


    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 地区
     */
    private String region;
    /**
     * 类型
     */
    private String type;

    /**
     * mean±sd
     */
    private String mean;

    /**
     * 获取率
     */
    private String acquisitionRate;

    /**
     * 变化范围
     */
    private String changeRange;


}
