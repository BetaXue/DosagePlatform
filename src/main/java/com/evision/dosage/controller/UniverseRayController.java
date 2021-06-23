package com.evision.dosage.controller;

import com.evision.dosage.constant.UniverseRayConfig;
import com.evision.dosage.pojo.entity.UniverseRay;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.UniverseRayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 宇宙射线api
 *
 * @Author: Yu Xiao
 * @Date: 2020/2/19 22:52
 */
@RestController
@Slf4j
@RequestMapping("/universeRay")
public class UniverseRayController {
    @Autowired
    private UniverseRayService universeRayService;
    @Autowired
    private UniverseRayConfig universeRayConfig;

    /**
     * 展示地图数据
     *
     * @return
     */
    @PostMapping("/showMap")
    public DosageResponseBody showGridData() {
        Map<String, Object> map = universeRayConfig.getMap();
        return DosageResponseBody.success(map);
    }

    /**
     * 展示表格数据
     *
     * @return
     */
    @PostMapping("/showTable")
    public DosageResponseBody showGridshowTableData() {
        List<UniverseRay> list = universeRayService.selectGroupByAreaCode();
        return DosageResponseBody.success(list);
    }


}
