package com.evision.dosage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.evision.dosage.pojo.entity.GammaRay;
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
 * 陆地伽马辐射Test
 * @Author: Yu Xiao
 * @Date: 2020/2/25 14:31
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class GammaRayServiceTest {
    @Resource
    private GammaRayService gammaRayService;

    /**
     * 上传
     */
    @Test
    public void upload() {
            try {
                File file = new File("C:\\Users\\yx\\Desktop\\陆地伽马辐射原始数据20200224.xlsx");
                InputStream inputStream = new FileInputStream(file);
                MultipartFile multipartFile = new MockMultipartFile(file.getName(),file.getName(),"text/plain", inputStream);
                List<GammaRay> gammaRays = gammaRayService.importExcel(multipartFile);
                log.info(gammaRays.size()+"");
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
            IPage<GammaRay> allData = gammaRayService.getDataList(null, 10, 1, null, null, 0);
            IPage<GammaRay> search = gammaRayService.getDataList("丁香园", 10, 1, null, null, 0);
            log.info(allData.getTotal() + "," + search.getSize());
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
            List<GammaRay> history = gammaRayService.getHistory(1);
            log.info(history.size()+"");
        } catch (Exception e) {


        }
    }

}
