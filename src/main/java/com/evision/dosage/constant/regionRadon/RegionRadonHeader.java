package com.evision.dosage.constant.regionRadon;

import com.evision.dosage.pojo.model.DosageDbHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 氡、钍射气及子体实体-各地区氡浓度
 *
 * @author: AubreyXue
 * @date: 2020-02-29 19:33
 **/
@Getter
@AllArgsConstructor
public enum RegionRadonHeader {

    REGION("地区", "region"),
    AVERAGE_RADON("平均氡浓度", "averageRadon"),
    MISTAKE("误差", "mistake"),
    UPDATE_TIME("最后修改日期", "updateTime");

    /**
     * 中文名称
     */
    private String label;

    /**
     * 字段值
     */
    private String prop;

    public static List<DosageDbHeader> getDosageDbHeader() {
        List<DosageDbHeader> headerEntities = new ArrayList<>();
        for (RegionRadonHeader value : RegionRadonHeader.values()) {
            DosageDbHeader headerEntity = new DosageDbHeader();
            headerEntity.setLabel(value.getLabel());
            headerEntity.setProp(value.getProp());
            headerEntities.add(headerEntity);
        }
        return headerEntities;
    }
}
