package com.evision.dosage.constant.vehicle;

import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/21 20:47
 */
@Getter
@AllArgsConstructor
public enum VehicleDosageHeaderEnum {
    DATA_CATEGORY("数据类别", "dataCategory", 1, "130"),
    LOCATION("位置", "location", 1, "130"),
    ROAD_NAME("名称", "roadName", 1, "130"),
    ROUTE("线路", "route", 1, "130"),
    DRIVING_DATE("行驶状态", "drivingState", 1, "130"),
    MONITOR_NUMBER("监测数量", "monitorNumber", 1, "130"),
    MEASURE_RESULT_MAX_RANGE("最大值范围", "measureResultMaxRange", 1, "130"),
    MEASURE_RESULT_MIN_RANGE("最小值范围", "measureResultMinRange", 1, "130"),
    MEASURE_RESULT_AVERAGE_RANGE("均值范围", "measureResultAverageRange", 1, "130"),
    UPDATE_TIME("修改时间", "updateTime", 0, "180");
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

    private String width;


    /**
     * 获取Header数据集合
     *
     * @return Header
     */
    public static List<DosageHeader> convertHeaderEntity() {
        List<DosageHeader> headerEntities = new ArrayList<>();
        for (VehicleDosageHeaderEnum value : VehicleDosageHeaderEnum.values()) {
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
        List<DosageDbHeader> messageResults = new ArrayList<>();
        for (VehicleDosageHeaderEnum value : VehicleDosageHeaderEnum.values()) {
            DosageDbHeader dosageDbHeader = new DosageDbHeader();
            dosageDbHeader.setLabel(value.getName());
            dosageDbHeader.setWidth(value.getWidth());
            if (value.getSupportSort() == 1) {
                dosageDbHeader.setSortable("custom");
            }
            dosageDbHeader.setProp(value.getCode());
            if (value == MEASURE_RESULT_MAX_RANGE || value == MEASURE_RESULT_MIN_RANGE || value == MEASURE_RESULT_AVERAGE_RANGE) {
                messageResults.add(dosageDbHeader);
            } else {
                dosageDbHeaders.add(dosageDbHeader);
            }
        }
        DosageDbHeader messageResult = new DosageDbHeader();
        messageResult.setLabel("测量结果");
        messageResult.setChildren(messageResults);
        dosageDbHeaders.add(messageResult);
        DosageDbHeader dosageDbHeader = null;
        for (int i = dosageDbHeaders.size() - 1; i >= 0; i--) {
            DosageDbHeader dosageDbHeaderes = dosageDbHeaders.get(i);
            if ("修改时间".equals(dosageDbHeaderes.getLabel())) {
                dosageDbHeaders.remove(i);
                dosageDbHeader = dosageDbHeaderes;
            }
        }
        dosageDbHeaders.add(dosageDbHeader);
        return dosageDbHeaders;
    }
}
