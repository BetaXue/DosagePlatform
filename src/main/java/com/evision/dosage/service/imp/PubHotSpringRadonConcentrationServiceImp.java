package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.PubHotSpringRadonConcentrationHeaderEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.PubHotSpringRadonConcentrationMapper;
import com.evision.dosage.pojo.entity.PubHotSpringRadonConcentrationEntity;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wei Zhenning
 * @version 1.0
 * @date 2020/2/24 12:23
 */
@Service
@Slf4j
@DosageServiceConfig("pubHotSpringRadonConcentration")
public class PubHotSpringRadonConcentrationServiceImp extends ServiceImpl<PubHotSpringRadonConcentrationMapper, PubHotSpringRadonConcentrationEntity> implements DosageService<PubHotSpringRadonConcentrationEntity> {

    @Resource
    private PubHotSpringRadonConcentrationMapper pubHotSpringRadonConcentrationMapper;

    @Override
    @Transactional
    public List importExcel(MultipartFile multipartFile) {
        try {
            Workbook workbook = ExcelUtils.getWorkbook(multipartFile);
            Sheet sheet = workbook.getSheet("温泉设施氡浓度测量结果");
            int totalRows;
            int totalCells = 1;
            totalRows = sheet.getLastRowNum();
            List<PubHotSpringRadonConcentrationEntity> hotSpringList = new ArrayList<>();
            //获取excel中数据
            for (int i = 1; i <= totalRows; i++) {
                PubHotSpringRadonConcentrationEntity pubHotSpringRadonConcentrationEntity = new PubHotSpringRadonConcentrationEntity();
                Row row = sheet.getRow(i);
                for (int j = 0; j <= totalCells; j++) {
                    Cell cell = row.getCell(j);
                    if (j == 0) {
                        pubHotSpringRadonConcentrationEntity.setHotSpring(cell.getStringCellValue());
                    } else if (j == 1) {
                        pubHotSpringRadonConcentrationEntity.setHotSpringRadonConcentration(cell.getStringCellValue());
                    }
                }
                pubHotSpringRadonConcentrationEntity.setUserId(UserUtils.getCurrentUserId());
                pubHotSpringRadonConcentrationEntity.setDeleted(0);
                pubHotSpringRadonConcentrationEntity.setDisabled(1);
                hotSpringList.add(pubHotSpringRadonConcentrationEntity);
            }
            //判断数据库中是否存在excel中的记录
            List<PubHotSpringRadonConcentrationEntity> oldList = pubHotSpringRadonConcentrationMapper.selectAll(1, 0);

            for (PubHotSpringRadonConcentrationEntity hotSpringRadonConcentrationEntity : hotSpringList) {
                int i = 0;
                if (oldList.size() != 0) {
                    for (PubHotSpringRadonConcentrationEntity p : oldList) {
                        if (hotSpringRadonConcentrationEntity.getHotSpring().equals(p.getHotSpring())) {
                            if (hotSpringRadonConcentrationEntity.getHotSpringRadonConcentration().equals(p.getHotSpringRadonConcentration())) {
                                i = i + 1;
                            } else {
                                p.setDisabled(0);
                                p.setDeleted(0);
                                p.setUpdateTime(LocalDateTime.now());
                                pubHotSpringRadonConcentrationMapper.updateById(p);
                            }
                        }
                    }
                }
                if (i == 0) {
                    hotSpringRadonConcentrationEntity.setCreateTime(LocalDateTime.now());
                    hotSpringRadonConcentrationEntity.setUpdateTime(LocalDateTime.now());
                    pubHotSpringRadonConcentrationMapper.insert(hotSpringRadonConcentrationEntity);
                }
            }
            if (oldList.size() > 0) {
                for (PubHotSpringRadonConcentrationEntity old : oldList) {
                    int i = 0;
                    System.out.println(old.getId());
                    if (hotSpringList.size() > 0) {
                        for (PubHotSpringRadonConcentrationEntity entity : hotSpringList) {
                            if (old.getHotSpring().equals(entity.getHotSpring())) {
                                i = i + 1;
                            }
                        }
                    }
                    if (i == 0) {
                        old.setUserId(UserUtils.getCurrentUserId());
                        old.setDeleted(1);
                        old.setUpdateTime(LocalDateTime.now());
                        pubHotSpringRadonConcentrationMapper.updateById(old);
                    }
                }
            }

            return hotSpringList;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<PubHotSpringRadonConcentrationEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) {
        List<PubHotSpringRadonConcentrationEntity> entities = pubHotSpringRadonConcentrationMapper.selectAll(1, incloudDel);
        if (entities.size() == 0) {
            return Lists.newArrayList();
        }
        BigDecimal total = new BigDecimal(0);
        BigDecimal totalAvg = new BigDecimal(0);
        for (PubHotSpringRadonConcentrationEntity p : entities
        ) {
            String[] hotSpringRadonConcentration = p.getHotSpringRadonConcentration().split("±");
            if (hotSpringRadonConcentration.length > 0) {
                total = total.add(new BigDecimal(hotSpringRadonConcentration[0]));
                totalAvg = totalAvg.add(new BigDecimal(hotSpringRadonConcentration[1]).multiply(new BigDecimal(hotSpringRadonConcentration[1])));
            }
        }
        double x = totalAvg.doubleValue();
        double y = Math.sqrt(x);
        BigDecimal n = new BigDecimal(y).setScale(1, BigDecimal.ROUND_HALF_UP);
        String avgRadon = averageCalculation(total, entities.size()).toString() + "±" + n.toString();
        PubHotSpringRadonConcentrationEntity pubHotSpringRadonConcentrationEntity = new PubHotSpringRadonConcentrationEntity();
        pubHotSpringRadonConcentrationEntity.setHotSpringRadonConcentration(avgRadon);
        pubHotSpringRadonConcentrationEntity.setHotSpring("MEAN±SD");
        entities.add(pubHotSpringRadonConcentrationEntity);
        return entities;
    }

    @Override
    public List<DosageHeader> getHeader() {
        return PubHotSpringRadonConcentrationHeaderEnum.convertHeaderEntity();
    }

    public List getHistory(int parameter) throws Exception {
        List<PubHotSpringRadonConcentrationEntity> entities = pubHotSpringRadonConcentrationMapper.getDataById(parameter);
        if (CollectionUtils.isEmpty(entities)) {
            throw new DosageException(String.format("ID: %d 无对应记录", parameter));
        }
        PubHotSpringRadonConcentrationEntity entity = entities.get(0);
        return pubHotSpringRadonConcentrationMapper.selectHistory(0, 0, entity.getHotSpring());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return PubHotSpringRadonConcentrationHeaderEnum.convertDbHeaderEntity();
    }

    private BigDecimal averageCalculation(BigDecimal total, Integer size) {
        return total.divide(new BigDecimal(size), 1, BigDecimal.ROUND_HALF_UP);
    }
}
