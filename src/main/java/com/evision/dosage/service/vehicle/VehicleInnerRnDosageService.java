package com.evision.dosage.service.vehicle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.vehicle.VehicleInnerRnDosageEntity;
import com.evision.dosage.service.PagingDosageService;

/**
 * @author DingZhanYang
 * @date 2020/2/21 16:05
 */
public interface VehicleInnerRnDosageService extends IService<VehicleInnerRnDosageEntity> , PagingDosageService<VehicleInnerRnDosageEntity> {
}
