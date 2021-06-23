package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

/**
 * @Classname CT
 * @Description 计算机断层摄影（CT)
 * @Date 2020/2/20 上午11:14
 * @Created by liufree
 */
@Data
public class Ct {

    /**
     * 头颅
     */
    private Integer head;

    /**
     * 颈部/颈椎
     */
    private Integer neck;

    /**
     * 五官
     */
    private Integer fiveSenses;

    /**
     * 胸部
     */
    private Integer chest;

    /**
     * 腹部
     */
    private Integer belly;

    /**
     * 胸椎
     */
    private Integer thoracicVertebra;

    /**
     * 腰椎
     */
    private Integer lumbarVertebra;

    /**
     * 骨盘及髋关节
     */
    private Integer pelvisAndHip;

    /**
     * 盘腔
     */
    private Integer pelvicCavity;

    /**
     * 四肢
     */
    private Integer limps;

    /**
     * 血管CTA冠脉
     */
    private Integer bloodCTACoronary;

    /**
     * 血管CTA头颈
     */
    private Integer bloodCTANeck;

    /**
     * 血管CTA外周
     */
    private Integer bloodCTAPeriphery;


    /**
     * 其他
     */
    private Integer other;

    /**
     * 合计
     */
    private Integer sum;
}