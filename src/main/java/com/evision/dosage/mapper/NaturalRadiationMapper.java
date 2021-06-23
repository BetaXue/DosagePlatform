package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.NaturalRadiation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 雷亚亚
 * @date 2020/2/19
 * 天然辐射
 */
public interface NaturalRadiationMapper extends BaseMapper<NaturalRadiation> {
    /**
     * 查询所有数据
     *
     * @return
     */
    List<NaturalRadiation> getNaturalRadiations(@Param("incloudDel") Integer incloudDel);

    /**
     * 批量增加
     *
     * @param naturalRadiations
     * @return
     */
    int insertNaturalRadiations(@Param("naturalRadiations") List<NaturalRadiation> naturalRadiations);

    /**
     * 单个新增操作，存在就更新，不存在就新增
     *
     * @param naturalRadiation 参数实体
     * @return 执行成功条数
     */
    int insertNaturalRadiation(@Param("naturalRadiation") NaturalRadiation naturalRadiation);

    /**
     * 单个更新操作
     *
     * @param naturalRadiation 参数实体
     * @return 执行成功条数
     */
    int updateNaturalRadiation(@Param("naturalRadiation") NaturalRadiation naturalRadiation);

    /**
     * 获取历史记录
     *
     * @param colNumber
     * @return
     */
    List<NaturalRadiation> getHistory(@Param("colNumber") String colNumber);
}
