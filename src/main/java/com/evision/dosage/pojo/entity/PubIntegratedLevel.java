package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Wei Zhenning
 * @version 1.0
 * @date 2020/2/18 11:25
 * <p>
 * 公共照射整体水平xlsx对应实体
 */
@Data
@TableName("pub_integrated_level")
public class PubIntegratedLevel extends DosageCommonEntity {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private int id;
    /**
     * 年剂量
     */
    private BigDecimal annualMeasurement;
    /**
     * 比例
     */
    private BigDecimal proportion;
    /**
     * 北京市集体剂量（人·Sv）
     */
    private BigDecimal collectiveDosage;
    /**
     * excel中的行数
     */
    private int orders;
}
