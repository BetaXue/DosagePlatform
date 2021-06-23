package com.evision.dosage.service.vehicle.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.constant.vehicle.MetroTransferStationDosageFieldsEnum;
import com.evision.dosage.constant.vehicle.MetroTransferStationDosageHeaderEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.vehicle.MetroTransferStationDosageMapper;
import com.evision.dosage.pojo.entity.vehicle.MetroTransferStationDosageEntity;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.pojo.model.ExcelCellCoordinate;
import com.evision.dosage.service.vehicle.MetroTransferStationDosageService;
import com.evision.dosage.utils.CompareUtils;
import com.evision.dosage.utils.ExcelUtils;
import com.evision.dosage.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DingZhanYang
 * @date 2020/2/21 9:21
 */
@Slf4j
@Service
@DosageServiceConfig("metroTransferStationDosage")
public class MetroTransferStationDosageServiceImpl extends ServiceImpl<MetroTransferStationDosageMapper, MetroTransferStationDosageEntity> implements MetroTransferStationDosageService {
    static Map<Integer, String> metroTransferStationDosageOrderNameMap = Arrays.stream(MetroTransferStationDosageFieldsEnum.values())
            .collect(Collectors.toMap(MetroTransferStationDosageFieldsEnum::getOrderNo, MetroTransferStationDosageFieldsEnum::getFieldName));

    @Resource
    MetroTransferStationDosageMapper metroTransferStationDosageMapper;

    @Transactional(rollbackFor = Exception.class)
    public List<MetroTransferStationDosageEntity> importExcel(MultipartFile file) throws Exception {
        Workbook workbook = ExcelUtils.getWorkbook(file);
        Sheet sheet = ExcelUtils.getSheet(workbook, DosageExcelEnum.METRO_TRANSFER_STATION_DOSAGE);
        return processExcel(sheet);
    }

    public List<MetroTransferStationDosageEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) throws Exception {
        throw new DosageException(DosageExcelEnum.METRO_TRANSFER_STATION_DOSAGE.getExcelType() + "请调用分页查询接口getPagingDataByExcelType，添加pageSize, pageNum参数");
    }

    @Override
    public IPage<MetroTransferStationDosageEntity> getDataList(String queryName, Integer pageSize, Integer pageNum, String sortName, Integer isAsc, Integer incloudDel) {
        Page<MetroTransferStationDosageEntity> page = new Page<>(pageNum, pageSize);
        isAsc = isAsc == null ? 1 : isAsc;
        String order = isAsc == 1 ? "asc" : "desc";
        if (StringUtils.isEmpty(sortName)) {
            sortName = MetroTransferStationDosageFieldsEnum.DATA_CATEGORY.getDbFieldName();
        } else {
            sortName = MetroTransferStationDosageFieldsEnum.getDbFiledName(sortName);
        }
        if (StringUtils.isEmpty(queryName)) {
            queryName = "%";
        } else {
            queryName = "%" + queryName + "%";
        }
        return metroTransferStationDosageMapper.queryValidDataFowWeb(page, queryName, sortName, order,incloudDel);
    }

    public List<MetroTransferStationDosageEntity> getHistory(String parameterOne, String parameterTwo, String parameterThree) throws Exception {
        return metroTransferStationDosageMapper.getHistory(parameterOne, parameterTwo);
    }

    public List<DosageHeader> getHeader() throws Exception {
        return MetroTransferStationDosageHeaderEnum.convertHeaderEntity();
    }

    public List<MetroTransferStationDosageEntity> getHistory(int parameter) throws Exception {
        MetroTransferStationDosageEntity entity = metroTransferStationDosageMapper.selectById(parameter);
        if (entity == null){
            throw new DosageException("id对应的记录不存在");
        }
        QueryWrapper<MetroTransferStationDosageEntity> query = new QueryWrapper<>();
        query.eq("data_category", entity.getDataCategory())
                .eq("station", entity.getStation())
                .eq("disabled", 0)
                .eq("deleted", 0)
                .orderByDesc("create_time");
        return metroTransferStationDosageMapper.selectList(query);
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return MetroTransferStationDosageHeaderEnum.convertDbHeaderEntity();
    }

    /**
     * 车厢γ剂量率监测数据
     *
     * @param sheet sheet
     * @return 实体类数据
     */
    public List<MetroTransferStationDosageEntity> processExcel(Sheet sheet) throws Exception {
        List<MetroTransferStationDosageEntity> vehicleDosageEntities;
        log.info("process excel: " + sheet.getSheetName());
        // 获取数据起始单元格坐标，并校验表列名行
        ExcelCellCoordinate excelCellCoordinate = obtainExcelBodyBeginCoordinate(sheet);
        // 取Excel数据
        vehicleDosageEntities = obtainExcelData(sheet, excelCellCoordinate);
        duplicateCheck(vehicleDosageEntities); //主键改为ID，不需检查
        // 数据写库
        dataUpdate(vehicleDosageEntities);
        return vehicleDosageEntities;
    }

    /**
     * 更新数据库数据
     *
     * @param vehicleDosageEntities excel数据
     */
    public void dataUpdate(List<MetroTransferStationDosageEntity> vehicleDosageEntities) throws Exception {
        // 遍历excel，新增数据
        for (MetroTransferStationDosageEntity metroTransferStationDosageEntity : vehicleDosageEntities) {
            List<MetroTransferStationDosageEntity> dbMetroTransferStationDosageEntities = metroTransferStationDosageMapper.queryValidByKeys(metroTransferStationDosageEntity);
            if (CollectionUtils.isEmpty(dbMetroTransferStationDosageEntities)) {
                add(metroTransferStationDosageEntity);
                continue;
            }
            MetroTransferStationDosageEntity dbMetroTransferStationDosageEntity = dbMetroTransferStationDosageEntities.get(0);
            if (isModify(metroTransferStationDosageEntity, dbMetroTransferStationDosageEntity)) {
                metroTransferStationDosageMapper.updateDisabled(dbMetroTransferStationDosageEntity);
                add(metroTransferStationDosageEntity);
            }
        }
        List<MetroTransferStationDosageEntity> dbMetroTransferStationDosageEntities = metroTransferStationDosageMapper.queryValidData();
        //遍历数据库，标识删除数据
        for (MetroTransferStationDosageEntity dbMetroTransferStationDosageEntity : dbMetroTransferStationDosageEntities) {
            boolean deleted = false;
            for (MetroTransferStationDosageEntity metroTransferStationDosageEntity : vehicleDosageEntities) {
                if (isSameKeys(metroTransferStationDosageEntity, dbMetroTransferStationDosageEntity)) {
                    deleted = true;
                }
            }
            if (!deleted) {
                delete(dbMetroTransferStationDosageEntity);
            }
        }
    }

    private void add(MetroTransferStationDosageEntity metroTransferStationDosageEntity) throws Exception {
        metroTransferStationDosageEntity.setDisabled(1);
        metroTransferStationDosageEntity.setDeleted(0);
        metroTransferStationDosageEntity.setUserId(UserUtils.getCurrentUserId());
        metroTransferStationDosageMapper.add(metroTransferStationDosageEntity);
    }

    private void delete(MetroTransferStationDosageEntity metroTransferStationDosageEntity) throws Exception {
        metroTransferStationDosageEntity.setDeleted(1);
        metroTransferStationDosageEntity.setUserId(UserUtils.getCurrentUserId());
        metroTransferStationDosageMapper.delete(metroTransferStationDosageEntity);
    }

    private void duplicateCheck(List<MetroTransferStationDosageEntity> vehicleDosageEntities) throws Exception {
        int allNumber = vehicleDosageEntities.size();
        List<Integer> hasCheck = new ArrayList<>();
        for (int i = 0; i < allNumber; i++) {
            for (int j = 0; j < allNumber; j++) {
                if (i == j) {
                    continue;
                }
                if (hasCheck.contains(j)) {
                    continue;
                }
                if (isSameKeys(vehicleDosageEntities.get(i), vehicleDosageEntities.get(j))) {
                    MetroTransferStationDosageEntity duplicate = vehicleDosageEntities.get(i);
                    throw new DosageException("excel存在主键重复的数据: " + duplicate.getDataCategory() + " " + duplicate.getStation());
                }
            }
            hasCheck.add(i);
        }
    }

    private boolean isSameKeys(MetroTransferStationDosageEntity metroTransferStationDosageEntity, MetroTransferStationDosageEntity dbMetroTransferStationDosageEntity) throws Exception {
        return CompareUtils.equals(metroTransferStationDosageEntity.getDataCategory(), dbMetroTransferStationDosageEntity.getDataCategory(), true)
                && CompareUtils.equals(metroTransferStationDosageEntity.getStation(), dbMetroTransferStationDosageEntity.getStation(), true);
    }

    private boolean isModify(MetroTransferStationDosageEntity metroTransferStationDosageEntity, MetroTransferStationDosageEntity dbMetroTransferStationDosageEntity) throws Exception {
        if (!CompareUtils.equals(metroTransferStationDosageEntity.getFirstQuarterMeasureResult(), dbMetroTransferStationDosageEntity.getFirstQuarterMeasureResult())) {
            return true;
        }
        if (!CompareUtils.equals(metroTransferStationDosageEntity.getSecondQuarterMeasureResult(), dbMetroTransferStationDosageEntity.getSecondQuarterMeasureResult())) {
            return true;
        }
        return !CompareUtils.equals(metroTransferStationDosageEntity.getFourthQuarterMeasureResult(), dbMetroTransferStationDosageEntity.getFourthQuarterMeasureResult());
    }

    /**
     * 获取Excel数据
     *
     * @param sheet               sheet页
     * @param excelCellCoordinate 数据起始单元格坐标
     * @return 实体类数据
     */
    private List<MetroTransferStationDosageEntity> obtainExcelData(Sheet sheet, ExcelCellCoordinate excelCellCoordinate) throws Exception {
        List<MetroTransferStationDosageEntity> vehicleDosageEntities = new ArrayList<>();
        int rowIndex = excelCellCoordinate.getRow();
        int columnBegin = excelCellCoordinate.getColumn();
        int rowEnd = sheet.getLastRowNum();
        for (; rowIndex <= rowEnd; rowIndex++) {
            MetroTransferStationDosageEntity metroTransferStationDosageEntity = new MetroTransferStationDosageEntity();
            Row row = sheet.getRow(rowIndex);
            int columnEnd = metroTransferStationDosageOrderNameMap.size() + columnBegin;
            int columnIndex = columnBegin;
            for (; columnIndex < columnEnd; columnIndex++) {
                Cell cell;
                if (ExcelUtils.isMergedRegion(sheet, rowIndex, columnIndex)) {
                    cell = ExcelUtils.getMergedRegionValue(sheet, rowIndex, columnIndex);
                } else {
                    cell = row.getCell(columnIndex);
                }
                if (cell == null) {
                    continue;
                }
                switch (columnIndex - excelCellCoordinate.getColumn()) {
                    case 1: {
                        metroTransferStationDosageEntity.setDataCategory(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 2: {
                        metroTransferStationDosageEntity.setStation(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 3: {
                        metroTransferStationDosageEntity.setFirstQuarterMeasureResult(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 4: {
                        metroTransferStationDosageEntity.setSecondQuarterMeasureResult(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 5: {
                        metroTransferStationDosageEntity.setThirdQuarterMeasureResult(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 6: {
                        metroTransferStationDosageEntity.setFourthQuarterMeasureResult(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                }
            }
            addCommonFieldValues(metroTransferStationDosageEntity);
            vehicleDosageEntities.add(metroTransferStationDosageEntity);
        }
        return vehicleDosageEntities;
    }

    private void addCommonFieldValues(MetroTransferStationDosageEntity metroTransferStationDosageEntity) throws Exception {
        metroTransferStationDosageEntity.setUserId(UserUtils.getCurrentUserId());
        metroTransferStationDosageEntity.setDeleted(0);
        metroTransferStationDosageEntity.setCreateTime(LocalDateTime.now());
        metroTransferStationDosageEntity.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 获取Excel表体起始单元格位置，并校验表头
     *
     * @param sheet Excel数据
     * @return 表体起始点坐标
     * @throws Exception exception
     */
    private ExcelCellCoordinate obtainExcelBodyBeginCoordinate(Sheet sheet) throws Exception {
        ExcelCellCoordinate excelCellCoordinate = new ExcelCellCoordinate();
        excelCellCoordinate.setCoordinate(0, 0);
        // Excel列字段名校验，及数值起始点坐标点位
        List<Boolean> fieldNameValids = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < sheet.getLastRowNum(); rowIndex++) {
            // 数值起始点坐标，行设置
            excelCellCoordinate.setRow(rowIndex);
            // 数值起始点坐标，列设置, checkFieldNames函数内
            List<Boolean> fieldNameValidTmp = checkExcelFieldNames(sheet, excelCellCoordinate);
            // 判断是否是列名行
            if (CompareUtils.isAnyTrue(fieldNameValidTmp)) {
                // 判断是否已定位到列名所在行
                if (CollectionUtils.isEmpty(fieldNameValids)) {
                    fieldNameValids.addAll(fieldNameValidTmp);
                } else {
                    // 对表头有多行的情况，多行间每个字段的检查结果取“或”，每个字段只要一行的字段名正确即可
                    for (int i = 0; i < fieldNameValidTmp.size(); i++) {
                        fieldNameValids.set(i, fieldNameValids.get(i) || fieldNameValidTmp.get(i));
                    }
                }
            } else {
                // 当前行不是列名行，并且曾遍历过表头，说明表头检查完成
                if (!CollectionUtils.isEmpty(fieldNameValids)) {
                    break;
                }
            }
        }
        // 判断表头的每个字段是否全部检查通过
        if (!CompareUtils.isAllTrue(fieldNameValids)) {
            String errorInfo = " Excel 表头错误, 标准表头：" + metroTransferStationDosageOrderNameMap.values();
            log.error(errorInfo);
            throw new DosageException(errorInfo);
        }
        return excelCellCoordinate;
    }

    /**
     * 检查Excel表头字段
     *
     * @param sheet               Excel数据
     * @param excelCellCoordinate 坐标
     * @return 每个表头字段的检查结果
     * @throws Exception exception
     */
    private List<Boolean> checkExcelFieldNames(Sheet sheet, ExcelCellCoordinate excelCellCoordinate) throws Exception {
        List<Boolean> fieldCheck = new ArrayList<>();
        int fieldIndex = MetroTransferStationDosageFieldsEnum.SERIAL_NUMBER.getOrderNo();
        int maxFieldNo = MetroTransferStationDosageFieldsEnum.FOURTH_QUARTER_MEASURE_RESULT.getOrderNo();
        for (; fieldIndex <= maxFieldNo; fieldIndex++) {
            fieldCheck.add(false);
        }
        int rowBegin = excelCellCoordinate.getRow();
        int columnBegin = excelCellCoordinate.getColumn();
        Row row = sheet.getRow(excelCellCoordinate.getRow());
        if (row == null) {
            return fieldCheck;
        }
        int columnEnd = row.getLastCellNum();
        if (columnEnd <= maxFieldNo) {
            String errorInfo = "excel列数不足，数据错误";
            log.error(errorInfo);
            throw new DosageException(errorInfo);
        }
        int columnIndex;
        int correctionCount = 0;
        for (fieldIndex = 0; fieldIndex <= maxFieldNo; fieldIndex++) {
            columnIndex = fieldIndex + columnBegin;
            Cell cell;
            if (ExcelUtils.isMergedRegion(sheet, rowBegin, columnIndex)) {
                cell = ExcelUtils.getMergedRegionValue(sheet, rowBegin, columnIndex);
            } else {
                cell = row.getCell(columnIndex);
            }
            if (cell == null) {
                correctionCount += 1;
                continue;
            }
            if (cell.getCellType() == CellType.STRING) {
                if (cell.getStringCellValue().contains(metroTransferStationDosageOrderNameMap.get(fieldIndex - correctionCount))) {
                    fieldCheck.set(fieldIndex - correctionCount, true);
                    if (MetroTransferStationDosageFieldsEnum.SERIAL_NUMBER.getFieldName().equals(cell.getStringCellValue())) {
                        // 数值起始点坐标列设置
                        excelCellCoordinate.setColumn(cell.getColumnIndex());
                    }
                }
            }
        }
        return fieldCheck;
    }
}
