package com.evision.dosage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.GammaRay;

import java.util.List;


/**
 * @Author: Yu Xiao
 * @Date: 2020/2/18 17:07
 */
public interface GammaRayService extends IService<GammaRay>,PagingDosageService<GammaRay> {
    /**
     * 根据网格编码查询网格数据
     * @param gridNumber 网格编码
     * @return
     */
    List<GammaRay> findByGridNumber(String gridNumber);

    /**
     * 不考虑是否加密问题
     * @return
     */
    List<GammaRay> findAllGroupByGridNumber();
}
