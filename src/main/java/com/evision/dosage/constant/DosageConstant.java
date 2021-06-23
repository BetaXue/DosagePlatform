package com.evision.dosage.constant;

import com.evision.dosage.pojo.model.BaseController;

import java.util.UUID;

/**
 * 全局变量
 *
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-18 15:10
 */
public class DosageConstant {
    /**
     * 排序规则：降序
     */
    public static final String ORDER_DESC = "desc";
    /**
     * 排序规则：升序
     */
    public static final String ORDER_ASC = "asc";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASS = "1QAZ2wsx!@";

    /**
     * {@link BaseController}
     * getDataTable 中 HashMap 默认的初始化容量
     */
    public static final int DATA_MAP_INITIAL_CAPACITY = 4;
    /**
     * 宇宙射线网格规格：N * N
     */
    public static final int[] UVNIVERSE_RAY_CELL_COUNT = {43, 32};
    /**
     * 伽马射线网格规格：N * N
     */
    public static final int[] GAMMA_RAY_CELL_COUNT = {34, 35};


    public static final String ADMIN_TOKEN = "ec4e46c0bf4f40e1a3714c9ab680a85c";
}
