package com.evision.dosage.controller;

import com.evision.dosage.constant.DosageGammaRayConfig;
import com.evision.dosage.pojo.model.DosageResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 陆地伽马api
 *
 * @Author: Yu Xiao
 * @Date: 2020/2/19 22:52
 */
@RestController
@Slf4j
@RequestMapping("/gammaRay")
public class GammaRayController {
    @Autowired
    private DosageGammaRayConfig dosageGammaRayConfig;

    /**
     * 返回给前端首页
     *
     * @return
     */
    @PostMapping("/showMap")
    public DosageResponseBody showGridData() {
        Map<String, Object> map = dosageGammaRayConfig.getMap();
        return DosageResponseBody.success(map);
    }
}
