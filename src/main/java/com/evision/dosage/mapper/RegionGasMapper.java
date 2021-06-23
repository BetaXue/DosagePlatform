package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.RegionGas;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 雷亚亚
 * @date 2020/2/22
 * 氡、钍射气及子体实体 --- 各地区、类型氡浓度
 */
public interface RegionGasMapper extends BaseMapper<RegionGas> {
    /**
     * 根据地区查询数据
     *
     * @param incloudDel 是否包含删除数据
     * @return
     */
    List<RegionGas> getRegionGass(@Param("incloudDel") Integer incloudDel);

    /**
     * 批量增加
     *
     * @param regionGases
     * @return
     */
    int insertRegionGass(@Param("regionGases") List<RegionGas> regionGases);

    /**
     * 单个新增操作，存在就更新，不存在就新增
     *
     * @param regionGas 参数实体
     * @return 执行成功条数
     */
    int insertRegionGas(@Param("regionGas") RegionGas regionGas);

    /**
     * 更新
     *
     * @param regionGas 参数实体
     * @return 执行成功条数
     */
    int updateRegionGas(@Param("regionGas") RegionGas regionGas);

    /**
     * 获取历史记录
     *
     * @return
     */
    List<RegionGas> getHistory(@Param("region") String region, @Param("type") String type);

    /**
     * 删除操作---逻辑删除，更新deleted字段
     * deleted 删除状态（1、已删除，0、未删除）
     *
     * @param regionGas 参数实体
     * @return 执行成功条数
     */
    int deleteRegionGas(@Param("regionGas") RegionGas regionGas);
}
