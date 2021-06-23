package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.pubFlight.PubInternationalFlightHeader;
import com.evision.dosage.mapper.PubInternationalFlightMapper;
import com.evision.dosage.pojo.entity.PubInternationalFlightEntity;
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
 * @date 2020/2/21 18:42
 */
@Service
@Slf4j
@DosageServiceConfig("pubInternationalFlight")
public class PubInternationalFlightServiceImp extends ServiceImpl<PubInternationalFlightMapper, PubInternationalFlightEntity> implements DosageService<PubInternationalFlightEntity> {

    @Resource
    private PubInternationalFlightMapper pubInternationalFlightMapper;

    @Override
    @Transactional
    public List<PubInternationalFlightEntity> importExcel(MultipartFile multipartFile) {
        try {
            Workbook workbook = ExcelUtils.getWorkbook(multipartFile);
            Sheet sheet = workbook.getSheet("国际航班");
            int totalRows;
            int totalCells = 5;
            totalRows = sheet.getLastRowNum();
            List<PubInternationalFlightEntity> flighList = new ArrayList<>();
            //获取excel中数据
            for (int i = 1; i <= totalRows; i++) {
                PubInternationalFlightEntity pubInternationalFlightEntity = new PubInternationalFlightEntity();
                Row row = sheet.getRow(i);
                for (int j = 1; j <= totalCells; j++) {
                    Cell cell = row.getCell(j);
                    if (j == 1) {
                        pubInternationalFlightEntity.setDepartureAirport(cell.getStringCellValue());
                    } else if (j == 2) {
                        pubInternationalFlightEntity.setDestinationAirport(cell.getStringCellValue());
                    } else if (j == 3) {
                        pubInternationalFlightEntity.setFlyTime(Integer.parseInt(new BigDecimal(cell.getNumericCellValue()).toString()));
                    } else if (j == 4) {
                        pubInternationalFlightEntity.setEffectiveDose(new BigDecimal(cell.getNumericCellValue()));
                    } else if (j == 5) {
                        pubInternationalFlightEntity.setEffectiveDoseRate(new BigDecimal(cell.getNumericCellValue()));
                    }
                }
                pubInternationalFlightEntity.setUserId(UserUtils.getCurrentUserId());
                pubInternationalFlightEntity.setDeleted(0);
                pubInternationalFlightEntity.setDisabled(1);
                flighList.add(pubInternationalFlightEntity);
            }
            //判断数据库中是否存在excel中的记录
            List<PubInternationalFlightEntity> oldList = pubInternationalFlightMapper.selectAll(0);
            for (PubInternationalFlightEntity flightEntity : flighList) {
                int i = 0;
                if (oldList.size() != 0) {
                    for (PubInternationalFlightEntity p : oldList) {
                        //判断数据库中是否存在该记录，存在的话判断值是否有更改,有更改的更新状态disable为0再新增，无更改的无操作，旧列表有 新列表没有的删除，新列表有 旧列表没有的新增
                        if (flightEntity.getDepartureAirport().equals(p.getDepartureAirport()) && flightEntity.getDestinationAirport().equals(p.getDestinationAirport())) {
                            BigDecimal dose = flightEntity.getEffectiveDose().setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal doseRate = flightEntity.getEffectiveDoseRate().setScale(2, BigDecimal.ROUND_HALF_UP);
                            if (flightEntity.getFlyTime().intValue() == p.getFlyTime().intValue() && dose.compareTo(p.getEffectiveDose()) == 0 && doseRate.compareTo(p.getEffectiveDoseRate()) == 0) {
                                i = i + 1;
                            } else {
                                p.setDisabled(0);
                                p.setDeleted(0);
                                p.setUpdateTime(LocalDateTime.now());
                                pubInternationalFlightMapper.updateById(p);
                            }
                        }
                    }
                }
                if (i == 0) {
                    flightEntity.setCreateTime(LocalDateTime.now());
                    flightEntity.setUpdateTime(LocalDateTime.now());
                    pubInternationalFlightMapper.insert(flightEntity);
                }
            }
            if (oldList.size() > 0) {
                for (PubInternationalFlightEntity old : oldList) {
                    int i = 0;
                    if (flighList.size() > 0) {
                        for (PubInternationalFlightEntity entity : flighList) {
                            if (old.getDepartureAirport().equals(entity.getDepartureAirport()) && old.getDestinationAirport().equals(entity.getDestinationAirport())) {
                                i = i + 1;
                            }
                        }
                    }
                    if (i == 0) {
                        old.setUserId(UserUtils.getCurrentUserId());
                        old.setDeleted(1);
                        old.setUpdateTime(LocalDateTime.now());
                        pubInternationalFlightMapper.updateById(old);
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
    public List<PubInternationalFlightEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) {

        List<PubInternationalFlightEntity> pubInternationalFlightEntities = pubInternationalFlightMapper.selectAll(incloudDel);
        if (pubInternationalFlightEntities.size() == 0) {
            return Lists.newArrayList();
        }
        BigDecimal totalEffectiveDose = new BigDecimal(0);
        BigDecimal totalEffectiveDoseRate = new BigDecimal(0);
        for (PubInternationalFlightEntity p : pubInternationalFlightEntities
        ) {
            totalEffectiveDose = totalEffectiveDose.add(p.getEffectiveDose());
            totalEffectiveDoseRate = totalEffectiveDoseRate.add(p.getEffectiveDoseRate());
        }
        PubInternationalFlightEntity pubInternationalFlightEntity = new PubInternationalFlightEntity();
        pubInternationalFlightEntity.setDepartureAirport("均值");
        BigDecimal effectiveDose = averageCalculation(totalEffectiveDose, pubInternationalFlightEntities.size());
        pubInternationalFlightEntity.setEffectiveDose(effectiveDose);
        BigDecimal effectiveDoseRate = averageCalculation(totalEffectiveDoseRate, pubInternationalFlightEntities.size());
        pubInternationalFlightEntity.setEffectiveDoseRate(effectiveDoseRate);
        pubInternationalFlightEntities.add(pubInternationalFlightEntity);
        return pubInternationalFlightEntities;
    }

    @Override
    public List<DosageHeader> getHeader() {
        return null;
    }

    public List getHistory(int parameter) throws Exception {
        PubInternationalFlightEntity pubInternationalFlightEntity = pubInternationalFlightMapper.selectById(parameter);
        return pubInternationalFlightMapper.selectHistory(0, 0, pubInternationalFlightEntity.getDepartureAirport(), pubInternationalFlightEntity.getDestinationAirport());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return PubInternationalFlightHeader.getDosageDbHeader();
    }

    //航班平均值计算
    private BigDecimal averageCalculation(BigDecimal total, Integer size) {
        return total.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
    }
}
