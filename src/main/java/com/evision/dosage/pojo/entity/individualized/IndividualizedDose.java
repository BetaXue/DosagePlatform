package com.evision.dosage.pojo.entity.individualized;

import lombok.Data;

/**
 * @Classname IndividualizedDose
 * @Description 个体化剂量输入表单
 * @Date 2020/2/20 上午10:03
 * @Created by liufree
 */

@Data
public class IndividualizedDose {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 性别,0为男，1为女
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 职业
     */
    private String occupation;
    /**
     * 每天户外停留时间
     */
    private Double stayTime;

    /**
     * X射线诊断
     */
    private Xray xray;

    /**
     * 牙科摄影
     */
    private Dental dental;

    /**
     * 乳腺摄影
     */
    private Breast breast;

    /**
     * 计算机断层摄影（CT）
     */
    private Ct ct;

    /**
     * 介入放射学
     */
    private InterventionalRadiology interventionalRadiology;

    /**
     * 医学应用
     */
    private MedicalApplication medicalApplication;

    /**
     * 工业应用
     */
    private IndustrialApplication industrialApplication;

    /**
     * 职业照射其他
     */
    private OccupationalExposureOther occupationalExposureOther;

    /**
     * 公众照射
     */
    private PublicExposure publicExposure;


}


