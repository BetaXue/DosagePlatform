package com.evision.dosage.constant.regionGas;

import com.evision.dosage.pojo.model.DosageDbHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 氡、钍射气及子体实体-各地区、类型氡浓度 表头
 *
 * @author: AubreyXue
 * @date: 2020-02-29 19:09
 **/
@Getter
@AllArgsConstructor
public enum RegionGasHeader {

    REGION("站点", "region"),

    TYPE("类型", "type"),

    MEAN("mean±sd", "mean"),

    ACQUISITION_RATE("数据获取率", "acquisitionRate"),

    CHANGE_RANGE("变化范围", "changeRange"),

    UPDATE_TIME("最后修改日期", "updateTime");

    private String label;

    private String prop;


    public static List<DosageDbHeader> getDosageDbHeader() {
        List<DosageDbHeader> headerEntities = new ArrayList<>();
        for (RegionGasHeader value : RegionGasHeader.values()) {
            DosageDbHeader headerEntity = new DosageDbHeader();
            headerEntity.setLabel(value.getLabel());
            headerEntity.setProp(value.getProp());
            headerEntities.add(headerEntity);
        }
        return headerEntities;
    }

}
