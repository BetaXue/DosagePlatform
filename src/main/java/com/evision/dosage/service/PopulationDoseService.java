package com.evision.dosage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.PopulationDoseEntity;

import java.util.List;

/**
 * 全民剂量首页业务层
 *
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-19 14:46
 */
public interface PopulationDoseService extends IService<PopulationDoseEntity>, DosageService<PopulationDoseEntity> {
    
}
