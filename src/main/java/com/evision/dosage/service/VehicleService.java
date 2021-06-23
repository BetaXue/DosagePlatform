package com.evision.dosage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.vehicle.VehicleSummaryDosageEntity;
import com.evision.dosage.pojo.model.QueryRequestBody;

/**
 * @author DingZhanYang
 * @date 2020/2/19 16:45
 */
public interface VehicleService extends IService<VehicleSummaryDosageEntity> {
    /**
     * 用户数据分页查询
     *
     * @param queryRequestBody 分页实体
     * @return 用户分页对象
     */
    IPage<VehicleSummaryDosageEntity> querySummaryDosage(QueryRequestBody queryRequestBody);
}
