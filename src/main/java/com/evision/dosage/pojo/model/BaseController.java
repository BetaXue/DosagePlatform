package com.evision.dosage.pojo.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.evision.dosage.constant.DosageConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-18 15:19
 */
public class BaseController {

    protected Map<String, Object> getDataTable(IPage<?> pageInfo) {
        return getDataTable(pageInfo, DosageConstant.DATA_MAP_INITIAL_CAPACITY);
    }

    protected Map<String, Object> getDataTable(IPage<?> pageInfo, int dataMapInitialCapacity) {
        Map<String, Object> data = new HashMap<>(dataMapInitialCapacity);
        data.put("rows", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());
        return data;
    }
}
