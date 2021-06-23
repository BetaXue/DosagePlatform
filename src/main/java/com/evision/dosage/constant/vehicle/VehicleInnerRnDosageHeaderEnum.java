package com.evision.dosage.constant.vehicle;

import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/21 20:47
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum VehicleInnerRnDosageHeaderEnum {
    DATA_CATEGORY("数据类别", "dataCategory", 0, "245"),
    LOCATION("地铁位置", "location", 0, "130"),
    ROUTE("线路名称", "route", 0, "130"),
    MONITOR_NUMBER("监测数量", "monitorNumber", 0, "130"),
    MEASURE_RESULT_MAX("最大值", "measureResultMax", 0, "130"),
    MEASURE_RESULT_MIN("最小值", "measureResultMin", 0, "130"),
    MEASURE_RESULT_AVERAGE("均值", "measureResultAverage", 0, "130"),
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
        for (VehicleInnerRnDosageHeaderEnum value : VehicleInnerRnDosageHeaderEnum.values()) {
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
        for (VehicleInnerRnDosageHeaderEnum value : VehicleInnerRnDosageHeaderEnum.values()) {
            DosageDbHeader dosageDbHeader = new DosageDbHeader();
            dosageDbHeader.setLabel(value.getName());
            dosageDbHeader.setWidth(value.getWidth());
            dosageDbHeader.setProp(value.getCode());
            if (value == MEASURE_RESULT_MAX || value == MEASURE_RESULT_MIN || value == MEASURE_RESULT_AVERAGE) {
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


    public static void main(String[] args) {
        List<DosageDbHeader> dosageDbHeaders = convertDbHeaderEntity();
        for (DosageDbHeader dosageDbHeader : dosageDbHeaders) {
            System.out.println(dosageDbHeader.getLabel());
        }
    }
}
