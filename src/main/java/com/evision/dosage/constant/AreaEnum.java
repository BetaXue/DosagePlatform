package com.evision.dosage.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 区代码
 * @Author: Yu Xiao
 * @Date: 2020/2/20 "1"7:34
 */
@Getter
@AllArgsConstructor
public enum AreaEnum {
    东城("1","东城"),
    西城("2","西城"),
    朝阳("3","朝阳"),
    海淀("4","海淀"),
    丰台("5","丰台"),
    石景山("6","石景山"),
    延庆("7","延庆"),
    通州("8","通州"),
    顺义("9","顺义"),
    平谷("10","平谷"),
    密云("11","密云"),
    门头沟("12","门头沟"),
    怀柔("13","怀柔"),
    房山("14","房山"),
    大兴("15","大兴"),
    昌平("16","昌平"),
    ;
    private String areaCode;
    private String areaName;
}
