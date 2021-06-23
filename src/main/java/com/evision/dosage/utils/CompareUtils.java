package com.evision.dosage.utils;

import com.evision.dosage.exception.DosageException;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/21 10:15
 */
public class CompareUtils {
    /**
     * 判断布尔值列表元素是否全为true
     *
     * @param booleans 布尔值列表
     * @return 是否全为true
     */
    public static boolean isAllTrue(List<Boolean> booleans) {
        for (boolean bl : booleans) {
            if (!bl) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断布尔值列表元素是否至少有一个为true
     *
     * @param booleans 布尔值列表
     * @return 是否至少有一个为true
     */
    public static boolean isAnyTrue(List<Boolean> booleans) {
        for (boolean bl : booleans) {
            if (bl) {
                return true;
            }
        }
        return false;
    }

    public static boolean equals(String str1, String str2){
        if (StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)){
            return true;
        }
        if (StringUtils.isEmpty(str1) || StringUtils.isEmpty(str2)){
            return false;
        }
        return str1.equals(str2);
    }
    public static boolean equals(String str1, String str2, boolean primaryKey) throws Exception{
        if (StringUtils.isEmpty(str1) || StringUtils.isEmpty(str2)){
            throw new DosageException("主键不能为空值：" + str1);
        }
        return str1.equals(str2);
    }

    public static boolean equals(BigDecimal decimal1, BigDecimal decimal2){
        if (decimal1 == null && decimal2 == null){
            return true;
        }
        if (decimal1 == null || decimal2 == null){
            return false;
        }
        return decimal1.equals(decimal2);
    }

    public static boolean equals(Integer int1, Integer int2){
        if (int1 == null && int2 == null){
            return true;
        }
        if (int1 == null || int2 == null){
            return false;
        }
        return int1.equals(int2);
    }
}
