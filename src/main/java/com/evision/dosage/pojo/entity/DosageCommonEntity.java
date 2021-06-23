package com.evision.dosage.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author DingZhanYang
 * @date 2020/2/14 11:12
 */
@Getter
@Setter
public class DosageCommonEntity {
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updateTime;
    /**
     * 创建人ID
     */
    private int userId;
    /**
     * 启用状态
     * 1、启用，0、禁用
     */
    private int disabled;
    /**
     * 删除状态
     * 1、已删除，0、未删除
     */
    private int deleted;
}
