package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.constant.FoodWaterRadionuclideEnum;
import com.evision.dosage.constant.NaturalRadionuclideEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.NaturalRadionuclideMapper;
import com.evision.dosage.pojo.entity.FoodWaterRadionuclideEntity;
import com.evision.dosage.pojo.entity.NaturalRadionuclideEntity;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.NaturalRadionuclideService;
import com.evision.dosage.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 天然放射性核素活度浓度
 *
 * @Author: kangwenxuan
 * @Date: 2020/2/21 18:43
 */
@Slf4j
@Service
@DosageServiceConfig("natural")
public class NaturalRadionuclideServiceImpl extends ServiceImpl<NaturalRadionuclideMapper, NaturalRadionuclideEntity> implements NaturalRadionuclideService {
    @Resource
    private NaturalRadionuclideMapper naturalRadionuclideMapper;

    /***
     * 检查表头名称
     * @param sheet
     * @return
     */
    private boolean validateRowName(Sheet sheet) {
        Row row = sheet.getRow(0);
        NaturalRadionuclideEnum[] values = NaturalRadionuclideEnum.values();
        //列数不同
        if (row.getPhysicalNumberOfCells() != values.length - 1) {
            return false;
        }
        int rowNum = 0;
        for (NaturalRadionuclideEnum value : values) {
            if (value.getEntityName().equals("updateTime")) {
                continue;
            }
            //列名不同
            if (!value.getFieldName().equals(row.getCell(rowNum).getStringCellValue())) {
                return false;
            }
            rowNum++;
        }
        return true;
    }

    /**
     * 处理读取的数据到数据库
     *
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean dealData(List<NaturalRadionuclideEntity> list) throws Exception {
        //已删除的展示在维护列表中，查看记录按createTime主键disabled=0
        if (!CollectionUtils.isEmpty(list)) {
            List<String> numberList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                NaturalRadionuclideEntity naturalRadionuclideEntity = list.get(i);
                naturalRadionuclideEntity.setOrders(i + 1);
                String name = naturalRadionuclideEntity.getName();
                numberList.add(name);
                //查找有效的样本
                QueryWrapper<NaturalRadionuclideEntity> query = new QueryWrapper<NaturalRadionuclideEntity>();
                query.eq("name", name).eq("disabled", 1).eq("deleted", 0);
                List<NaturalRadionuclideEntity> existsEntityList = naturalRadionuclideMapper.selectList(query);
                if (CollectionUtils.isEmpty(existsEntityList)) {
                    //新增
                    naturalRadionuclideEntity.setCreateTime(LocalDateTime.now());
                    naturalRadionuclideEntity.setUpdateTime(LocalDateTime.now());
                    naturalRadionuclideMapper.insert(naturalRadionuclideEntity);
                } else {
                    //数据不一致
                    NaturalRadionuclideEntity existsEntity = existsEntityList.get(0);
                    if (!naturalRadionuclideEntity.equals(existsEntity)) {
                        //将旧记录设为禁用状态
                        existsEntity.setDisabled(0);
                        naturalRadionuclideMapper.updateById(existsEntity);
                        //新增
                        naturalRadionuclideEntity.setCreateTime(LocalDateTime.now());
                        naturalRadionuclideEntity.setUpdateTime(LocalDateTime.now());
                        naturalRadionuclideMapper.insert(naturalRadionuclideEntity);
                    }
                }
            }
            List<NaturalRadionuclideEntity> existsEntityDb = this.getDataList(null, null, null, 0);
            if (!CollectionUtils.isEmpty(existsEntityDb)) {
                for (NaturalRadionuclideEntity entityDb : existsEntityDb) {
                    if (!numberList.contains(entityDb.getName())) {
                        entityDb.setDeleted(1);
                        naturalRadionuclideMapper.updateById(entityDb);
                    }
                }
            }
        }
        return true;
    }


    @Override
    public boolean saveBatch(Collection<NaturalRadionuclideEntity> entityList) {
        return false;
    }

    @Override
    public List<NaturalRadionuclideEntity> importExcel(MultipartFile multipartFile) throws Exception {
        List<NaturalRadionuclideEntity> response = new ArrayList<>();
        try {
            Workbook workbook = new ExcelUtils().getWorkbook(multipartFile);
            Sheet sheet = workbook.getSheetAt(0);
            String sheetName = sheet.getSheetName();
            String fileName = multipartFile.getName();

            if (DosageExcelEnum.NATURAL_RADIONUCLIDE.getExcelName().equals(fileName) ||
                    DosageExcelEnum.NATURAL_RADIONUCLIDE.getExcelName().equals(sheetName)) {
                //检查行名
                if (!validateRowName(sheet)) {
                    throw new IllegalAccessException("excel表列数不同或者列名不同");
                }
                List<String> cellKeys = new ArrayList<>();
                for (NaturalRadionuclideEnum value : NaturalRadionuclideEnum.values()) {
                    cellKeys.add(value.getEntityName());
                }
                List<NaturalRadionuclideEntity> result = ExcelUtils.readExcel(sheet, NaturalRadionuclideEntity.class, cellKeys, 1, 0, 8);
                // 数据写库
                if (dealData(result)) {
                    log.info("excel sheet名称: " + sheetName + "上传成功！");
                    response.addAll(result);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("天然放射性核素活度浓度 error: " + e.toString());
            throw new DosageException("导入失败，请检查模板！");
        }
        return response;
    }

    @Override
    public List<NaturalRadionuclideEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) throws Exception {
        QueryWrapper<NaturalRadionuclideEntity> query = new QueryWrapper<NaturalRadionuclideEntity>();
        query.eq("disabled", 1);
        //模糊查询
        if (!StringUtils.isEmpty(queryName)) {
            query.like("name", queryName);
        }
        if (incloudDel == null || incloudDel == 0) {
            query.eq("deleted", incloudDel);
        } else {
            query.or().eq("deleted", incloudDel);
        }
        //排序
        String defaultSortName = "orders";
        if (!StringUtils.isEmpty(sortName)) {
            defaultSortName = NaturalRadionuclideEnum.getDbFieldName(sortName);
            defaultSortName = "ABS(" + defaultSortName + ")";
        }
        boolean defaultIsAsc = true;
        if (isAsc != null && isAsc == 0) {
            defaultIsAsc = false;
        }
        query.orderBy(true, defaultIsAsc, defaultSortName);
        List<NaturalRadionuclideEntity> naturalRadionuclideEntities = naturalRadionuclideMapper.selectList(query);
        NaturalRadionuclideEntity naturalRadionuclideEntityTotal = null;
        for (int i = 0; i < naturalRadionuclideEntities.size(); i++) {
            NaturalRadionuclideEntity naturalRadionuclideEntity = naturalRadionuclideEntities.get(i);
            if ("合计".equals(naturalRadionuclideEntity.getName())) {
                naturalRadionuclideEntities.remove(i);
                naturalRadionuclideEntityTotal = naturalRadionuclideEntity;
            }
        }
        if (naturalRadionuclideEntityTotal!=null){
            naturalRadionuclideEntities.add(naturalRadionuclideEntityTotal);
        }
        return naturalRadionuclideEntities;
    }

    @Override
    public List<DosageHeader> getHeader() throws Exception {
        return NaturalRadionuclideEnum.convertHeaderEntity();
    }

    public List<NaturalRadionuclideEntity> getHistory(int parameter) throws Exception {
        NaturalRadionuclideEntity naturalRadionuclideEntity = this.baseMapper.selectById(parameter);
        QueryWrapper<NaturalRadionuclideEntity> query = new QueryWrapper<NaturalRadionuclideEntity>();
        query.eq("name", naturalRadionuclideEntity.getName()).orderByDesc("create_time");
        query.eq("deleted", 0);
        query.eq("disabled", 0);
        return naturalRadionuclideMapper.selectList(query);
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return NaturalRadionuclideEnum.getDbHeader();
    }
}
