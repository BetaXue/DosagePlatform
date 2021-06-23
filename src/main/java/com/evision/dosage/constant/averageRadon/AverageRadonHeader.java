package com.evision.dosage.constant.averageRadon;

import com.evision.dosage.constant.regionGas.RegionGasHeader;
import com.evision.dosage.pojo.model.DosageDbHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 氡、钍射气及子体-平均氡浓度表头
 *
 * @author: AubreyXue
 * @date: 2020-02-29 23:05
 **/
@Getter
@AllArgsConstructor
public enum AverageRadonHeader {

    REGION("地区", "region"),
    AVERAGE_RADON("平均氡浓度(Bq/m3)", "averageRadon"),
    UPDATE_TIME("最后修改日期", "updateTime");


    private String label;

    private String prop;

    public static List<DosageDbHeader> getDosageDbHeader() {
        List<DosageDbHeader> headerEntities = new ArrayList<>();
        for (AverageRadonHeader value : AverageRadonHeader.values()) {
            DosageDbHeader headerEntity = new DosageDbHeader();
            headerEntity.setLabel(value.getLabel());
            headerEntity.setProp(value.getProp());
            headerEntities.add(headerEntity);
        }
        return headerEntities;
    }


}
