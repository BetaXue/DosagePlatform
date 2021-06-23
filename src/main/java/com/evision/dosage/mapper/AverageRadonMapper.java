package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.AverageRadon;
import com.evision.dosage.pojo.entity.NaturalRadiation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 雷亚亚
 * @date 2020/2/22
 * 氡、钍射气及子体实体 --- 平均氡浓度
 */
public interface AverageRadonMapper extends BaseMapper<AverageRadon> {
    /**
     * 查询所有数据
     *
     * @return
     */
    List<AverageRadon> getAverageRadons(@Param("incloudDel") Integer incloudDel);

    /**
     * 批量增加
     *
     * @param averageRadons
     * @return
     */
    int insertAverageRadons(@Param("averageRadons") List<AverageRadon> averageRadons);

    /**
     * 单个新增操作，存在就更新，不存在就新增
     *
     * @param averageRadon 参数实体
     * @return 执行成功条数
     */
    int insertAverageRadon(@Param("averageRadon") AverageRadon averageRadon);

    /**
     * 更新操作
     *
     * @param averageRadon 参数实体
     * @return 执行成功条数
     */
    int updateAverageRadon(@Param("averageRadon") AverageRadon averageRadon);

    /**
     * 获取历史记录
     *
     * @param region
     * @return
     */
    List<AverageRadon> getHistory(@Param("region") String region);

    /**
     * 删除操作---逻辑删除，更新deleted字段
     * deleted 删除状态（1、已删除，0、未删除）
     *
     * @param averageRadon 参数实体
     * @return 执行成功条数
     */
    int deleteAverageRadon(@Param("averageRadon") AverageRadon averageRadon);

}
