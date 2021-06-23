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
 * 氡、钍射气及子体实体 --- 平均氡浓度
 */
@Setter
@Getter
@TableName("average_radon_concentration")
public class AverageRadon extends DosageCommonEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 地区
     */
    private String region;
    /**
     * 平均氡浓度
     */
    private String averageRadon;

}
