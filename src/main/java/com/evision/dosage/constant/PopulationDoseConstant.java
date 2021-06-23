package com.evision.dosage.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-19 23:57
 */
@Getter
@AllArgsConstructor
public enum PopulationDoseConstant {

    UNKNOWN(10000, "未知"),
    NATURAL_EXPOSURE_A(1, "天然辐射-宇宙射线-电离成份"),
    NATURAL_EXPOSURE_B(2, "天然辐射-宇宙射线-中子成份"),
    NATURAL_EXPOSURE_C(3, "天然辐射-宇宙射线-宇生放射性核素"),
    NATURAL_EXPOSURE_D(4, "天然辐射-陆地伽马辐射"),
    NATURAL_EXPOSURE_E(5, "天然辐射-氡、钍射气-氡及其短寿命子体"),
    NATURAL_EXPOSURE_F(6, "天然辐射-氡、钍射气-钍射气及其短寿命子体"),
    NATURAL_EXPOSURE_G(7, "天然辐射-40K"),
    NATURAL_EXPOSURE_H(8, "天然辐射-原生放射性核素-吸入途径"),
    NATURAL_EXPOSURE_I(9, "天然辐射-原生放射性核素-食入途径及饮水"),
    NATURAL_EXPOSURE_J(10, "天然辐射-天然辐射 合计"),
    MEDICAL_EXPOSURE_A(11, "医疗照射-普通X射线诊断"),
    MEDICAL_EXPOSURE_B(12, "医疗照射-牙科摄影"),
    MEDICAL_EXPOSURE_C(13, "医疗照射-乳腺摄影"),
    MEDICAL_EXPOSURE_D(14, "医疗照射-计算机断层摄影（CT）"),
    MEDICAL_EXPOSURE_E(15, "医疗照射-介入放射学"),
    MEDICAL_EXPOSURE_F(16, "医疗照射-医疗照射 合计"),
    OCCUPATIONAL_EXPOSURE_A(17, "职业照射-医学应用"),
    OCCUPATIONAL_EXPOSURE_B(18, "职业照射-其它"),
    OCCUPATIONAL_EXPOSURE_C(19, "职业照射-工业应用"),
    OCCUPATIONAL_EXPOSURE_D(20, "职业照射-职业照射 合计"),
    PUBLIC_EXPOSURE_A(21, "公众照射-抽烟"),
    PUBLIC_EXPOSURE_B(22, "公众照射-交通方式-民用航空"),
    PUBLIC_EXPOSURE_C(23, "公众照射-交通方式-轨道交通、公交车及小汽车"),
    PUBLIC_EXPOSURE_D(24, "公众照射-温泉"),
    PUBLIC_EXPOSURE_E(25, "公众照射-化石燃料燃烧"),
    PUBLIC_EXPOSURE_F(26, "公众照射-使用天然气做饭"),
    PUBLIC_EXPOSURE_G(27, "公众照射-核燃料循环和核电站运行"),
    PUBLIC_EXPOSURE_H(28, "公众照射-切尔诺贝利核事故"),
    PUBLIC_EXPOSURE_I(29, "公众照射-福岛核事故"),
    PUBLIC_EXPOSURE_J(30, "公众照射-核能与新能源研究院"),
    PUBLIC_EXPOSURE_K(31,"公众照射-中国原子能科学研究院"),
    PUBLIC_EXPOSURE_L(32, "公众照射-香蕉"),
    PUBLIC_EXPOSURE_M(33, "公众照射-公众照射 合计"),
    TOTAL(34, "合计");

    Integer index;

    String name;

    /**
     * 根据Key得到枚举的Value
     * Lambda表达式，比较判断（JDK 1.8）
     *
     * @param key
     * @return
     */
    public static String getIndexValue(Integer key) {
        return Arrays.asList(PopulationDoseConstant.values()).stream()
                .filter(populationDoseConstant -> populationDoseConstant.getIndex().equals(key))
                .findFirst().orElse(PopulationDoseConstant.UNKNOWN).getName();
    }
}