package com.evision.dosage.service.vehicle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.pojo.entity.user.UserEntity;
import com.evision.dosage.pojo.entity.vehicle.VehicleDosageEntity;
import com.evision.dosage.pojo.entity.vehicle.VehicleDosageRateEntity;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.service.vehicle.imp.VehicleDosageServiceImpl;
import com.evision.dosage.utils.ExcelUtils;
import com.evision.dosage.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/25 15:55
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class VehicleDosageServiceTest {
    @Resource
    VehicleDosageServiceImpl service;
    @Before
    public void setUser(){
        UserEntity user = new UserEntity();
        user.setId(1);
        UserUtils.setCurrentUser(user);
    }
    @Test
    public void testProcessExcel() throws Exception{
        File originFile = ResourceUtils.getFile("classpath:车辆γ剂量-5张表-导入模板.xlsx");
        processFile(originFile);
        Thread.sleep(1100);
        File changeFile = ResourceUtils.getFile("classpath:车辆γ剂量-5张表-导入模板-修改.xlsx");
        processFile(changeFile);
    }

    private void processFile(File file) throws Exception{
        Workbook workBook = ExcelUtils.getWorkbook(file);
        Sheet sheet = ExcelUtils.getSheet(workBook, DosageExcelEnum.VEHICLE_DOSAGE);
        List<VehicleDosageEntity> ret = service.processExcel(sheet);
        Assert.assertTrue(ret.size() > 0);
    }

    @Test
    public void testGetHistory() throws Exception{
        String one = "公交车车厢γ剂量率监测数据";
        String two = "456路";
        List<VehicleDosageEntity> ret = service.getHistory(one, two, "");
        Assert.assertTrue(ret.size() > 0);
    }

    @Test
    public void testGetHeader() throws Exception{
        List<DosageHeader> ret = service.getHeader();
        Assert.assertTrue(ret.size() > 0);
    }

    @Test
    public void testGetDataList() throws Exception{
        IPage<VehicleDosageEntity> ret = service.getDataList("", 1, 1, "", 1, 0);
        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.getRecords().size() > 0);
    }
}
