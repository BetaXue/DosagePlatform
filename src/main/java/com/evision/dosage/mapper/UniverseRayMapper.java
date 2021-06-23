package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evision.dosage.pojo.entity.UniverseRay;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Author: Yu Xiao
 * @Date: 2020/2/17 21:48
 */
@Repository
public interface UniverseRayMapper extends BaseMapper<UniverseRay> {
    /**
     * 按区分组展示剂量率
     * @return
     */
    List<UniverseRay> selectGroupByAreaCode();
}
