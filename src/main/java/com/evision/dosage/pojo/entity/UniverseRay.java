package com.evision.dosage.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 宇宙射线剂量率
 *
 * @author Yu Xiao
 */
@Data
@TableName("universe_ray")
public class UniverseRay extends DosageCommonEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 网格编号
     */
    private String gridNumber;

    /**
     * 是否在北京范围内 1是0不是
     */
    private String inBj;

    /**
     * 区代码
     */
    private String areaCode;

    /**
     * 海拔
     */
    private String altitude;

    /**
     * 宇宙射线电离成分剂量率
     */
    private String ionizationDoseRate;

    /**
     * 宇宙射线中子成分剂量率
     */
    private String neutronDoseRate;

    /**
     * 宇生放射性核素剂量率
     */
    private String cosmogenicDoseRate;

    /**
     * 宇宙射线总剂量率
     */
    private String totalDoseRate;

    private static final long serialVersionUID = 1L;

    public UniverseRay() {
        this.setDisabled(1);
        this.setDeleted(0);
    }
}