package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 陆地伽马射线剂量率
 *
 * @author Yu Xiao
 */
@Data
@TableName("gamma_ray")
public class GammaRay extends DosageCommonEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 点位名称
     */
    private String placeName;

    /**
     * 最大值
     */
    private String maxValue;

    /**
     * 最小值
     */
    private String minValue;

    /**
     * 均值
     */
    private String averageValue;

    /**
     * 网格点
     */
    private String gridNumber;

    /**
     * 地表类型
     */
    private String surfaceType;
    /**
     * 是否加密网格 1是0不是
     */
    private String encryGrid;

    @TableField(exist = false)
    private String avgValue;

    private static final long serialVersionUID = 1L;

    public GammaRay() {
        this.setDisabled(1);
        this.setDeleted(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GammaRay gammaRay = (GammaRay) o;
        return Objects.equals(placeName, gammaRay.placeName) &&
                Objects.equals(maxValue, gammaRay.maxValue) &&
                Objects.equals(minValue, gammaRay.minValue) &&
                Objects.equals(averageValue, gammaRay.averageValue) &&
                Objects.equals(gridNumber, gammaRay.gridNumber) &&
                Objects.equals(surfaceType, gammaRay.surfaceType) &&
                Objects.equals(encryGrid, gammaRay.encryGrid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeName, maxValue, minValue, averageValue, gridNumber, surfaceType, encryGrid);
    }
}