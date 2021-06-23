package com.evision.dosage.pojo.entity.individualized;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Classname StatisticalResult
 * @Description 统计结果
 * @Date 2020/2/21 下午4:19
 * @Created by liufree
 */
@Data
@TableName("statistical_result")
public class StatisticalResult {

    /**
     * 用户主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * individualized_dose_result表关联的id
     */
    private Integer idvId;

    /**
     * 用户名字
     */
    private String username;

    /**
     * 性别,0为男，1为女
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;


    /**
     * 天然照射剂量
     */
    private String naturalExposure;

    /**
     * 医疗照射剂量
     */
    private String medicalExposure;

    /**
     * 职业照射剂量
     */
    private String occupationalExposure;

    /**
     * 公众照射剂量
     */
    private String publicExposure;

    /**
     * 总剂量
     */
    private String sum;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
