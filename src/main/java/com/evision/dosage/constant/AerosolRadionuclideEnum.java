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
public enum AerosolRadionuclideEnum {
    SERIAL_NUMBER("序号", null, "serial_number", "serialNumber","130"),
    SAMPLE_NUMBER("样品编号", 0, "sample_number", "sampleNumber","130"),
    RA226("Ra-226", 1, "ra226", "ra226","130"),
    RA226_UNCERTAINTY("不确定度", 0, "ra226_uncertainty", "ra226Uncertainty","130"),
    TH232("Th-232", 1, "th232", "th232","130"),
    TH232_UNCERTAINTY("不确定度", 0, "th232_uncertainty", "th232Uncertainty","130"),
    K40("K-40", 1, "k40", "k40","130"),
    K40_UNCERTAINTY("不确定度", 0, "k40_uncertainty", "k40Uncertainty","130"),
    U238("U-238", 1, "u238", "u238","130"),
    U238_UNCERTAINTY("不确定度", 0, "u238_uncertainty", "u238Uncertainty","130"),
    CS137("Cs-137", 1, "cs137", "cs137","130"),
    CS137_UNCERTAINTY("不确定度", 0, "cs137_uncertainty", "cs137Uncertainty","130"),
    UPDATE_TIME("最后修改日期", null, "create_time", "createTime","180"),
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
        for (AerosolRadionuclideEnum value : AerosolRadionuclideEnum.values()) {
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
     * 数据库维护页面表头
     *
     * @return
     */
    public static List<DosageDbHeader> convertDbHeaderEntity() {
        List<DosageDbHeader> headerEntities = new ArrayList<>();
        for (AerosolRadionuclideEnum value : AerosolRadionuclideEnum.values()) {
            String entityName = value.getEntityName();
            if (!"serialNumber".equals(entityName)) {
                DosageDbHeader headerEntity = new DosageDbHeader();
                headerEntity.setProp(entityName);
                headerEntity.setLabel(value.getFieldName());
                headerEntity.setWidth(value.getWidth());
                Integer supportSort = value.getSupportSort();
                if (supportSort != null && supportSort == 1) {
                    headerEntity.setSortable("custom");
                }
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
        for (AerosolRadionuclideEnum value : AerosolRadionuclideEnum.values()) {
            if (entityName.equals(value.getEntityName())) {
                return value.getDbFieldName();
            }
        }
        return null;
    }

}
