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
public enum GammaRayEnum {
    PLACE_NAME("点位名称", 0, "place_name", "placeName"),
    MEASURE_RESULT("测量结果（nSv/h）", null, null, null),
    MAX_VALUE("最大值", 1, "max_value", "maxValue"),
    MIN_VALUE("最小值", 1, "min_value", "minValue"),
    AVERAGE_VALUE("均值", 1, "average_value", "averageValue"),
    SURFACE_TYPE("地表类型", 0, "surface_type", "surfaceType"),
    GRID_NUMBER("网格点", 1, "grid_number", "gridNumber"),
    UPDATE_TIME("修改时间", 0, "create_time", "createTime"),
    ;
    private String fieldName;
    /**
     * 是否允许排序 1 允许 0 不允许
     */
    private Integer supportSort;
    private String dbFieldName;
    private String entityName;


    /**
     * 获取Header数据集合(暂时用不到)
     *
     * @return Header
     */
    public static List<DosageHeader> convertHeaderEntity() {
        List<DosageHeader> headerEntities = new ArrayList<>();
        for (GammaRayEnum value : GammaRayEnum.values()) {
            DosageHeader headerEntity = new DosageHeader();
            headerEntity.setName(value.getFieldName());
            headerEntity.setCode(value.getEntityName());
            headerEntity.setSupportSort(value.getSupportSort());
            headerEntities.add(headerEntity);
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
        for (GammaRayEnum value : GammaRayEnum.values()) {
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
        List<DosageDbHeader> measureResult = new ArrayList<>();
        for (GammaRayEnum value : GammaRayEnum.values()) {
            String entityName = value.getEntityName();
            Integer supportSort = value.getSupportSort();
            if ("maxValue".equals(entityName) || "minValue".equals(entityName)
                    || "averageValue".equals(entityName)) {
                DosageDbHeader headerEntity = new DosageDbHeader();
                headerEntity.setProp(entityName);
                headerEntity.setLabel(value.getFieldName());
                if (supportSort != null && supportSort == 1) {
                    headerEntity.setSortable("custom");
                }
                measureResult.add(headerEntity);
            } else {
                DosageDbHeader headerEntity = new DosageDbHeader();
                headerEntity.setProp(entityName);
                headerEntity.setLabel(value.getFieldName());
                if (supportSort != null && supportSort == 1) {
                    headerEntity.setSortable("custom");
                }
                if (entityName == null) {
                    headerEntity.setChildren(measureResult);
                }
                headerEntities.add(headerEntity);
            }
        }
        DosageDbHeader dosageDbHeader = null;
        for (int i = headerEntities.size() - 1; i >= 0; i--) {
            DosageDbHeader oldDosageDbHeader = headerEntities.get(i);
            if ("修改时间".equals(oldDosageDbHeader.getLabel())) {
                headerEntities.remove(i);
                dosageDbHeader = oldDosageDbHeader;
            }
        }
        headerEntities.add(dosageDbHeader);
        return headerEntities;
    }
}
