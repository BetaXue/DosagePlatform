package com.evision.dosage.pojo.entity.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author DingZhanYang
 * @date 2020/2/17 17:08
 */
@Getter
@Setter
public class UserCommonEntity {
    /**
     * 有效的
     */
    private int valid;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
