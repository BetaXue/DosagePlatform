package com.evision.dosage.service.vehicle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.pojo.entity.user.UserEntity;
import com.evision.dosage.pojo.entity.vehicle.MetroTransferStationDosageEntity;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.service.vehicle.imp.MetroTransferStationDosageServiceImpl;
import com.evision.dosage.utils.ExcelUtils;
import com.evision.dosage.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/25 15:55
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class MetroTransferStationDosageServiceTest {
    @Resource
    MetroTransferStationDosageServiceImpl service;
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
        Sheet sheet = ExcelUtils.getSheet(workBook, DosageExcelEnum.METRO_TRANSFER_STATION_DOSAGE);
        List<MetroTransferStationDosageEntity> ret = service.processExcel(sheet);
        Assert.assertTrue(ret.size() > 0);
    }

    @Test
    public void testGetHistory() throws Exception{
        String one = "京港地铁需地铁换乘站累积剂量率测量结果";
        String two = "大望路14号线E口";
        List<MetroTransferStationDosageEntity> ret = service.getHistory(one, two, "");
        Assert.assertTrue(ret.size() > 0);
    }

    @Test
    public void testGetHeader() throws Exception{
        List<DosageHeader> ret = service.getHeader();
        Assert.assertTrue(ret.size() > 0);
    }

    @Test
    public void testGetDataList() throws Exception{
        IPage<MetroTransferStationDosageEntity> ret = service.getDataList("", 1, 1, "", 1, 0);
        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.getRecords().size() > 0);
    }

    @Test
    public void changeValues() throws Exception{
        File originFile = new File("E:\\WorkFiles\\国民剂量监测评估系统\\系统导入模板(2)\\系统导入模板\\excel模板\\公众照射\\地铁换乘站累积剂量率测量结果.xlsx");
        Workbook workBook = ExcelUtils.getWorkbook(originFile);
        Sheet sheet = ExcelUtils.getSheet(workBook, DosageExcelEnum.METRO_TRANSFER_STATION_DOSAGE);
        List<Integer> adds = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        String group = "";
        for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            Cell labelCell = row.getCell(1);
            String nowGroup = ExcelUtils.getStringValue(labelCell);

            Cell changeCell = row.getCell(2);
            String originValue = ExcelUtils.getStringValue(changeCell);
            if (group.equals(nowGroup)) {
                if (labels.contains(originValue)) {
                    int index = labels.indexOf(originValue);
                    int add = adds.get(index) + 1;
                    changeCell.setCellValue(originValue + add);
                    adds.set(index, add);
                } else {
                    labels.add(originValue);
                    adds.add(0);
                }
            }else{
                labels = new ArrayList<>();
                adds = new ArrayList<>();
                labels.add(originValue);
                adds.add(0);
                group = nowGroup;
            }
        }
        FileOutputStream fos = new FileOutputStream("E:\\WorkFiles\\国民剂量监测评估系统\\系统导入模板(2)\\系统导入模板\\excel模板\\公众照射\\newExcel.xls");
        workBook.write(fos);
        fos.close();
        workBook.close();
    }
}
