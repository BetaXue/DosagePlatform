package com.evision.dosage.mapper.vehicle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evision.dosage.pojo.entity.vehicle.VehicleDosageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/20 16:51
 */
@Mapper
public interface VehicleDosageMapper  extends BaseMapper<VehicleDosageEntity> {
    /**
     * 查询最新数据，disabled=1
     * @return
     */
    List<VehicleDosageEntity> queryValidData();

    /**
     * 查询最新数据，disabled=1
     * @return
     */
    List<VehicleDosageEntity> queryValidByKeys(VehicleDosageEntity vehicleDosageEntity);

    /**
     * 添加一条新记录
     * @param vehicleDosageEntity
     * @return
     */
    int add(VehicleDosageEntity vehicleDosageEntity);

    /**
     * 将原有记录的disabled设未0
     * @param vehicleDosageEntity
     * @return
     */
    int updateDisabled(VehicleDosageEntity vehicleDosageEntity);

    /**
     * 将原有记录的deleted设为1
     * @param vehicleDosageEntity
     * @return
     */

    int delete(VehicleDosageEntity vehicleDosageEntity);
    /**
     * 查询历史记录
     * @param dataCategory
     * @param route
     * @return
     */
    List<VehicleDosageEntity> getHistory(@Param("dataCategory") String dataCategory, @Param("route") String route);

    /**
     * q前端分页查询
     * @param page
     * @param queryName
     * @param orderField
     * @param isAsc
     * @return
     */
    IPage<VehicleDosageEntity> queryValidDataFowWeb(Page<VehicleDosageEntity> page, @Param("queryName") String queryName, @Param("orderField") String orderField, @Param("isAsc") String isAsc, @Param("incloudDel") Integer incloudDel);

    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    List<VehicleDosageEntity> getDataById(@Param("id") int id);
}
