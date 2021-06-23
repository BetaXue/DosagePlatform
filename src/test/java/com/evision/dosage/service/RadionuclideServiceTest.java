package com.evision.dosage.service;

import com.evision.dosage.pojo.entity.AerosolRadionuclideEntity;
import com.evision.dosage.pojo.entity.FoodWaterRadionuclideEntity;
import com.evision.dosage.service.imp.FoodRadionuclideServiceImpl;
import com.evision.dosage.service.imp.WaterRadionuclideServiceImpl;
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
import java.io.InputStream;
import java.util.List;

/**
 * 食物、水样、气溶胶中放射性核素活度浓度Test
 *
 * @Author: Yu Xiao
 * @Date: 2020/2/25 14:31
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RadionuclideServiceTest {
    @Resource
    private FoodRadionuclideServiceImpl foodRadionuclideService;
    @Resource
    private WaterRadionuclideServiceImpl waterRadionuclideService;
    @Resource
    private AerosolRadionuclideService aerosolRadionuclideService;

    /**
     * 上传
     */
    @Test
    public void upload() {
        try {
            File file = new File("C:\\Users\\yx\\Desktop\\国民剂量统计\\原始数据.xlsx");
            InputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "text/plain", inputStream);
            List<FoodWaterRadionuclideEntity> waters = waterRadionuclideService.importExcel(multipartFile);
            List<FoodWaterRadionuclideEntity> foods = foodRadionuclideService.importExcel(multipartFile);
            List<AerosolRadionuclideEntity> aersols = aerosolRadionuclideService.importExcel(multipartFile);

            log.info(waters.size() + "," + foods.size() + "," + aersols.size());
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    /**
     * 展示数据
     */
    @Test
    public void showData() {
        try {
            List<FoodWaterRadionuclideEntity> allData = foodRadionuclideService.getDataList(null, null, null, 0);
            List<AerosolRadionuclideEntity> aerosolRadionuclideEntities = aerosolRadionuclideService.getDataList(null, null, null, 0);
            log.info(allData.size() + "," + aerosolRadionuclideEntities.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 历史记录
     */
    @Test
    public void history() {
        try {
            List<FoodWaterRadionuclideEntity> history = foodRadionuclideService.getHistory(1);
            log.info(history.size() + "");
        } catch (Exception e) {


        }
    }

}
