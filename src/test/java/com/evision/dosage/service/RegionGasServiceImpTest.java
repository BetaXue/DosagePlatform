package com.evision.dosage.service;

import com.evision.dosage.pojo.entity.RegionGas;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/25
 * 氡、钍射气及子体实体 --- 各地区、类型氡浓度
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RegionGasServiceImpTest {

    @Resource
    RegionGasService regionGasService;

    @Test
    public void getRegionGass() {
    }

    @Test
    public void insertRegionGass() {
    }

    @Test
    public void importExcel() {
        String filePath = "C:\\Users\\小宇宙\\Desktop\\办公\\三个观测汇总.xlsx";
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "", fileInputStream);
            regionGasService.importExcel(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getHistory() {
        String one = "三个观测地点汇总";
        String two = "氡子体浓度";
        String three = "";
        try {
            List<RegionGas> list = regionGasService.getHistory(1);
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}