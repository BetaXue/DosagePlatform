package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

/**
 * @Classname PublicExposure
 * @Description x射线
 * @Date 2020/2/20 下午12:11
 * @Created by liufree
 */

@Data
public class Xray {
    /**
     * 胸部摄影正位
     */
    private Integer chestRight;

    /**
     * 胸部摄影侧位
     */
    private Integer chestSide;

    /**
     * 胸部摄影双斜位
     */
    private Integer chestDoubleOblique;

    /**
     * 头颅摄影正位
     */
    private Integer headRight;

    /**
     * 头颅摄影侧位
     */
    private Integer headSide;

    /**
     * 颈椎摄影正位
     */
    private Integer neckRight;

    /**
     * 颈椎摄影侧位
     */
    private Integer neckSide;

    /**
     * 颈椎摄影双斜位
     */
    private Integer neckDoubleOblique;

    /**
     * 胸椎摄影正位
     */
    private Integer thoracicVertebraRight;

    /**
     * 胸椎摄影侧位
     */
    private Integer thoracicVertebraSide;

    /**
     * 腰椎摄影正位
     */
    private Integer lumbarVertebraRight;

    /**
     * 腰椎摄影侧位
     */
    private Integer lumbarVertebraSide;

    /**
     * 腰椎摄影双斜位
     */
    private Integer lumbarVertebraDoubleOblique;

    /**
     * 五官
     */
    private Integer fiveSenses;

    /**
     * 骨盘
     */
    private Integer pelvis;

    /**
     * 髋关节
     */
    private Integer hip;

    /**
     * 四肢及关节摄影
     */
    private Integer limbsAndJoints;

    /**
     * 锁骨及肩关节
     */
    private Integer collarboneAndShoulderJoints;

    /**
     * 腹部摄影
     */
    private Integer belly;

    /**
     * 普通x射线诊断其他
     */
    private Integer other;

    /**
     * 胸部透视门诊
     */
    private Integer chestPerspectiveClinic;

    /**
     * 胸部透视体检
     */
    private Integer chestPerspectivePhysical;

    /**
     * 腹部透视
     */
    private Integer bellyPerspective;

    /**
     * 合计
     */
    private Integer sum;
}
