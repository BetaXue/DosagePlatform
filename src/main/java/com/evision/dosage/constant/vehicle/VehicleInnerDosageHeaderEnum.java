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
public enum VehicleInnerDosageHeaderEnum {
    DATA_CATEGORY("数据类别", "dataCategory", 0, "130"),
    ROAD_TYPE("公路类型", "roadType", 0, "130"),
    VEHICLE_TYPE("车辆类型", "vehicleType", 0, "130"),
    MONITOR_NUMBER("车厢监测数量", "monitorNumber", 0, "130"),
    FRONT_RESULT_RANGE("车厢前侧", "frontResultRange", 0, "130"),
    MIDDLE_RESULT_RANGE("车厢中部", "middleResultRange", 0, "130"),
    BACK_RESULT_RANGE("车厢后侧", "backResultRange", 0, "130"),
    ENTIRE_RESULT_RANGE("车厢范围", "entiretyResultRange", 0, "130"),
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
        for (VehicleInnerDosageHeaderEnum value : VehicleInnerDosageHeaderEnum.values()) {
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
        for (VehicleInnerDosageHeaderEnum value : VehicleInnerDosageHeaderEnum.values()) {
            DosageDbHeader dosageDbHeader = new DosageDbHeader();
            dosageDbHeader.setLabel(value.getName());
            dosageDbHeader.setWidth(value.getWidth());
            if (value.getSupportSort() == 1) {
                dosageDbHeader.setSortable("custom");
            }
            dosageDbHeader.setProp(value.getCode());
            if (value == FRONT_RESULT_RANGE || value == MIDDLE_RESULT_RANGE || value == BACK_RESULT_RANGE || value == ENTIRE_RESULT_RANGE) {
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
            DosageDbHeader oldDosageDbHeader = dosageDbHeaders.get(i);
            if ("修改时间".equals(oldDosageDbHeader.getLabel())) {
                dosageDbHeaders.remove(i);
                dosageDbHeader = oldDosageDbHeader;
            }
        }
        dosageDbHeaders.add(dosageDbHeader);
        return dosageDbHeaders;
    }
}
