package com.evision.dosage.service.vehicle;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.vehicle.VehicleDosageEntity;
import com.evision.dosage.service.PagingDosageService;

/**
 * @author DingZhanYang
 * @date 2020/2/21 9:18
 */
public interface VehicleDosageService extends IService<VehicleDosageEntity>, PagingDosageService<VehicleDosageEntity> {
}
