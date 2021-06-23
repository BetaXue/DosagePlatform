package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.pubFlight.PubDomesticFlightHeader;
import com.evision.dosage.mapper.PubDomesticFlightMapper;
import com.evision.dosage.pojo.entity.PubDomesticFlightEntity;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wei Zhenning
 * @version 1.0
 * @date 2020/2/21 19:34
 */
@Service
@Slf4j
@DosageServiceConfig("pubDomesticFlight")
public class PubDomesticFlightServiceImp extends ServiceImpl<PubDomesticFlightMapper, PubDomesticFlightEntity> implements DosageService<PubDomesticFlightEntity> {

    @Resource
    private PubDomesticFlightMapper pubDomesticFlightMapper;

    @Override
    @Transactional
    public List importExcel(MultipartFile multipartFile) {
        try {
            Workbook workbook = ExcelUtils.getWorkbook(multipartFile);
            Sheet sheet = workbook.getSheet("国内航班");
            int totalRows;
            int totalCells = 6;
            totalRows = sheet.getLastRowNum();
            System.out.println(totalRows);
            List<PubDomesticFlightEntity> flighList = new ArrayList<>();
            //获取excel中数据
            for (int i = 1; i <= totalRows; i++) {
                PubDomesticFlightEntity pubDomesticFlightEntity = new PubDomesticFlightEntity();
                Row row = sheet.getRow(i);
                for (int j = 1; j <= totalCells; j++) {
                    Cell cell = row.getCell(j);
                    if (j == 1) {
                        pubDomesticFlightEntity.setDepartureAirport(cell.getStringCellValue());
                    } else if (j == 2) {
                        pubDomesticFlightEntity.setDestinationAirport(cell.getStringCellValue());
                    } else if (j == 3) {
                        pubDomesticFlightEntity.setAvgFly(new BigDecimal(cell.getNumericCellValue()));
                    } else if (j == 4) {
                        pubDomesticFlightEntity.setFlyTime(Integer.parseInt(new BigDecimal(cell.getNumericCellValue()).toString()));
                    } else if (j == 5) {
                        pubDomesticFlightEntity.setEffectiveDose(new BigDecimal(cell.getNumericCellValue()));
                    } else if (j == 6) {
                        pubDomesticFlightEntity.setEffectiveDoseRate(new BigDecimal(cell.getNumericCellValue()));
                    }
                }
                pubDomesticFlightEntity.setUserId(UserUtils.getCurrentUserId());
                pubDomesticFlightEntity.setDeleted(0);
                pubDomesticFlightEntity.setDisabled(1);
                flighList.add(pubDomesticFlightEntity);
            }
            //判断数据库中是否存在excel中的记录
            List<PubDomesticFlightEntity> oldList = pubDomesticFlightMapper.selectAll(0);

            for (PubDomesticFlightEntity flightEntity : flighList) {
                int i = 0;
                if (oldList.size() != 0) {
                    for (PubDomesticFlightEntity p : oldList) {
                        //判断数据库中是否存在该记录，存在的话判断值是否有更改,有更改的更新状态disable为0再新增，无更改的无操作，旧列表有 新列表没有的删除，新列表有 旧列表没有的新增
                        if (flightEntity.getDepartureAirport().equals(p.getDepartureAirport()) && flightEntity.getDestinationAirport().equals(p.getDestinationAirport())) {
                            BigDecimal avgFly = flightEntity.getAvgFly().setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal dose = flightEntity.getEffectiveDose().setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal doseRate = flightEntity.getEffectiveDoseRate().setScale(2, BigDecimal.ROUND_HALF_UP);
                            if (avgFly.compareTo(p.getAvgFly()) == 0 && flightEntity.getFlyTime().intValue() == p.getFlyTime().intValue() && dose.compareTo(p.getEffectiveDose()) == 0 && doseRate.compareTo(p.getEffectiveDoseRate()) == 0) {
                                i = i + 1;
                            } else {
                                p.setDisabled(0);
                                p.setDeleted(0);
                                p.setUpdateTime(LocalDateTime.now());
                                pubDomesticFlightMapper.updateById(p);
                            }
                        }
                    }
                }
                if (i == 0) {
                    flightEntity.setCreateTime(LocalDateTime.now());
                    flightEntity.setUpdateTime(LocalDateTime.now());
                    pubDomesticFlightMapper.insert(flightEntity);
                }
            }
            if (oldList.size() > 0) {
                for (PubDomesticFlightEntity old : oldList) {
                    int i = 0;
                    if (flighList.size() > 0) {
                        for (PubDomesticFlightEntity entity : flighList) {
                            if (old.getDepartureAirport().equals(entity.getDepartureAirport()) && old.getDestinationAirport().equals(entity.getDestinationAirport())) {
                                i = i + 1;
                            }
                        }
                    }
                    if (i == 0) {
                        old.setUserId(UserUtils.getCurrentUserId());
                        old.setDeleted(1);
                        old.setUpdateTime(LocalDateTime.now());
                        pubDomesticFlightMapper.updateById(old);
                    }
                }
            }
            return flighList;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public List<PubDomesticFlightEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer
            incloudDel) {
        List<PubDomesticFlightEntity> pubDomesticFlightEntities = pubDomesticFlightMapper.selectAll(incloudDel);
        if (pubDomesticFlightEntities.size() == 0) {
            return Lists.newArrayList();
        }
        BigDecimal totalEffectiveDose = new BigDecimal(0);
        BigDecimal totalEffectiveDoseRate = new BigDecimal(0);
        for (PubDomesticFlightEntity p : pubDomesticFlightEntities
        ) {
            totalEffectiveDose = totalEffectiveDose.add(p.getEffectiveDose());
            totalEffectiveDoseRate = totalEffectiveDoseRate.add(p.getEffectiveDoseRate());
        }
        PubDomesticFlightEntity pubDomesticFlightEntity = new PubDomesticFlightEntity();
        pubDomesticFlightEntity.setDepartureAirport("航班日频加权均值");
        BigDecimal effectiveDose = averageCalculation(totalEffectiveDose, pubDomesticFlightEntities.size());
        pubDomesticFlightEntity.setEffectiveDose(effectiveDose);
        BigDecimal effectiveDoseRate = averageCalculation(totalEffectiveDoseRate, pubDomesticFlightEntities.size());
        pubDomesticFlightEntity.setEffectiveDoseRate(effectiveDoseRate);
        pubDomesticFlightEntities.add(pubDomesticFlightEntity);
        return pubDomesticFlightEntities;
    }

    @Override
    public List<DosageHeader> getHeader() {
        return null;
    }

    public List getHistory(int parameter) throws Exception {
        PubDomesticFlightEntity pubDomesticFlightEntity = pubDomesticFlightMapper.selectById(parameter);
        return pubDomesticFlightMapper.selectHistory(0, 0, pubDomesticFlightEntity.getDepartureAirport(), pubDomesticFlightEntity.getDestinationAirport());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return PubDomesticFlightHeader.getDosageDbHeader();
    }

    //航班平均值计算
    private BigDecimal averageCalculation(BigDecimal total, Integer size) {
        return total.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
    }
}
