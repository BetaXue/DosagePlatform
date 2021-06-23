package com.evision.dosage.pojo.model;

import lombok.Data;

/**
 * 表头实体
 *
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-21 15:38
 */
@Data
public class DosageHeader {

    /**
     * 表头中文名称
     */
    private String name;

    /**
     * 表头code值
     */
    private String code;

    /**
     * 是否支持排序 1支持 0不支持
     */
    private Integer supportSort;
}
