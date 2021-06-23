package com.evision.dosage.service;

import com.evision.dosage.pojo.entity.RegionRadon;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/25
 * 氡、钍射气及子体实体 --- 各站点氡浓度
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RegionRadonServiceImpTest {
    @Resource
    private RegionRadonService regionRadonService;

    @Test
    public void getRegionRadons() {
        List<RegionRadon> dataList = null;
        try {
            dataList = regionRadonService.getDataList("", "", 0, 0);
            for (RegionRadon regionRadon :
                    dataList) {
                LocalDateTime update = regionRadon.getUpdateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String updateTime = update.format(formatter);
                regionRadon.setUpdateTime(update);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataList.size();
    }

    @Test
    public void insertRegionRadons() {
    }

    @Test
    public void importExcel() {
        String filePath = "C:\\Users\\小宇宙\\Desktop\\办公\\氡、钍射气及子体-站点平均浓度.xlsx";
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "", fileInputStream);
            regionRadonService.importExcel(multipartFile);
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
            List<RegionRadon> list = regionRadonService.getHistory(1);
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}