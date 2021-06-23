package com.evision.dosage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.RegionGas;
import com.evision.dosage.pojo.entity.RegionRadon;
import com.evision.dosage.pojo.model.DosageResponseBody;

import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/24
 * 氡、钍射气及子体实体 --- 各地区、类型氡浓度
 */
public interface RegionGasService extends IService<RegionGas>, DosageService<RegionGas> {
    /**
     * 各地区、类型氡浓度 查询数据
     *
     * @return 返回表所有数据
     * @region 地区
     */
    DosageResponseBody getRegionGass(String region);

    /**
     * 批量插入数据
     *
     * @param regionGass 要插入的数据
     * @return 返回成功条数
     */
    int insertRegionGass(List<RegionGas> regionGass);

}
