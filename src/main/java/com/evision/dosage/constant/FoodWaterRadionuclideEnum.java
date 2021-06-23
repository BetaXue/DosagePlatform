package com.evision.dosage.constant;

import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Yu Xiao
 * @Date: 2020/2/17 19:25
 */
@Getter
@AllArgsConstructor
public enum FoodWaterRadionuclideEnum {
    SERIAL_NUMBER("序号", null, "serial_number", "serialNumber","130"),
    SAMPLE_NUMBER("样品编号", 0, "sample_number", "sampleNumber","130"),
    PO210("Po-210", 1, "po210", "po210","130"),
    PO210_UNCERTAINTY("不确定度", 0, "po210_uncertainty", "po210Uncertainty","130"),
    PB210("Pb-210", 1, "pb210", "pb210","130"),
    PB210_UNCERTAINTY("不确定度", 0, "pb210_uncertainty", "pb210Uncertainty","130"),
    RA226("Ra-226", 1, "ra226", "ra226","130"),
    RA226_UNCERTAINTY("不确定度", 0, "ra226_uncertainty", "ra226Uncertainty","130"),
    RA228("Ra-228", 1, "ra228", "ra228","130"),
    RA228_UNCERTAINTY("不确定度", 0, "ra228_uncertainty", "ra228Uncertainty","130"),
    UPDATE_TIME("修改日期",null,"create_time","createTime","180"),
    ;
    private String fieldName;
    /**
     * 是否允许排序 1 允许 0 不允许
     */
    private Integer supportSort;
    private String dbFieldName;
    private String entityName;
    private String width;


    /**
     * 获取Header数据集合
     *
     * @return Header
     */
    public static List<DosageHeader> convertHeaderEntity() {
        List<DosageHeader> headerEntities = new ArrayList<>();
        for (FoodWaterRadionuclideEnum value : FoodWaterRadionuclideEnum.values()) {
            Integer supportSort = value.getSupportSort();
            if (supportSort != null) {
                DosageHeader headerEntity = new DosageHeader();
                headerEntity.setName(value.getFieldName());
                headerEntity.setCode(value.getEntityName());
                headerEntity.setSupportSort(supportSort);
                headerEntities.add(headerEntity);
            }
        }
        return headerEntities;
    }

    /**
     * 得到数据库字段
     *
     * @param entityName
     * @return dbFieldName
     */
    public static String getDbFieldName(String entityName) {
        for (FoodWaterRadionuclideEnum value : FoodWaterRadionuclideEnum.values()) {
            if (entityName.equals(value.getEntityName())) {
                return value.getDbFieldName();
            }
        }
        return null;
    }

    /**
     * 数据库维护页面表头
     *
     * @return
     */
    public static List<DosageDbHeader> convertDbHeaderEntity() {
        List<DosageDbHeader> headerEntities = new ArrayList<>();
        for (FoodWaterRadionuclideEnum value : FoodWaterRadionuclideEnum.values()) {
            String entityName = value.getEntityName();
            if(!"serialNumber".equals(entityName)){
                DosageDbHeader headerEntity = new DosageDbHeader();
                headerEntity.setProp(entityName);
                headerEntity.setWidth(value.getWidth());
                headerEntity.setLabel(value.getFieldName());
                Integer supportSort = value.getSupportSort();
                if (supportSort != null && supportSort == 1) {
                    headerEntity.setSortable("custom");
                }
                headerEntities.add(headerEntity);
            }
        }
        return headerEntities;
    }
}
