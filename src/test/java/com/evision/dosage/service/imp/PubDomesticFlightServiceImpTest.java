package com.evision.dosage.service.imp;

import com.evision.dosage.pojo.entity.PubDomesticFlightEntity;
import com.evision.dosage.pojo.entity.user.UserEntity;
import com.evision.dosage.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Assert;
import org.junit.Before;
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


@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class PubDomesticFlightServiceImpTest {

    @Resource
    PubDomesticFlightServiceImp pubDomesticFlightServiceImp;

    @Before
    public void setUser() {
        UserEntity user = new UserEntity();
        user.setId(1);
        UserUtils.setCurrentUser(user);
    }

    @Test
    public void importExcel() {
        try {
            File file = new File("C:\\Users\\Administrator\\Desktop\\国内航班.xlsx");
            InputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "text/plain", inputStream);
            List<T> list = pubDomesticFlightServiceImp.importExcel(multipartFile);
            Assert.assertTrue(list.size() > 0);
        } catch (Exception e) {
        }

    }

    @Test
    public void getDataList() {
        List<PubDomesticFlightEntity> list = pubDomesticFlightServiceImp.getDataList("", "", 1, 0);
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void getHistory() throws Exception {
        List<PubDomesticFlightEntity> list = pubDomesticFlightServiceImp.getHistory(1);
        Assert.assertTrue(list.size() > 0);
    }
}