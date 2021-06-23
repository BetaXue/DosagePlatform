package com.evision.dosage.constant;

import com.evision.dosage.pojo.model.DosageDbHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 地铁氡浓度
 *
 * @Author: Yu Xiao
 * @Date: 2020/2/17 19:25
 */
@Getter
@AllArgsConstructor
public enum SubwayRadonEnum {
    SUBWAY_NAME("站点", 0, "subwayName"),
    UNCERTAINTY("不确定度", 1, "uncertainty"),
    AVG_RADON_CONCENTRATION("平均氡浓度(Bq/m3)", 1, "avgRadonConcentration"),
    UPDATE_TIME("修改时间", 1, "updateTime");


    private String fieldName;
    /**
     * 是否允许排序 1 允许 0 不允许
     */
    private Integer supportSort;
    private String entityName;


    /**
     * 数据库维护页面表头
     *
     * @return
     */
    public static List<DosageDbHeader> convertDbHeaderEntity() {
        List<DosageDbHeader> headerEntities = new ArrayList<>();
        for (SubwayRadonEnum value : SubwayRadonEnum.values()) {
            String entityName = value.getEntityName();
            DosageDbHeader headerEntity = new DosageDbHeader();
            headerEntity.setProp(entityName);
            headerEntity.setLabel(value.getFieldName());
            headerEntities.add(headerEntity);
        }
        return headerEntities;
    }
}
