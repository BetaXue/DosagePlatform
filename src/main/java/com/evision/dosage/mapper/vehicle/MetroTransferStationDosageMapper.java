package com.evision.dosage.mapper.vehicle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evision.dosage.pojo.entity.vehicle.MetroTransferStationDosageEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/20 16:51
 */
public interface MetroTransferStationDosageMapper  extends BaseMapper<MetroTransferStationDosageEntity> {
    /**
     * 查询最新数据，disabled=1
     * @return
     */
    List<MetroTransferStationDosageEntity> queryValidData();

    /**
     * 查询最新数据，disabled=1
     * @return
     */
    List<MetroTransferStationDosageEntity> queryValidByKeys(MetroTransferStationDosageEntity metroTransferStationDosageEntity);

    /**
     * 添加一条新记录
     * @param metroTransferStationDosageEntity
     * @return
     */
    int add(MetroTransferStationDosageEntity metroTransferStationDosageEntity);

    /**
     * 将原有记录的disabled设未0
     * @param metroTransferStationDosageEntity
     * @return
     */
    int updateDisabled(MetroTransferStationDosageEntity metroTransferStationDosageEntity);

    /**
     * 将原有记录的deleted设为1
     * @param metroTransferStationDosageEntity
     * @return
     */
    int delete(MetroTransferStationDosageEntity metroTransferStationDosageEntity);

    /**
     * 查询历史记录
     * @param dataCategory
     * @param station
     * @return
     */
    List<MetroTransferStationDosageEntity> getHistory(@Param("dataCategory") String dataCategory, @Param("station") String station);

    /**
     * q前端分页查询
     * @param sortName
     * @param page
     * @param queryName
     * @param isAsc
     * @param incloudDel
     * @return
     */
    IPage<MetroTransferStationDosageEntity> queryValidDataFowWeb(Page<MetroTransferStationDosageEntity> page, @Param("queryName") String queryName, @Param("orderField") String orderField, @Param("isAsc") String isAsc, @Param("incloudDel")Integer incloudDel);

}
