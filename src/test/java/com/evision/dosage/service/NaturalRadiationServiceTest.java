package com.evision.dosage.service;

import com.evision.dosage.pojo.entity.NaturalRadiation;
import com.evision.dosage.pojo.model.DosageResponseBody;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/19
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class NaturalRadiationServiceTest {

    @Resource
    private NaturalRadiationService naturalRadiationService;

    @Test
    public void getNaturalRadiations() {
        DosageResponseBody dosageResponseBody = naturalRadiationService.getNaturalRadiations();
        System.out.println(dosageResponseBody);
    }

    @Test
    public void insertNaturalRadiations() {
        List<NaturalRadiation> lists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NaturalRadiation naturalRadiation1 = new NaturalRadiation();
            naturalRadiation1.setColNumber(i + 1);
            naturalRadiation1.setCreateTime(LocalDateTime.now());
            naturalRadiation1.setUpdateTime(LocalDateTime.now());
            naturalRadiation1.setDeleted(0);
            naturalRadiation1.setDisabled(1);
            naturalRadiation1.setUserId(1);
            naturalRadiation1.setAnnualDose(String.valueOf(i + 22));
            naturalRadiation1.setDoseRate(String.valueOf(i + 3));
            lists.add(naturalRadiation1);
        }
        naturalRadiationService.insertNaturalRadiations(lists);
    }

    @Test
    public void importExcel() {
        String filePath = "C:\\Users\\小宇宙\\Desktop\\办公\\天然辐射.xlsx";
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "", fileInputStream);
            naturalRadiationService.importExcel(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getHistory() {
        String one = "2";
        String two = "";
        String three = "";
        try {
            List<NaturalRadiation> list = naturalRadiationService.getHistory(1);
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}