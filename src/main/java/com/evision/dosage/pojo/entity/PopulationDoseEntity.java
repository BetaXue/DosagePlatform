package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

/**
 * 居民剂量首页实体
 *
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-19 14:35
 */
@Data
@Excel(value = "居民剂量首页数据")
@TableName(value = "population_dose")
public class PopulationDoseEntity extends DosageCommonEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 类别名称
     */
    private String category;

    @ExcelField
    @TableField(exist = false)
    private String fistName;

    @ExcelField
    @TableField(exist = false)
    private String secondName;

    @ExcelField
    @TableField(exist = false)
    private String threeName;

    /**
     * 年有效剂量
     */
    @ExcelField
    private String annualEffectiveDose;

    /**
     * 分项比例
     */
    @ExcelField
    private String subItemRatio;

    /**
     * 总剂量比例
     */
    @ExcelField
    private String totalDoseRatio;

    /**
     * 排序
     */
    private Integer orders;
}
