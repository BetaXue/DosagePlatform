package com.evision.dosage.mapper.vehicle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evision.dosage.pojo.entity.vehicle.VehicleInnerRnDosageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/20 16:52
 */
@Mapper
public interface VehicleInnerRnDosageMapper extends BaseMapper<VehicleInnerRnDosageEntity> {
    /**
     * 查询最新数据，disabled=1
     * @return
     */
    List<VehicleInnerRnDosageEntity> queryValidData();

    /**
     * 查询最新数据，disabled=1
     * @return
     */
    List<VehicleInnerRnDosageEntity> queryValidByKeys(VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity);

    /**
     * 添加一条新记录
     * @param vehicleInnerRnDosageEntity
     * @return
     */
    int add(VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity);

    /**
     * 将原有记录的disabled设未0
     * @param vehicleInnerRnDosageEntity
     * @return
     */
    int updateDisabled(VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity);

    /**
     * 将原有记录的deleted设为1
     * @param vehicleInnerRnDosageEntity
     * @return
     */
    int delete(VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity);

    /**
     * 查询历史记录
     * @param dataCategory
     * @param route
     * @return
     */
    List<VehicleInnerRnDosageEntity> getHistory(@Param("dataCategory") String dataCategory, @Param("route") String route);


    /**
     * q前端分页查询
     * @param page
     * @param queryName
     * @param orderField
     * @param isAsc
     * @return
     */
    IPage<VehicleInnerRnDosageEntity> queryValidDataFowWeb(Page<VehicleInnerRnDosageEntity> page, @Param("queryName") String queryName, @Param("orderField") String orderField, @Param("isAsc") String isAsc, @Param("incloudDel") Integer incloudDel);

    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    List<VehicleInnerRnDosageEntity> getDataById(@Param("id") int id);
}
