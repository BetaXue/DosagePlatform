package com.evision.dosage.service.vehicle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.vehicle.VehicleDosageRateEntity;
import com.evision.dosage.service.PagingDosageService;

/**
 * @author DingZhanYang
 * @date 2020/2/21 15:50
 */
public interface VehicleDosageRateService extends IService<VehicleDosageRateEntity>, PagingDosageService<VehicleDosageRateEntity> {
}
