package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.PubHotSpringRadonConcentrationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PubHotSpringRadonConcentrationMapper extends BaseMapper<PubHotSpringRadonConcentrationEntity> {
    List<PubHotSpringRadonConcentrationEntity> selectAll(@Param("disabled") Integer disabled, @Param("deleted") Integer deleted);

    List<PubHotSpringRadonConcentrationEntity> selectHistory(@Param("disabled") Integer disabled, @Param("deleted") Integer deleted, @Param("hotSpring") String hotSpring);

    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    List<PubHotSpringRadonConcentrationEntity> getDataById(@Param("id") int id);
}
