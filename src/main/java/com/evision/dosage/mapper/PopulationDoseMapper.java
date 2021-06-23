package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.PopulationDoseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 居民剂量首页数据库操作层
 *
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-19 14:44
 */
public interface PopulationDoseMapper extends BaseMapper<PopulationDoseEntity> {
    /**
     * 查询居民剂量首页数据
     *
     * @param populationDoseEntity 查询参数
     * @return 数据集合
     */
    List<PopulationDoseEntity> getAllPopulationDose(PopulationDoseEntity populationDoseEntity, @Param("incloudDel") Integer incloudDel);

    /**
     * 根据类别获取实体
     *
     * @param category 类型
     * @return 获取对象
     */
    PopulationDoseEntity selectByCategory(@Param("category") String category);

    /**
     * 根据类别名称查询历史记录
     *
     * @param category 类别名称
     * @return 历史记录
     */
    List<PopulationDoseEntity> getHistory(@Param("category") String category);
}
