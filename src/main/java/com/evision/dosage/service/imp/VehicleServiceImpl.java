package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.mapper.VehicleSummaryMapper;
import com.evision.dosage.pojo.entity.vehicle.VehicleSummaryDosageEntity;
import com.evision.dosage.pojo.model.QueryRequestBody;
import com.evision.dosage.service.VehicleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author DingZhanYang
 * @date 2020/2/19 16:57
 */
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleSummaryMapper, VehicleSummaryDosageEntity> implements VehicleService {

    @Resource
    VehicleSummaryMapper vehicleSummaryMapper;
    @Override
    public IPage<VehicleSummaryDosageEntity> querySummaryDosage(QueryRequestBody queryRequestBody){
        return null;
    }

}
