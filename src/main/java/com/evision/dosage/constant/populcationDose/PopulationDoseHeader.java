package com.evision.dosage.constant.populcationDose;

import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-21 15:41
 */
@Getter
@AllArgsConstructor
public enum PopulationDoseHeader {

    DOSE_SOURCE("剂量来源", "doseSource", 0),
    DOSE_CATEGORY("类别", "category", 0),
    DOSE_SOURCE_SUBCATEGORY("次类别", "doseSourceSubcategory", 0),
    ANNUAL_EFFECTIVE_DOSE("年有效剂量（μSv/a）", "annualEffectiveDose", 1),
    SUB_ITEM_RATIO("分项比例（%）", "subItemRatio", 1),
    TOTAL_DOSE_RATIO("总剂量比例（%）", "totalDoseRatio", 1),
    UPDATE_TIME("最后修改日期", "updateTime", 0);

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
        for (PopulationDoseHeader value : PopulationDoseHeader.values()) {
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
        for (PopulationDoseHeader value : PopulationDoseHeader.values()) {
            DosageDbHeader headerEntity = new DosageDbHeader();
            headerEntity.setLabel(value.getName());
            headerEntity.setProp(value.getCode());
            dosageDbHeaders.add(headerEntity);
        }
        return dosageDbHeaders;
    }
}
