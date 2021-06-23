package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.populcationDose.PopulationDoseHeader;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.PubIntegratedLevelMapper;
import com.evision.dosage.pojo.entity.PubIntegratedLevel;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.service.DosageService;
import com.evision.dosage.utils.ExcelUtils;
import com.evision.dosage.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wei Zhenning
 * @version 1.0
 * @date 2020/2/21 17:29
 */
@Service
@Slf4j
//@DosageServiceConfig("pubIntegratedLevel")
public class PubIntegratedLevelServiceImp extends ServiceImpl<PubIntegratedLevelMapper, PubIntegratedLevel> implements DosageService {

    @Resource
    private PubIntegratedLevelMapper pubIntegratedLevelMapper;

    @Override
    @Transactional
    public List<PubIntegratedLevel> importExcel(MultipartFile multipartFile) {
        List<PubIntegratedLevel> levelList = new ArrayList<>();
        try {
            Workbook workbook = ExcelUtils.getWorkbook(multipartFile);
            Sheet sheet = workbook.getSheet("公众照射");
            int totalRows;
            int totalCells = 5;
            totalRows = sheet.getLastRowNum();

            //获取excel中数据
            for (int i = 1; i < totalRows; i++) {
                System.out.println(i);
                PubIntegratedLevel pubIntegratedLevel = new PubIntegratedLevel();
                Row row = sheet.getRow(i);
                log.info(row.getCell(0).getStringCellValue());
                for (int j = 2; j < totalCells; j++) {
                    Cell cell = row.getCell(j);
                    BigDecimal cellValue = getCellValue(cell, j);
                    if (j == 2) {
                        pubIntegratedLevel.setAnnualMeasurement(cellValue);
                    } else if (j == 3) {
                        pubIntegratedLevel.setProportion(cellValue);
                    } else {
                        pubIntegratedLevel.setCollectiveDosage(cellValue);
                    }
                }
                pubIntegratedLevel.setOrders(i);
                //todo 获取user_id
                //pubIntegratedLevel.setUserId(UserUtils.getCurrentUserId());
                pubIntegratedLevel.setUserId(UserUtils.getCurrentUserId());
                pubIntegratedLevel.setDeleted(0);
                pubIntegratedLevel.setDisabled(1);
                pubIntegratedLevel.setCreateTime(LocalDateTime.now());
                pubIntegratedLevel.setUpdateTime(LocalDateTime.now());
                levelList.add(pubIntegratedLevel);
            }
            //判断数据库中是否存在excel中的记录
            List<PubIntegratedLevel> oldLevelList = pubIntegratedLevelMapper.selectAll(0);
            if (oldLevelList.size() != 0) {
                for (PubIntegratedLevel level : levelList) {
                    for (PubIntegratedLevel p : oldLevelList) {
                        if (p.getOrders() == level.getOrders()) {
                            //todo 获取userid
                            //pubIntegratedLevel.setUserId(UserUtils.getCurrentUserId());
                            p.setUserId(UserUtils.getCurrentUserId());
                            p.setDisabled(0);
                            p.setDeleted(0);
                            p.setCreateTime(LocalDateTime.now());
                            p.setUpdateTime(LocalDateTime.now());
                            pubIntegratedLevelMapper.updateById(p);
                        }
                    }
                }
            }

            for (PubIntegratedLevel level : levelList) {
                level.setCreateTime(LocalDateTime.now());
                level.setUpdateTime(LocalDateTime.now());
                pubIntegratedLevelMapper.insert(level);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        return levelList;
    }

    private BigDecimal getCellValue(Cell cell, Integer index) {
        BigDecimal cellValue = new BigDecimal(0.0000);
        if (StringUtils.isEmpty(cell)) {
            return cellValue;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                cellValue = new BigDecimal(cell.getNumericCellValue()).setScale(4, BigDecimal.ROUND_HALF_UP);
                break;
            case STRING:
                cellValue = new BigDecimal(cell.getStringCellValue()).setScale(4, BigDecimal.ROUND_HALF_UP);
                break;
            case FORMULA:
                if (index == 3) {
                    cellValue = new BigDecimal(cell.getNumericCellValue() * 100).setScale(4, BigDecimal.ROUND_HALF_UP);
                } else {
                    cellValue = new BigDecimal(cell.getNumericCellValue()).setScale(4, BigDecimal.ROUND_HALF_UP);
                }
                break;
        }
        return cellValue;
    }

    @Override
    public List<PubIntegratedLevel> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) {
        try {
            List<PubIntegratedLevel> list = pubIntegratedLevelMapper.selectAll(incloudDel);
            if (list.size() == 0) {
                return Lists.newArrayList();
            }
            BigDecimal totalAnnualMeasurement = new BigDecimal(0.0000);
            BigDecimal totalProportion = new BigDecimal(0.0000);
            BigDecimal totalCollectiveDosage = new BigDecimal(0.0000);
            for (PubIntegratedLevel p : list
            ) {
                totalAnnualMeasurement = totalAnnualMeasurement.add(p.getAnnualMeasurement().setScale(4, BigDecimal.ROUND_HALF_UP));
                totalProportion = totalProportion.add(p.getProportion()).setScale(4, BigDecimal.ROUND_HALF_UP);
                totalCollectiveDosage = totalCollectiveDosage.add(p.getCollectiveDosage().setScale(4, BigDecimal.ROUND_HALF_UP));
            }

            if (totalProportion.compareTo(new BigDecimal(100)) > -1) {
                totalProportion = new BigDecimal(100.0000);
            }
            PubIntegratedLevel pubIntegratedLevel = new PubIntegratedLevel();
            pubIntegratedLevel.setCollectiveDosage(totalCollectiveDosage);
            pubIntegratedLevel.setAnnualMeasurement(totalAnnualMeasurement);
            pubIntegratedLevel.setProportion(totalProportion.setScale(4, BigDecimal.ROUND_HALF_UP));
            pubIntegratedLevel.setOrders(15);
            return list;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    @Override
    public List<DosageHeader> getHeader() {
        return PopulationDoseHeader.convertHeaderEntity();
    }

    public List<PubIntegratedLevel> getHistory(int parameter) throws Exception {
        List<PubIntegratedLevel> entities = pubIntegratedLevelMapper.getDataById(parameter);
        if (CollectionUtils.isEmpty(entities)) {
            throw new DosageException(String.format("ID: %d 无对应记录", parameter));
        }
        PubIntegratedLevel entity = entities.get(0);
        return pubIntegratedLevelMapper.selectHistory(0, 0, entity.getOrders());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return PopulationDoseHeader.convertDbHeaderEntity();
    }
}
