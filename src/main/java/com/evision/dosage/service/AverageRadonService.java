package com.evision.dosage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.AverageRadon;
import com.evision.dosage.pojo.model.DosageResponseBody;

import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/22
 * 氡、钍射气及子体实体 --- 平均氡浓度
 */
public interface AverageRadonService extends IService<AverageRadon>, DosageService<AverageRadon> {
    /**
     * 平均氡浓度 查询数据
     *
     * @return 返回表所有数据
     */
    DosageResponseBody getAverageRadons();

    /**
     * 批量插入数据
     *
     * @param averageRadons 要插入的数据
     * @return 返回成功条数
     */
    int insertAverageRadons(List<AverageRadon> averageRadons);

}
