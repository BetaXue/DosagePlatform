package com.evision.dosage.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 有效状态枚举类
 *
 * @author: kangwenxuan
 * @date: 2020-02-25 14:26
 **/
@Getter
@AllArgsConstructor
public enum ValidConstant {
    /**
     * 有效
     */
    VALID(1),
    /**
     * 无效
     */
    DIS_VALID(0);

    private Integer value;
}
