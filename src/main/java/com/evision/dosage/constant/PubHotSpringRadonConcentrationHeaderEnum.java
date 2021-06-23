package com.evision.dosage.constant;

import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/29 22:24
 */
@Getter
@AllArgsConstructor
public enum PubHotSpringRadonConcentrationHeaderEnum {
    MEASURE_RESULT_MIN("温泉地点", "hotSpring", 0),
    MEASURE_RESULT_AVERAGE("前3次平均氡浓度", "hotSpringRadonConcentration", 0),
    UPDATE_TIME("修改时间", "updateTime", 0);
    /**
     * 中文名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 是否允许排序 1 允许 0 不允许
     */
    private Integer supportSort;


    /**
     * 获取Header数据集合
     *
     * @return Header
     */
    public static List<DosageHeader> convertHeaderEntity() {
        List<DosageHeader> headerEntities = new ArrayList<>();
        for (PubHotSpringRadonConcentrationHeaderEnum value : PubHotSpringRadonConcentrationHeaderEnum.values()) {
            DosageHeader headerEntity = new DosageHeader();
            headerEntity.setName(value.getName());
            headerEntity.setCode(value.getCode());
            headerEntity.setSupportSort(value.getSupportSort());
            headerEntities.add(headerEntity);
        }
        return headerEntities;
    }

    /**
     * 获取Header数据集合
     *
     * @return Header
     */
    public static List<DosageDbHeader> convertDbHeaderEntity() {
        List<DosageDbHeader> dosageDbHeaders = new ArrayList<>();
        for (PubHotSpringRadonConcentrationHeaderEnum value : PubHotSpringRadonConcentrationHeaderEnum.values()) {
            DosageDbHeader dosageDbHeader = new DosageDbHeader();
            dosageDbHeader.setLabel(value.getName());
            dosageDbHeader.setProp(value.getCode());
            dosageDbHeaders.add(dosageDbHeader);
        }
        return dosageDbHeaders;
    }
}
