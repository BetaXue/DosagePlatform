package com.evision.dosage.controller;

import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.AverageRadonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 雷亚亚
 * @date 2020/2/22
 * 氡、钍射气及子体 --- 平均氡浓度
 */
@Slf4j
@RestController
@RequestMapping(value = "/averageRadon")
public class AverageRadonController {
    @Autowired
    private AverageRadonService averageRadonService;

    @PostMapping(value = "/getlist")
    public DosageResponseBody getLists() {
        try {
            return averageRadonService.getAverageRadons();
        } catch (Exception e) {
            return DosageResponseBody.failure("操作失败");
        }
    }
}
