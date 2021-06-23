package com.evision.dosage.service;

import com.evision.dosage.pojo.entity.AverageRadon;
import com.evision.dosage.pojo.model.DosageDbHeader;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/25
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class AverageRadonServiceImpTest {

    @Resource
    private AverageRadonService averageRadonService;

    @Test
    public void getAverageRadons() {
        DosageResponseBody dosageResponseBody = averageRadonService.getAverageRadons();
        System.out.println(dosageResponseBody);
    }
    @Test
    public void getDataList() {
        List<AverageRadon> list = null;
        try {
            list = averageRadonService.getDataList("","",0,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(list);
    }

    @Test
    public void insertAverageRadons() {
        List<AverageRadon> averageRadons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AverageRadon averageRadon = new AverageRadon();
            averageRadon.setAverageRadon("123");
            averageRadon.setRegion("地点" + i);
            averageRadon.setCreateTime(LocalDateTime.now());
            averageRadon.setUpdateTime(LocalDateTime.now());
            averageRadon.setDeleted(0);
            averageRadon.setDisabled(1);
            averageRadon.setUserId(1);
        }
        averageRadonService.insertAverageRadons(averageRadons);
    }

    @Test
    public void importExcel() {
        String filePath = "C:\\Users\\小宇宙\\Desktop\\办公\\氡、钍射气及子体-平均氡浓度.xlsx";
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "", fileInputStream);
            averageRadonService.importExcel(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getHistory() {
        String one = "东城";
        String two = "";
        String three = "";
        try {
            List<AverageRadon> list = averageRadonService.getHistory(1);
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDbHeader() {
        try {
            List<DosageDbHeader> lists = averageRadonService.getDbHeader();
            System.out.println(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}