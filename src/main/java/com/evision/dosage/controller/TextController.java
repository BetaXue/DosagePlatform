package com.evision.dosage.controller;

import com.evision.dosage.pojo.model.BaiDuEntity;
import com.evision.dosage.utils.BaiDuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: AubreyXue
 * @date: 2020-03-10 20:59
 **/
@RestController
@RequestMapping("test")
public class TextController {

    @Resource
    private BaiDuUtils baiDuUtils;

    @GetMapping("convert")
    public BaiDuEntity convert(@RequestParam String x,@RequestParam String y){
       return baiDuUtils.convertToBaidu(x,y);
    }

}
