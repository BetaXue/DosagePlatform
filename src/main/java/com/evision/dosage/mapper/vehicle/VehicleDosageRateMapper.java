package com.evision.dosage.mapper.vehicle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evision.dosage.pojo.entity.vehicle.VehicleDosageRateEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/20 16:51
 */
public interface VehicleDosageRateMapper extends BaseMapper<VehicleDosageRateEntity> {
    /**
     * 查询最新数据，disabled=1
     * @return
     */
    List<VehicleDosageRateEntity> queryValidData();

    /**
     * 查询最新数据，disabled=1 and deleted=0
     * @return
     */
    List<VehicleDosageRateEntity> queryValidDataForCompute();

    /**
     * 查询最新数据，disabled=1
     * @return
     */
    List<VehicleDosageRateEntity> queryValidByKeys(VehicleDosageRateEntity vehicleDosageRateEntity);

    /**
     * 添加一条新记录
     * @param vehicleDosageRateEntity
     * @return
     */
    int add(VehicleDosageRateEntity vehicleDosageRateEntity);

    /**
     * 将原有记录的disabled设未0
     * @param vehicleDosageRateEntity
     * @return
     */
    int updateDisabled(VehicleDosageRateEntity vehicleDosageRateEntity);

    /**
     * 将原有记录的deleted设为1
     * @param vehicleDosageRateEntity
     * @return
     */
    int delete(VehicleDosageRateEntity vehicleDosageRateEntity);
    /**
     * 查询历史记录
     * @param vehicleCategory
     * @param route
     * @return
     */
    List<VehicleDosageRateEntity> getHistory(@Param("vehicleCategory") String vehicleCategory, @Param("route") String route, @Param("station") String station);

    /**
     * q前端分页查询
     * @param page
     * @param queryName
     * @param orderField
     * @param isAsc
     * @return
     */
    IPage<VehicleDosageRateEntity> queryValidDataFowWeb(Page<VehicleDosageRateEntity> page, @Param("queryName") String queryName, @Param("orderField") String orderField, @Param("isAsc") String isAsc, @Param("incloudDel") Integer incloudDel);

    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    List<VehicleDosageRateEntity> getDataById(@Param("id") int id);
}
