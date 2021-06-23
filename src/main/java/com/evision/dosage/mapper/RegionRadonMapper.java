package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.AverageRadon;
import com.evision.dosage.pojo.entity.RegionRadon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 雷亚亚
 * @date 2020/2/22
 * 氡、钍射气及子体实体 --- 各地区氡浓度
 */
public interface RegionRadonMapper extends BaseMapper<RegionRadon> {
    /**
     * 查询所有数据
     *
     * @return
     */
    List<RegionRadon> getRegionRadons(@Param("incloudDel") Integer incloudDel);

    /**
     * 批量增加
     *
     * @param regionRadons
     * @return
     */
    int insertRegionRadons(@Param("regionRadons") List<RegionRadon> regionRadons);

    /**
     * 单个新增操作，存在就更新，不存在就新增
     *
     * @param regionRadon 参数实体
     * @return 执行成功条数
     */
    int insertRegionRadon(@Param("regionRadon") RegionRadon regionRadon);

    /**
     * 更新
     *
     * @param regionRadon 参数实体
     * @return 执行成功条数
     */
    int updateRegionRadon(@Param("regionRadon") RegionRadon regionRadon);

    /**
     * 获取历史记录
     *
     * @return
     */
    List<RegionRadon> getHistory(@Param("region") String region);

    /**
     * 删除操作---逻辑删除，更新deleted字段
     * deleted 删除状态（1、已删除，0、未删除）
     *
     * @param regionRadon 参数实体
     * @return 执行成功条数
     */
    int deleteRegionRadon(@Param("regionRadon") RegionRadon regionRadon);
}
