package com.evision.dosage.mapper.vehicle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evision.dosage.pojo.entity.vehicle.VehicleInnerDosageEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/20 16:52
 */
public interface VehicleInnerDosageMapper extends BaseMapper<VehicleInnerDosageEntity> {
    /**
     * 查询最新数据，disabled=1
     * @return
     */
    List<VehicleInnerDosageEntity> queryValidData();

    /**
     * 查询最新数据，disabled=1
     * @return
     */
    List<VehicleInnerDosageEntity> queryValidByKeys(VehicleInnerDosageEntity vehicleInnerDosageEntity);

    /**
     * 添加一条新记录
     * @param vehicleInnerDosageEntity
     * @return
     */
    int add(VehicleInnerDosageEntity vehicleInnerDosageEntity);

    /**
     * 将原有记录的disabled设未0
     * @param vehicleInnerDosageEntity
     * @return
     */
    int updateDisabled(VehicleInnerDosageEntity vehicleInnerDosageEntity);

    /**
     * 将原有记录的deleted设为1
     * @param vehicleInnerDosageEntity
     * @return
     */
    int delete(VehicleInnerDosageEntity vehicleInnerDosageEntity);
    /**
     * 查询历史记录
     * @param dataCategory
     * @param roadType
     * @return
     */
    List<VehicleInnerDosageEntity> getHistory(@Param("dataCategory") String dataCategory, @Param("roadType") String roadType);

    /**
     * q前端分页查询
     * @param page
     * @param queryName
     * @param orderField
     * @param isAsc
     * @return
     */
    IPage<VehicleInnerDosageEntity> queryValidDataFowWeb(Page<VehicleInnerDosageEntity> page, @Param("queryName") String queryName, @Param("orderField") String orderField, @Param("isAsc") String isAsc);

    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    List<VehicleInnerDosageEntity> getDataById(@Param("id") int id);
}
