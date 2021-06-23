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
public enum MetroTransferStationDosageHeaderEnum {
    //    SERIAL_NUMBER("序号", "serialNumber", 0),
    DATA_CATEGORY("数据类别", "dataCategory", 0),
    STATION("地铁换乘站", "station", 0),
    MEASURE_RESULT("累积剂量测量结果（nSv/h）", null, 1),
    FIRST_QUARTER_MEASURE_RESULT("第一季度", "firstQuarterMeasureResult", 1),
    SECOND_QUARTER_MEASURE_RESULT("第二季度", "secondQuarterMeasureResult", 1),
    THIRD_QUARTER_MEASURE_RESULT("第三季度", "thirdQuarterMeasureResult", 1),
    FOURTH_QUARTER_MEASURE_RESULT("第四季度", "fourthQuarterMeasureResult", 1),
    UPDATE_TIME("修改时间", "updateTime", 0);
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


    /**
     * 获取Header数据集合
     *
     * @return Header
     */
    public static List<DosageHeader> convertHeaderEntity() {
        List<DosageHeader> headerEntities = new ArrayList<>();
        for (MetroTransferStationDosageHeaderEnum value : MetroTransferStationDosageHeaderEnum.values()) {
            DosageHeader headerEntity = new DosageHeader();
            headerEntity.setName(value.getName());
            headerEntity.setCode(value.getCode());
            headerEntity.setSupportSort(value.getSupportSort());
            headerEntities.add(headerEntity);
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
        List<DosageDbHeader> measureResult = new ArrayList<>();
        for (MetroTransferStationDosageHeaderEnum value : MetroTransferStationDosageHeaderEnum.values()) {
            String code = value.getCode();
            if ("firstQuarterMeasureResult".equals(code) || "secondQuarterMeasureResult".equals(code)
                    || "thirdQuarterMeasureResult".equals(code) || "fourthQuarterMeasureResult".equals(code)) {
                DosageDbHeader headerEntity = new DosageDbHeader();
                headerEntity.setProp(code);
                if (value.getSupportSort() == 1) {
                    headerEntity.setSortable("custom");
                }
                headerEntity.setLabel(value.getName());
                measureResult.add(headerEntity);
            } else {
                DosageDbHeader headerEntity = new DosageDbHeader();
                headerEntity.setProp(code);
                if (value.getSupportSort() == 1) {
                    headerEntity.setSortable("custom");
                }
                headerEntity.setLabel(value.getName());
                if (code == null) {
                    headerEntity.setChildren(measureResult);
                }
                headerEntities.add(headerEntity);
            }
        }
        DosageDbHeader dosageDbHeader = null;
        for (int i = headerEntities.size() - 1; i >= 0; i--) {
            DosageDbHeader dosageDbHeaders = headerEntities.get(i);
            if ("修改时间".equals(dosageDbHeaders.getLabel())) {
                headerEntities.remove(i);
                dosageDbHeader = dosageDbHeaders;
            }
        }
        headerEntities.add(dosageDbHeader);
        return headerEntities;
    }
}
