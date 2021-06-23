package com.evision.dosage.service.imp;

import com.evision.dosage.pojo.entity.PubHotSpringRadonConcentrationEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Assert;
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
import java.util.Map;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class PubHotSpringRadonConcentrationServiceImpTest {

    @Resource
    PubHotSpringRadonConcentrationServiceImp pubHotSpringRadonConcentrationServiceImp;

    @Test
    public void importExcel() {
        try {
            File file = new File("C:\\Users\\Administrator\\Desktop\\温泉设施氡浓度测量结果.xlsx");
            InputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "text/plain", inputStream);
            List<T> list = pubHotSpringRadonConcentrationServiceImp.importExcel(multipartFile);
            Assert.assertTrue(list.size() > 0);
        } catch (Exception e) {
        }

    }

    @Test
    public void getDataList() {
        try {
            List<PubHotSpringRadonConcentrationEntity> list = pubHotSpringRadonConcentrationServiceImp.getDataList("", "", 1, 0);
            Assert.assertTrue(list.size() > 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void getHistory() {
        try {
            List<Map> list = pubHotSpringRadonConcentrationServiceImp.getHistory(1);
            Assert.assertTrue(list.size() > 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}