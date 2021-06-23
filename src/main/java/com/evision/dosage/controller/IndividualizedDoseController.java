package com.evision.dosage.controller;

import com.evision.dosage.annotation.PassToken;
import com.evision.dosage.pojo.entity.individualized.Dental;
import com.evision.dosage.pojo.entity.individualized.IndividualizedDose;
import com.evision.dosage.pojo.entity.individualized.IndividualizedDoseConvert;
import com.evision.dosage.pojo.entity.individualized.IndividualizedResultJson;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.IndividualizedDoseService;
import com.evision.dosage.utils.UserUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Classname IndividualizedDoseController
 * @Description 个体化剂量
 * @Date 2020/2/20 上午10:02
 * @Created by liufree
 */
@RestController
@RequestMapping(value = "/national/dosage/individualized")
public class IndividualizedDoseController {

    @Resource
    private IndividualizedDoseService individualizedDoseService;
    BeanCopier bc = BeanCopier.create(IndividualizedDose.class, IndividualizedDoseConvert.class,
            false);

    @PostMapping("/compute")
    public DosageResponseBody compute(@RequestBody IndividualizedDose individualizedDose) throws Exception {
        int userId = UserUtils.getCurrentUserId();
        individualizedDose.setUserId(userId);
        IndividualizedResultJson individualizedResultJson = new IndividualizedResultJson();
        individualizedResultJson.setUserId(userId);
        individualizedResultJson.setOutput(individualizedDoseService.compute(individualizedDose));

        IndividualizedDoseConvert individualizedDoseConvert = new IndividualizedDoseConvert();
        bc.copy(individualizedDose,individualizedDoseConvert,null);
        individualizedDoseConvert.setSex(individualizedDose.getSex()==0?"男":"女");
        individualizedResultJson.setInput(individualizedDoseConvert);
        return DosageResponseBody.success(individualizedResultJson);
    }

    /**
     * 根据用户权限获取不同的统计结果列表
     *
     * @param
     * @return
     */
    @GetMapping("/getStatisticalResult")
    public DosageResponseBody getStatisticalResult(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNum) throws Exception {
        int userId = UserUtils.getCurrentUserId();
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return DosageResponseBody.success(individualizedDoseService.getStatisticalResultList(pageSize, pageNum, userId));
    }

    /**
     * 根据individualizedDoseResultId获取单个计算结果
     */
    @GetMapping("/getSingleResult/{idv_id}")
    public DosageResponseBody getSingleResult(@PathVariable("idv_id") Integer idvId) {
        return DosageResponseBody.success(individualizedDoseService.getSingleResult(idvId));
    }
}