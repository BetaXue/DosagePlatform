package com.evision.dosage.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.evision.dosage.pojo.entity.individualized.IndividualizedDose;
import com.evision.dosage.pojo.entity.individualized.IndividualizedItem;
import com.evision.dosage.pojo.entity.individualized.IndividualizedResultJson;
import com.evision.dosage.pojo.entity.individualized.StatisticalResult;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Classname IndividualizedDoseServiceTest
 * @Description 个体化剂量测试
 * @Date 2020/2/25 上午10:45
 * @Created by liufree
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndividualizedDoseServiceTest {
    @Resource
    private IndividualizedDoseService individualizedDoseService;

    @Test
    public void compute() throws IOException {
        File file = ResourceUtils.getFile("classpath:IndividualizedDose.json");
        String content = FileUtils.readFileToString(file, "UTF-8");
        Gson gson = new Gson();
        IndividualizedDose individualizedDose = gson.fromJson(content, IndividualizedDose.class);
        individualizedDose.setUserId(1);
        List<IndividualizedItem> itemList = individualizedDoseService.compute(individualizedDose);
        Assert.assertTrue(itemList.size() > 0);
    }

    @Test
    public void getStatisticalResultList() {
        IPage<StatisticalResult> list = individualizedDoseService.getStatisticalResultList(10, 1, 1);
        Assert.assertTrue(list.getSize() > 0);
    }

    @Test
    public void getSingleResult() {
        IndividualizedResultJson individualizedResultJson = individualizedDoseService.getSingleResult(1);
        Assert.assertNotNull(individualizedResultJson);
    }
}