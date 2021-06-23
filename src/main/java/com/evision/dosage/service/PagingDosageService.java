package com.evision.dosage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author DingZhanYang
 * @date 2020/2/23 8:16
 */
public interface PagingDosageService<T> extends DosageService<T> {
    IPage<T> getDataList(String queryName, Integer pageSize, Integer pageNum, String sortName, Integer isAsc, Integer incloudDel) throws Exception;
}
