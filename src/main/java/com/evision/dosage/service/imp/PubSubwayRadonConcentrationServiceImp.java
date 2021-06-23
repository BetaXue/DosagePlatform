package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.SubwayRadonEnum;
import com.evision.dosage.mapper.PubSubwayRadonConcentrationMapper;
import com.evision.dosage.pojo.entity.PubSubwayRadonConcentrationEntity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wei Zhenning
 * @version 1.0
 * @date 2020/2/21 19:54
 */
@Service
@Slf4j
@DosageServiceConfig("pubSubwayRadonConcentration")
public class PubSubwayRadonConcentrationServiceImp extends ServiceImpl<PubSubwayRadonConcentrationMapper, PubSubwayRadonConcentrationEntity> implements DosageService {

    @Resource
    private PubSubwayRadonConcentrationMapper pubSubwayRadonConcentrationMapper;

    @Override
    @Transactional
    public List importExcel(MultipartFile multipartFile) {
        try {
            Workbook workbook = ExcelUtils.getWorkbook(multipartFile);
            Sheet sheet = workbook.getSheet("地铁氡浓度");
            int totalRows;
            int totalCells = 6;
            totalRows = sheet.getLastRowNum();
            List<PubSubwayRadonConcentrationEntity> radonList = new ArrayList<>();
            //获取excel中数据
            for (int i = 1; i <= totalRows; i++) {
                PubSubwayRadonConcentrationEntity pubSubwayRadonConcentrationEntity = new PubSubwayRadonConcentrationEntity();
                Row row = sheet.getRow(i);
                for (int j = 0; j <= totalCells; j++) {
                    Cell cell = row.getCell(j);
                    if (j == 0) {
                        pubSubwayRadonConcentrationEntity.setSubwayName(cell.getStringCellValue());
                    } else if (j == 1) {
                        pubSubwayRadonConcentrationEntity.setAvgRadonConcentration(new BigDecimal(cell.getNumericCellValue()));
                    } else if (j == 2) {
                        pubSubwayRadonConcentrationEntity.setUncertainty(new BigDecimal(cell.getNumericCellValue()));
                    }
                }
                pubSubwayRadonConcentrationEntity.setUserId(UserUtils.getCurrentUserId());
                pubSubwayRadonConcentrationEntity.setDeleted(0);
                pubSubwayRadonConcentrationEntity.setDisabled(1);
                radonList.add(pubSubwayRadonConcentrationEntity);
            }
            //判断数据库中是否存在excel中的记录
            List<PubSubwayRadonConcentrationEntity> oldList = pubSubwayRadonConcentrationMapper.selectAll(1, 0);
            for (PubSubwayRadonConcentrationEntity radonConcentrationEntity : radonList) {
                int i = 0;
                if (oldList.size() != 0) {
                    for (PubSubwayRadonConcentrationEntity p : oldList) {
                        //判断数据库中是否存在该记录，存在的话判断值是否有更改,有更改的更新状态disable为0再新增，无更改的无操作，旧列表有 新列表没有的删除，新列表有 旧列表没有的新增
                        if (radonConcentrationEntity.getSubwayName().equals(p.getSubwayName())) {
                            BigDecimal avgRadonConcentration = radonConcentrationEntity.getAvgRadonConcentration().setScale(1, BigDecimal.ROUND_HALF_UP);
                            BigDecimal uncertainty = radonConcentrationEntity.getUncertainty().setScale(1, BigDecimal.ROUND_HALF_UP);
                            if (avgRadonConcentration.compareTo(p.getAvgRadonConcentration()) == 0 && uncertainty.compareTo(p.getUncertainty()) == 0) {
                                i = i + 1;
                            } else {
                                p.setDisabled(0);
                                p.setDeleted(0);
                                p.setUpdateTime(LocalDateTime.now());
                                pubSubwayRadonConcentrationMapper.updateById(p);
                            }
                        }
                    }
                }
                if (i == 0) {
                    radonConcentrationEntity.setCreateTime(LocalDateTime.now());
                    radonConcentrationEntity.setUpdateTime(LocalDateTime.now());
                    pubSubwayRadonConcentrationMapper.insert(radonConcentrationEntity);
                }
            }
            if (oldList.size() > 0) {
                for (PubSubwayRadonConcentrationEntity old : oldList) {
                    int i = 0;
                    if (radonList.size() > 0) {
                        for (PubSubwayRadonConcentrationEntity entity : radonList) {
                            if (old.getSubwayName().equals(entity.getSubwayName())) {
                                i = i + 1;
                            }
                        }
                    }
                    if (i == 0) {
                        old.setUserId(UserUtils.getCurrentUserId());
                        old.setDeleted(1);
                        old.setUpdateTime(LocalDateTime.now());
                        pubSubwayRadonConcentrationMapper.updateById(old);
                    }
                }
            }
            return radonList;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) {
        List<PubSubwayRadonConcentrationEntity> pubSubwayRadonConcentrationEntities = new ArrayList<>();
        if (incloudDel == 0) {
            //不包括删除的数据
            pubSubwayRadonConcentrationEntities = pubSubwayRadonConcentrationMapper.selectAll(1, incloudDel);
        }
        if (incloudDel == 1) {
            //包括删除的数据
            pubSubwayRadonConcentrationEntities = pubSubwayRadonConcentrationMapper.selectList(new QueryWrapper<PubSubwayRadonConcentrationEntity>().eq("disabled", 1));
        }
        if (pubSubwayRadonConcentrationEntities.size() == 0) {
            return Lists.newArrayList();
        }
        BigDecimal totalAvgRadonConcentration = new BigDecimal(0);
        BigDecimal totalUncertainty = new BigDecimal(0);
        for (PubSubwayRadonConcentrationEntity p : pubSubwayRadonConcentrationEntities
        ) {
            totalAvgRadonConcentration = totalAvgRadonConcentration.add(p.getAvgRadonConcentration());
            totalUncertainty = totalUncertainty.add(p.getUncertainty());
        }
        PubSubwayRadonConcentrationEntity pubSubwayRadonConcentrationEntity = new PubSubwayRadonConcentrationEntity();
        pubSubwayRadonConcentrationEntity.setSubwayName("均值");
        pubSubwayRadonConcentrationEntity.setAvgRadonConcentration(averageCalculation(totalAvgRadonConcentration, pubSubwayRadonConcentrationEntities.size()));
        pubSubwayRadonConcentrationEntity.setUncertainty(averageCalculation(totalUncertainty, pubSubwayRadonConcentrationEntities.size()));
        pubSubwayRadonConcentrationEntities.add(pubSubwayRadonConcentrationEntity);
        return pubSubwayRadonConcentrationEntities;
    }

    @Override
    public List<DosageHeader> getHeader() {
        return null;
    }

    public List getHistory(int parameter) throws Exception {
        PubSubwayRadonConcentrationEntity entity = pubSubwayRadonConcentrationMapper.selectById(parameter);
        QueryWrapper<PubSubwayRadonConcentrationEntity> query = new QueryWrapper<PubSubwayRadonConcentrationEntity>();
        query.eq("subway_name", entity.getSubwayName())
                .eq("disabled", 0)
                .eq("deleted", 0);
        return pubSubwayRadonConcentrationMapper.selectList(query);
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return SubwayRadonEnum.convertDbHeaderEntity();
    }

    private BigDecimal averageCalculation(BigDecimal total, Integer size) {
        return total.divide(new BigDecimal(size), 1, BigDecimal.ROUND_HALF_UP);
    }
}
