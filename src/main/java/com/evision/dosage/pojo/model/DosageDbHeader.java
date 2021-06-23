package com.evision.dosage.pojo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/29 17:02
 */
@Getter
@Setter
@NoArgsConstructor
public class DosageDbHeader {
    /**
     * 表头对应数据库字段名
     */
    private String prop;
    /**
     * 前端显示表头名
     */
    private String label;

    /**
     * 排序表格
     */
    private String sortable;

    private String width;
    /**
     * 子单元格
     */
    private List<DosageDbHeader> children;
}
