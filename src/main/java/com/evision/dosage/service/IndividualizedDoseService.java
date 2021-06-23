package com.evision.dosage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.evision.dosage.pojo.entity.individualized.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Classname IndividualizedDoseService
 * @Description TODO，计算
 * @Date 2020/2/20 下午2:44
 * @Created by liufree
 */
public interface IndividualizedDoseService {

    List<IndividualizedItem> compute(IndividualizedDose individualizedDose);

    IPage<StatisticalResult> getStatisticalResultList(Integer pageSize, Integer pageNum, Integer userId);

    IndividualizedResultJson getSingleResult(Integer id);
}
