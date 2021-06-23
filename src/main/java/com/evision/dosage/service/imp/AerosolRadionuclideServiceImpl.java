package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.AerosolRadionuclideEnum;
import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.AerosolRadionuclideMapper;
import com.evision.dosage.pojo.entity.AerosolRadionuclideEntity;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.service.AerosolRadionuclideService;
import com.evision.dosage.utils.ExcelUtils;
import com.evision.dosage.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 气溶胶中放射性核素活度浓度
 *
 * @Author: Yu Xiao
 * @Date: 2020/2/17 18:43
 */
@Slf4j
@Service
@DosageServiceConfig("aerosol")
public class AerosolRadionuclideServiceImpl extends ServiceImpl<AerosolRadionuclideMapper, AerosolRadionuclideEntity> implements AerosolRadionuclideService {
    @Autowired
    private AerosolRadionuclideMapper aerosolRadionuclideMapper;

    /***
     * 检查表头名称
     * @param sheet
     * @return
     */
    private boolean validateRowName(Sheet sheet) {
        Row row = sheet.getRow(0);
        AerosolRadionuclideEnum[] values = AerosolRadionuclideEnum.values();
        //列数不同
        if (row.getPhysicalNumberOfCells() != values.length) {
            return false;
        }
        int rowNum = 0;
        for (AerosolRadionuclideEnum value : values) {
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
    public boolean dealData(List<AerosolRadionuclideEntity> list) throws Exception {
        //已删除的展示在维护列表中，查看记录按createTime主键disabled=0
        if (!CollectionUtils.isEmpty(list)) {
            List<String> numberList = new ArrayList<>();
            for (AerosolRadionuclideEntity aerosolRadionuclideEntity : list) {
                String sampleNumber = aerosolRadionuclideEntity.getSampleNumber();
                numberList.add(sampleNumber);
                //查找有效的样本
                QueryWrapper<AerosolRadionuclideEntity> query = new QueryWrapper<AerosolRadionuclideEntity>();
                query.eq("sample_number", sampleNumber).eq("disabled", 1).eq("deleted", 0);
                List<AerosolRadionuclideEntity> existsEntityList = aerosolRadionuclideMapper.selectList(query);
                if (CollectionUtils.isEmpty(existsEntityList)) {
                    //新增
                    aerosolRadionuclideEntity.setCreateTime(LocalDateTime.now());
                    aerosolRadionuclideEntity.setUserId(UserUtils.getCurrentUserId());
                    aerosolRadionuclideMapper.insert(aerosolRadionuclideEntity);
                } else {
                    //数据不一致
                    AerosolRadionuclideEntity existsEntity = existsEntityList.get(0);
                    if (!aerosolRadionuclideEntity.equals(existsEntity)) {
                        //将旧记录设为禁用状态
                        existsEntity.setDisabled(0);
                        aerosolRadionuclideMapper.updateById(existsEntity);
                        //新增
                        aerosolRadionuclideEntity.setCreateTime(LocalDateTime.now());
                        aerosolRadionuclideEntity.setUserId(UserUtils.getCurrentUserId());
                        aerosolRadionuclideMapper.insert(aerosolRadionuclideEntity);
                    }
                }
            }
            List<AerosolRadionuclideEntity> existsEntityDb = this.getDataList(null, null, null, 0);
            if (!CollectionUtils.isEmpty(existsEntityDb)) {
                for (AerosolRadionuclideEntity entityDb : existsEntityDb) {
                    if (!numberList.contains(entityDb.getSampleNumber())) {
                        entityDb.setDeleted(1);
                        aerosolRadionuclideMapper.updateById(entityDb);
                    }
                }
            }
        }
        return true;
    }


    @Override
    public List<AerosolRadionuclideEntity> importExcel(MultipartFile multipartFile) throws Exception {
        List<AerosolRadionuclideEntity> response = new ArrayList<>();
        Workbook workbook = ExcelUtils.getWorkbook(multipartFile);
        int sheetCount = workbook.getNumberOfSheets();
        for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex += 1) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            String sheetName = sheet.getSheetName();
            String fileName = multipartFile.getOriginalFilename();
            if (DosageExcelEnum.AEROSOL_RADIONUCLIDE.getExcelName().equals(sheetName) ||
                    DosageExcelEnum.AEROSOL_RADIONUCLIDE.getExcelName().equals(fileName)) {
                //检查行名
                /*if (!validateRowName(sheet)) {
                    log.error("excel表列数或者列名与模板不符！");
                    throw new DosageException("excel表列数或者列名与模板不符！");
                }*/
                //读取数据
                List<String> cellKeys = new ArrayList<>();
                for (AerosolRadionuclideEnum value : AerosolRadionuclideEnum.values()) {
                    cellKeys.add(value.getEntityName());
                }
                Class<AerosolRadionuclideEntity> c = AerosolRadionuclideEntity.class;
                List<AerosolRadionuclideEntity> result = ExcelUtils.readExcel(sheet, c, cellKeys, 1, 1, 8);
                // 数据写库
                if (dealData(result)) {
                    log.info("excel sheet名称: " + sheetName + "上传成功！");
                    //response.addAll(result);
                }
            } else {
                log.error("上传的excel文件与数据源类别不符！");
                throw new DosageException("上传的excel文件与数据源类别不符！");
            }
        }
        return response;
    }

    @Override
    public List<AerosolRadionuclideEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDe) {
        QueryWrapper<AerosolRadionuclideEntity> query = new QueryWrapper<AerosolRadionuclideEntity>();
        query.eq("disabled", 1);
        if (incloudDe == 0) {
            query.eq("deleted", incloudDe);
        }
        //模糊查询
        if (!StringUtils.isEmpty(queryName)) {
            query.like("sample_number", queryName);
        }
        //排序
        boolean defaultIsAsc = true;
        if (isAsc != null && isAsc == 0) {
            defaultIsAsc = false;
        }
        if (!StringUtils.isEmpty(sortName)) {
            String defaultSortName = AerosolRadionuclideEnum.getDbFieldName(sortName);
            query.orderBy(true, defaultIsAsc, defaultSortName + "+0");
        }
        return aerosolRadionuclideMapper.selectList(query);
    }

    @Override
    public List<DosageHeader> getHeader() {
        return AerosolRadionuclideEnum.convertHeaderEntity();
    }

    public List<AerosolRadionuclideEntity> getHistory(int parameter) throws Exception {
        AerosolRadionuclideEntity aerosolRadionuclideEntity = aerosolRadionuclideMapper.selectById(parameter);
        QueryWrapper<AerosolRadionuclideEntity> query = new QueryWrapper<AerosolRadionuclideEntity>();
        query.eq("sample_number", aerosolRadionuclideEntity.getSampleNumber())
                .eq("disabled", 0)
                .eq("deleted", 0)
                .orderByDesc("create_time");
        return aerosolRadionuclideMapper.selectList(query);
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return AerosolRadionuclideEnum.convertDbHeaderEntity();
    }
}
