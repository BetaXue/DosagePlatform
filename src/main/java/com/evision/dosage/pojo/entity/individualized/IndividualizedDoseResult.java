package com.evision.dosage.pojo.entity.individualized;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Classname IndicidualizedDoseResult
 * @Description 个人剂量存储结果，将表格以json形式存储，而不用每一项都存储下来
 * @Date 2020/2/21 下午5:33
 * @Created by liufree
 */
@Data
@TableName("individualized_dose_result")
public class IndividualizedDoseResult {

    /**
     * id，主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户输入
     */
    private String input;

    /**
     * 计算结果
     */
    private String result;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
