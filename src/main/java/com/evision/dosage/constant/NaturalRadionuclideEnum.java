package com.evision.dosage.constant;

import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: kangwenxuan
 * @Date: 2020/2/21 19:25
 */
@Getter
@AllArgsConstructor
public enum NaturalRadionuclideEnum {
    NAME("序号", 0, "name", "name", "120"),
    INJECT_PO210("Po-210（Bq/a）", 1, "inject_po210", "injectPo210", "120"),
    INJECT_PB210("Pb-210（Bq/a）", 1, "inject_pb210", "injectPb210", "120"),
    INJECT_RA226("Ra-226（Bq/a）", 1, "inject_ra226", "injectRa226", "120"),
    INJECT_RA228("Ra-228（Bq/a）", 1, "inject_ra228", "injectRa228", "120"),
    PO210("Po-210（nSv/a）", 1, "po210", "po210", "120"),
    PB210("Pb-210（nSv/a）", 1, "pb210", "pb210", "120"),
    RA226("Ra-226（nSv/a）", 1, "ra226", "ra226", "120"),
    RA228("Ra-228（nSv/a）", 1, "ra228", "ra228", "120"),
    SUM("合计", 1, "sum", "sum", "120"),
    PROPORTION("占比", 1, "proportion", "proportion", "120"),
    UPDATE_TIME("修改时间", 0, "updateTime", "updateTime", "180");


    private String fieldName;
    private Integer supportSort;
    private String dbFieldName;
    private String entityName;
    private String width;

    /**
     * 得到数据库字段
     *
     * @param entityName
     * @return dbFieldName
     */
    public static String getDbFieldName(String entityName) {
        for (NaturalRadionuclideEnum value : NaturalRadionuclideEnum.values()) {
            if (entityName.equals(value.getEntityName())) {
                return value.getDbFieldName();
            }
        }
        return null;
    }

    /**
     * 获取Header数据集合
     *
     * @return Header
     */
    public static List<DosageHeader> convertHeaderEntity() {
        List<DosageHeader> headerEntities = new ArrayList<>();
        for (NaturalRadionuclideEnum value : NaturalRadionuclideEnum.values()) {
            DosageHeader headerEntity = new DosageHeader();
            headerEntity.setName(value.getFieldName());
            headerEntity.setCode(value.getEntityName());
            headerEntity.setSupportSort(value.getSupportSort());
            headerEntities.add(headerEntity);
        }
        return headerEntities;
    }

    public static List<DosageDbHeader> getDbHeader() {
        List<DosageDbHeader> headerEntities = new ArrayList<>();
        for (NaturalRadionuclideEnum value : NaturalRadionuclideEnum.values()) {
            DosageDbHeader headerEntity = new DosageDbHeader();
            headerEntity.setLabel(value.getFieldName());
            headerEntity.setProp(value.getEntityName());
            headerEntity.setWidth(value.getWidth());
            if (value.getSupportSort() == 1) {
                headerEntity.setSortable("custom");
            }
            headerEntities.add(headerEntity);
        }
        return headerEntities;
    }

}
