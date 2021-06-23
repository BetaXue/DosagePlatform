package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.constant.FoodWaterRadionuclideEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.FoodWaterRadionuclideMapper;
import com.evision.dosage.pojo.entity.FoodWaterRadionuclideEntity;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.service.FoodWaterRadionuclideService;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 饮水中放射性核素活度浓度
 *
 * @Author: Yu Xiao
 * @Date: 2020/2/17 18:43
 */
@Slf4j
@Service
@DosageServiceConfig("water")
public class WaterRadionuclideServiceImpl extends ServiceImpl<FoodWaterRadionuclideMapper, FoodWaterRadionuclideEntity> implements FoodWaterRadionuclideService {
    @Autowired
    private FoodWaterRadionuclideMapper foodWaterRadionuclideMapper;

    /***
     * 检查表头名称
     * @param sheet
     * @return
     */
    private boolean validateRowName(Sheet sheet) {
        Row row = sheet.getRow(0);
        FoodWaterRadionuclideEnum[] values = FoodWaterRadionuclideEnum.values();
        //列数不同
        if (row.getPhysicalNumberOfCells() != values.length) {
            return false;
        }
        int rowNum = 0;
        for (FoodWaterRadionuclideEnum value : values) {
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
    public boolean dealData(List<FoodWaterRadionuclideEntity> list) throws Exception {
        //已删除的展示在维护列表中，查看记录按createTime主键disabled=0
        if (!CollectionUtils.isEmpty(list)) {
            List<String> numberList = new ArrayList<>();
            for (FoodWaterRadionuclideEntity foodWaterRadionuclideEntity : list) {
                String sampleNumber = foodWaterRadionuclideEntity.getSampleNumber();
                numberList.add(sampleNumber);
                //查找有效的样本
                QueryWrapper<FoodWaterRadionuclideEntity> query = new QueryWrapper<FoodWaterRadionuclideEntity>();
                query.eq("type", 1).eq("sample_number", sampleNumber).eq("disabled", 1).eq("deleted", 0);
                List<FoodWaterRadionuclideEntity> existsEntityList = foodWaterRadionuclideMapper.selectList(query);
                if (CollectionUtils.isEmpty(existsEntityList)) {
                    //新增
                    foodWaterRadionuclideEntity.setCreateTime(LocalDateTime.now());
                    foodWaterRadionuclideEntity.setUserId(UserUtils.getCurrentUserId());
                    foodWaterRadionuclideEntity.setType(1);
                    foodWaterRadionuclideMapper.insert(foodWaterRadionuclideEntity);
                } else {
                    //数据不一致
                    FoodWaterRadionuclideEntity existsEntity = existsEntityList.get(0);
                    if (!foodWaterRadionuclideEntity.equals(existsEntity)) {
                        //将旧记录设为禁用状态
                        existsEntity.setDisabled(0);
                        foodWaterRadionuclideMapper.updateById(existsEntity);
                        //新增
                        foodWaterRadionuclideEntity.setType(1);
                        foodWaterRadionuclideEntity.setCreateTime(LocalDateTime.now());
                        foodWaterRadionuclideEntity.setUserId(UserUtils.getCurrentUserId());
                        foodWaterRadionuclideMapper.insert(foodWaterRadionuclideEntity);
                    }
                }
            }
            List<FoodWaterRadionuclideEntity> existsEntityDb = this.getDataList(null, null, null, 0);
            if (!CollectionUtils.isEmpty(existsEntityDb)) {
                for (FoodWaterRadionuclideEntity entityDb : existsEntityDb) {
                    if (!numberList.contains(entityDb.getSampleNumber())) {
                        entityDb.setDeleted(1);
                        foodWaterRadionuclideMapper.updateById(entityDb);
                    }
                }
            }
        }
        return true;
    }


    @Override
    public List<FoodWaterRadionuclideEntity> importExcel(MultipartFile multipartFile) throws Exception {
        List<FoodWaterRadionuclideEntity> response = new ArrayList<>();

        Workbook workbook = ExcelUtils.getWorkbook(multipartFile);
        int sheetCount = workbook.getNumberOfSheets();
        for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex += 1) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            String sheetName = sheet.getSheetName();
            String fileName = multipartFile.getOriginalFilename();
            //水样放射性核素活度浓度
            if (DosageExcelEnum.WATER_RADIONUCLIDE.getExcelName().equals(fileName) ||
                    DosageExcelEnum.WATER_RADIONUCLIDE.getExcelName().equals(sheetName)) {
                //检查行名
//                if (!validateRowName(sheet)) {
//                    log.error("excel表列数或者列名与模板不符！");
//                    throw new DosageException("excel表列数或者列名与模板不符！");
//                }
                List<String> cellKeys = new ArrayList<>();
                for (FoodWaterRadionuclideEnum value : FoodWaterRadionuclideEnum.values()) {
                    cellKeys.add(value.getEntityName());
                }
                Class<FoodWaterRadionuclideEntity> c = FoodWaterRadionuclideEntity.class;
                List<FoodWaterRadionuclideEntity> result = ExcelUtils.readExcel(sheet, c, cellKeys, 1, 1, 8);
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
    public List<FoodWaterRadionuclideEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) {
        QueryWrapper<FoodWaterRadionuclideEntity> query = new QueryWrapper<FoodWaterRadionuclideEntity>();
        query.eq("type", 1).eq("disabled", 1);
        //模糊查询
        if (!StringUtils.isEmpty(queryName)) {
            query.like("sample_number", queryName);
        }
        if (incloudDel == 0) {
            query.eq("deleted", incloudDel);
        }
        //排序
        boolean defaultIsAsc = true;

        if (isAsc != null && isAsc == 0) {
            defaultIsAsc = false;
        }
        if (!StringUtils.isEmpty(sortName)) {
            String defaultSortName = FoodWaterRadionuclideEnum.getDbFieldName(sortName);
            query.orderBy(true, defaultIsAsc, defaultSortName+"+0");
        }
        return foodWaterRadionuclideMapper.selectList(query);
    }

    @Override
    public List<DosageHeader> getHeader() {
        return FoodWaterRadionuclideEnum.convertHeaderEntity();
    }

    public List<FoodWaterRadionuclideEntity> getHistory(int parameter) throws Exception {
        FoodWaterRadionuclideEntity entity = foodWaterRadionuclideMapper.selectById(parameter);
        QueryWrapper<FoodWaterRadionuclideEntity> query = new QueryWrapper<FoodWaterRadionuclideEntity>();
        query.eq("sample_number", entity.getSampleNumber())
                .eq("disabled", 0)
                .eq("deleted", 0)
                .eq("type", 1)
                .orderByDesc("create_time");
        return foodWaterRadionuclideMapper.selectList(query);
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return FoodWaterRadionuclideEnum.convertDbHeaderEntity();
    }

    /**
     * 此方法为了获取接口请求url，因为食物和水的数据在同一个表中，service也共用一个，
     * 接口中无excelType传递到service层，所以需要确定是食物还是水样的查询。
     *
     * @return type 数据类型
     */
    @Deprecated
    private int getRequestUrl() {
        int type = -1;
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String uri = request.getRequestURI();
        if (!StringUtils.isEmpty(uri)) {
            if (uri.endsWith("food")) {
                type = 0;
            }
            if (uri.endsWith("water")) {
                type = 1;
            }
        }
        log.info(uri);
        return type;
    }
}
