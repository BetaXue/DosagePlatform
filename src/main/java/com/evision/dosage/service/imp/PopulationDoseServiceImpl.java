package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.constant.PopulationDoseConstant;
import com.evision.dosage.constant.populcationDose.PopulationDoseHeader;
import com.evision.dosage.mapper.PopulationDoseMapper;
import com.evision.dosage.pojo.entity.PopulationDoseEntity;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.service.PopulationDoseService;
import com.evision.dosage.utils.BigDecimalUtils;
import com.evision.dosage.utils.ObjectUtils;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 全民剂量首页业务层
 *
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-19 14:46
 */
@Slf4j
@Service
@DosageServiceConfig("populationDose")
public class PopulationDoseServiceImpl extends ServiceImpl<PopulationDoseMapper, PopulationDoseEntity> implements PopulationDoseService {

    @Override
    public List<PopulationDoseEntity> importExcel(MultipartFile multipartFile) throws Exception {
        List<PopulationDoseEntity> successList = Lists.newArrayList();
        List<Map<String, Object>> errorList = Lists.newArrayList();
        try {
            List<Integer> lineNum = Arrays.asList(new Integer[]{17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33});
            ExcelKit.$Import(PopulationDoseEntity.class)
                    .readXlsx(multipartFile.getInputStream(), new ExcelReadHandler<PopulationDoseEntity>() {
                        @Override
                        public void onSuccess(int sheetIndex, int rowIndex, PopulationDoseEntity entity) {
                            // 单行读取成功，加入入库队列。
                            if (lineNum.contains(rowIndex)) {
                                entity.setAnnualEffectiveDose(BigDecimalUtils.convertNum(entity.getAnnualEffectiveDose(), 2));
                            } else {
                                entity.setAnnualEffectiveDose(BigDecimalUtils.convertNum(entity.getAnnualEffectiveDose(), 0));
                            }
                            entity.setSubItemRatio(BigDecimalUtils.convertNumRatio(entity.getSubItemRatio()));
                            entity.setTotalDoseRatio(BigDecimalUtils.convertNumRatio(entity.getTotalDoseRatio()));
                            entity.setFistName(null);
                            entity.setSecondName(null);
                            entity.setThreeName(null);
                            if (entity.getSubItemRatio() != null && !"".equals(entity.getSubItemRatio())) {
                                entity.setSubItemRatio(entity.getSubItemRatio() + "%");
                            }
                            if (entity.getTotalDoseRatio() != null && !"".equals(entity.getTotalDoseRatio())) {
                                entity.setTotalDoseRatio(entity.getTotalDoseRatio() + "%");
                            }
                            entity.setCategory(PopulationDoseConstant.getIndexValue(rowIndex));
                            successList.add(entity);
                        }

                        @Override
                        public void onError(int sheetIndex, int rowIndex, List<ExcelErrorField> errorFields) {

                        }
                    });

        } catch (Exception e) {

        }
        dataFilter(successList);
        return successList;
    }

    /**
     * 数据过滤入库
     *
     * @param successList excel解析的数据
     */
    private void dataFilter(List<PopulationDoseEntity> successList) {
        // 入库
        for (int i = 0; i < successList.size(); i++) {
            PopulationDoseEntity populationDoseEntity = successList.get(i);
            PopulationDoseEntity oldPopulation = this.baseMapper.selectByCategory(populationDoseEntity.getCategory());
            if (oldPopulation == null) {
                populationDoseEntity.setDisabled(1);
                populationDoseEntity.setDeleted(0);
                populationDoseEntity.setOrders(i+1);
                populationDoseEntity.setCreateTime(LocalDateTime.now());
                populationDoseEntity.setUpdateTime(LocalDateTime.now());
                this.baseMapper.insert(populationDoseEntity);
            } else {
                populationDoseEntity.setId(oldPopulation.getId());
                boolean flag = ObjectUtils.compareObject(oldPopulation, populationDoseEntity);
                log.info("Old实体:{},new实体：{} -> {}", oldPopulation, populationDoseEntity, flag);
                if (flag) {
                    oldPopulation.setDisabled(0);
                    oldPopulation.setUpdateTime(LocalDateTime.now());
                    this.baseMapper.updateById(oldPopulation);
                    populationDoseEntity.setId(null);
                    populationDoseEntity.setDisabled(1);
                    populationDoseEntity.setDeleted(0);
                    populationDoseEntity.setOrders(oldPopulation.getOrders());
                    populationDoseEntity.setCreateTime(LocalDateTime.now());
                    populationDoseEntity.setUpdateTime(LocalDateTime.now());
                    this.baseMapper.insert(populationDoseEntity);
                }
            }

        }
    }

    @Override
    public List<PopulationDoseEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) {
        return this.baseMapper.getAllPopulationDose(null, incloudDel);
    }

    @Override
    public List<DosageHeader> getHeader() {
        return PopulationDoseHeader.convertHeaderEntity();
    }

    public List<PopulationDoseEntity> getHistory(int parameter) throws Exception {
        PopulationDoseEntity populationDoseEntity = this.baseMapper.selectById(parameter);
        return this.baseMapper.getHistory(populationDoseEntity.getCategory());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return null;
    }
}
