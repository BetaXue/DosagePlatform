package com.evision.dosage.service.imp;

import com.evision.dosage.pojo.entity.PubIntegratedLevel;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class PubIntegratedLevelServiceImpTest {
    @Resource
    PubIntegratedLevelServiceImp pubIntegratedLevelServiceImp;

    @Test
    public void importExcel() throws IOException {
        try{

            File file = new File("C:\\Users\\Administrator\\Desktop\\整体水平.xlsx");
            InputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "", inputStream);
            System.out.println(multipartFile.getName() + multipartFile.getOriginalFilename());
            List<PubIntegratedLevel> list = pubIntegratedLevelServiceImp.importExcel(multipartFile);
            Assert.assertTrue(list.size() > 0);

        }catch (Exception e){

        }

    }

    @Test
    public void getDataList() {
        List<PubIntegratedLevel> list = pubIntegratedLevelServiceImp.getDataList("","",1,0);
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void getHistory() throws Exception {
        List<PubIntegratedLevel> list = pubIntegratedLevelServiceImp.getHistory(1);
        Assert.assertTrue(list.size() > 0);
    }
}