package com.evision.dosage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.AverageRadon;
import com.evision.dosage.pojo.entity.RegionRadon;
import com.evision.dosage.pojo.model.DosageResponseBody;

import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/22
 * 氡、钍射气及子体实体 --- 各地区氡浓度
 */
public interface RegionRadonService extends IService<RegionRadon>, DosageService<RegionRadon> {
    /**
     * 各地区氡浓度 查询数据
     *
     * @return 返回表所有数据
     */
    DosageResponseBody getRegionRadons();

    /**
     * 批量插入数据
     *
     * @param regionRadons 要插入的数据
     * @return 返回成功条数
     */
    int insertRegionRadons(List<RegionRadon> regionRadons);

}
