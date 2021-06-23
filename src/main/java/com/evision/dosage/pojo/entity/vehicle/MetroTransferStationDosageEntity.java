package com.evision.dosage.pojo.entity.vehicle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.evision.dosage.pojo.entity.DosageCommonEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 地铁换乘站累积剂量率测量结果
 *
 * @author DingZhanYang
 * @date 2020/2/20 13:51
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("metro_transfer_station_dosage")
public class MetroTransferStationDosageEntity extends DosageCommonEntity {
    @TableId(type = IdType.AUTO)
    private int id;
    /**
     * 数据类别
     */
    private String dataCategory;
    /**
     * 地铁换乘站
     */
    private String station;
    /**
     * 累计剂量测量结果-第一季度
     * 单位：nSv/h
     */
    private BigDecimal firstQuarterMeasureResult;
    /**
     * 累计剂量测量结果-第二季度
     * 单位：nSv/h
     */
    private BigDecimal secondQuarterMeasureResult;
    /**
     * 累计剂量测量结果-第三季度
     * 单位：nSv/h
     */
    private BigDecimal thirdQuarterMeasureResult;
    /**
     * 累计剂量测量结果-第四季度
     * 单位：nSv/h
     */
    private BigDecimal fourthQuarterMeasureResult;
}
