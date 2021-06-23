package com.evision.dosage.service.vehicle.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.constant.vehicle.VehicleInnerRnDosageFieldsEnum;
import com.evision.dosage.constant.vehicle.VehicleInnerRnDosageHeaderEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.vehicle.VehicleInnerRnDosageMapper;
import com.evision.dosage.pojo.entity.vehicle.VehicleInnerRnDosageEntity;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.pojo.model.ExcelCellCoordinate;
import com.evision.dosage.service.vehicle.VehicleInnerRnDosageService;
import com.evision.dosage.utils.CompareUtils;
import com.evision.dosage.utils.ExcelUtils;
import com.evision.dosage.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
@DosageServiceConfig("vehicleInnerRnDosage")
public class VehicleInnerRnDosageServiceImpl extends ServiceImpl<VehicleInnerRnDosageMapper, VehicleInnerRnDosageEntity> implements VehicleInnerRnDosageService {
    static Map<Integer, String> vehicleInnerRnDosageOrderNameMap = Arrays.stream(VehicleInnerRnDosageFieldsEnum.values())
            .collect(Collectors.toMap(VehicleInnerRnDosageFieldsEnum::getOrderNo, VehicleInnerRnDosageFieldsEnum::getFieldName));

    @Resource
    VehicleInnerRnDosageMapper vehicleInnerRnDosageMapper;

    @Transactional(rollbackFor = Exception.class)
    public List<VehicleInnerRnDosageEntity> importExcel(MultipartFile file) throws Exception {
        Workbook workbook = ExcelUtils.getWorkbook(file);
        Sheet sheet = ExcelUtils.getSheet(workbook, DosageExcelEnum.VEHICLE_INNER_RN_DOSAGE);
        return processExcel(sheet);
    }

    public List<VehicleInnerRnDosageEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) throws Exception {
        return vehicleInnerRnDosageMapper.queryValidData();
    }

    @Override
    public IPage<VehicleInnerRnDosageEntity> getDataList(String queryName, Integer pageSize, Integer pageNum, String sortName, Integer isAsc, Integer incloudDel) throws Exception {
//        Page<VehicleInnerRnDosageEntity> page = new Page<>(pageNum, pageSize);
//        isAsc = isAsc == null ? 1 : isAsc;
//        String order = isAsc == 1 ? "asc" : "desc";
//        if (StringUtils.isEmpty(sortName)){
//            sortName = VehicleInnerRnDosageFieldsEnum.DATA_CATEGORY.getDbFieldName();
//        }else {
//            sortName = VehicleInnerRnDosageFieldsEnum.getDbFiledName(sortName);
//        }
//        if (StringUtils.isEmpty(queryName)){
//            queryName = "%";
//        }else {
//            queryName = "%" + queryName + "%";
//        }
//        return vehicleInnerRnDosageMapper.queryValidDataFowWeb(page, queryName, sortName, order);
        throw new DosageException(DosageExcelEnum.VEHICLE_INNER_RN_DOSAGE.getExcelType() + "请调用查询接口getDataByExcelType");

    }

    public List<VehicleInnerRnDosageEntity> getHistory(String parameterOne, String parameterTwo, String parameterThree) throws Exception {
        return vehicleInnerRnDosageMapper.getHistory(parameterOne, parameterTwo);
    }

    public List<DosageHeader> getHeader() throws Exception {
        return VehicleInnerRnDosageHeaderEnum.convertHeaderEntity();
    }

    public List<VehicleInnerRnDosageEntity> getHistory(int parameter) throws Exception {
        List<VehicleInnerRnDosageEntity> entities = vehicleInnerRnDosageMapper.getDataById(parameter);
        if (CollectionUtils.isEmpty(entities)){
            throw new DosageException(String.format("ID: %d 无对应记录", parameter));
        }
        VehicleInnerRnDosageEntity entity = entities.get(0);
        return vehicleInnerRnDosageMapper.getHistory(entity.getDataCategory(), entity.getRoute());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return VehicleInnerRnDosageHeaderEnum.convertDbHeaderEntity();
    }

    /**
     * 车厢γ剂量率监测数据
     *
     * @param sheet sheet
     * @return 实体类数据
     */
    public List<VehicleInnerRnDosageEntity> processExcel(Sheet sheet) throws Exception {
        List<VehicleInnerRnDosageEntity> vehicleInnerRnDosageEntities;
        log.info("process excel: " + sheet.getSheetName());
        // 获取数据起始单元格坐标，并校验表列名行
        ExcelCellCoordinate excelCellCoordinate = obtainExcelBodyBeginCoordinate(sheet);
        // 取Excel数据
        vehicleInnerRnDosageEntities = obtainExcelData(sheet, excelCellCoordinate);
        duplicateCheck(vehicleInnerRnDosageEntities);//主键改为ID，不需检查
        // 数据写库
        dataUpdate(vehicleInnerRnDosageEntities);
        return vehicleInnerRnDosageMapper.queryValidData();
    }

    /**
     * 更新数据库数据
     *
     * @param vehicleInnerRnDosageEntities excel数据
     */
    public void dataUpdate(List<VehicleInnerRnDosageEntity> vehicleInnerRnDosageEntities) throws Exception {
        // 遍历excel，新增数据
        for (VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity : vehicleInnerRnDosageEntities) {
            List<VehicleInnerRnDosageEntity> dbVehicleDosageEntities = vehicleInnerRnDosageMapper.queryValidByKeys(vehicleInnerRnDosageEntity);
            if (CollectionUtils.isEmpty(dbVehicleDosageEntities)) {
                add(vehicleInnerRnDosageEntity);
                continue;
            }
            VehicleInnerRnDosageEntity dbVehicleInnerRnDosageEntity = dbVehicleDosageEntities.get(0);
            if (isModify(vehicleInnerRnDosageEntity, dbVehicleInnerRnDosageEntity)) {
                vehicleInnerRnDosageMapper.updateDisabled(dbVehicleInnerRnDosageEntity);
                add(vehicleInnerRnDosageEntity);
            }
        }
        List<VehicleInnerRnDosageEntity> dbVehicleDosageEntities = vehicleInnerRnDosageMapper.queryValidData();
        //遍历数据库，标识删除数据
        for (VehicleInnerRnDosageEntity dbVehicleInnerRnDosageEntity : dbVehicleDosageEntities) {
            boolean deleted = false;
            for (VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity : vehicleInnerRnDosageEntities) {
                if (isSameKeys(vehicleInnerRnDosageEntity, dbVehicleInnerRnDosageEntity)) {
                    deleted = true;
                }
            }
            if (!deleted) {
                delete(dbVehicleInnerRnDosageEntity);
            }
        }
    }

    private void add(VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity) throws Exception {
        vehicleInnerRnDosageEntity.setDisabled(1);
        vehicleInnerRnDosageEntity.setDeleted(0);
        vehicleInnerRnDosageEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleInnerRnDosageMapper.add(vehicleInnerRnDosageEntity);
    }

    private void delete(VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity) throws Exception {
        vehicleInnerRnDosageEntity.setDeleted(1);
        vehicleInnerRnDosageEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleInnerRnDosageMapper.delete(vehicleInnerRnDosageEntity);
    }

    private void duplicateCheck(List<VehicleInnerRnDosageEntity> vehicleInnerRnDosageEntities) throws Exception {
        int allNumber = vehicleInnerRnDosageEntities.size();
        List<Integer> hasCheck = new ArrayList<>();
        for (int i = 0; i < allNumber; i++) {
            for (int j = 0; j < allNumber; j++) {
                if (i == j) {
                    continue;
                }
                if (hasCheck.contains(j)) {
                    continue;
                }
                if (isSameKeys(vehicleInnerRnDosageEntities.get(i), vehicleInnerRnDosageEntities.get(j))) {
                    VehicleInnerRnDosageEntity duplicate = vehicleInnerRnDosageEntities.get(i);
                    throw new DosageException("excel存在主键重复的数据: " + duplicate.getDataCategory() + " " + duplicate.getRoute());
                }
            }
            hasCheck.add(i);
        }
    }

    private boolean isSameKeys(VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity, VehicleInnerRnDosageEntity dbVehicleInnerRnDosageEntity) throws Exception {
        return CompareUtils.equals(vehicleInnerRnDosageEntity.getDataCategory(), dbVehicleInnerRnDosageEntity.getDataCategory(), true)
                && CompareUtils.equals(vehicleInnerRnDosageEntity.getRoute(), dbVehicleInnerRnDosageEntity.getRoute(), true);
    }

    private boolean isModify(VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity, VehicleInnerRnDosageEntity dbVehicleInnerRnDosageEntity) throws Exception {
        if (!CompareUtils.equals(vehicleInnerRnDosageEntity.getLocation(), dbVehicleInnerRnDosageEntity.getLocation())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleInnerRnDosageEntity.getLocation(), dbVehicleInnerRnDosageEntity.getLocation())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleInnerRnDosageEntity.getMonitorNumber(), dbVehicleInnerRnDosageEntity.getMonitorNumber())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleInnerRnDosageEntity.getMeasureResultMax(), dbVehicleInnerRnDosageEntity.getMeasureResultMax())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleInnerRnDosageEntity.getMeasureResultMin(), dbVehicleInnerRnDosageEntity.getMeasureResultMin())) {
            return true;
        }
        return !CompareUtils.equals(vehicleInnerRnDosageEntity.getMeasureResultAverage(), dbVehicleInnerRnDosageEntity.getMeasureResultAverage());
    }

    /**
     * 获取Excel数据
     *
     * @param sheet               sheet页
     * @param excelCellCoordinate 数据起始单元格坐标
     * @return 实体类数据
     */
    private List<VehicleInnerRnDosageEntity> obtainExcelData(Sheet sheet, ExcelCellCoordinate excelCellCoordinate) throws Exception {
        List<VehicleInnerRnDosageEntity> vehicleInnerRnDosageEntities = new ArrayList<>();
        int rowIndex = excelCellCoordinate.getRow();
        int columnBegin = excelCellCoordinate.getColumn();
        int rowEnd = sheet.getLastRowNum();
        for (; rowIndex <= rowEnd; rowIndex++) {
            VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity = new VehicleInnerRnDosageEntity();
            Row row = sheet.getRow(rowIndex);
            int columnEnd = vehicleInnerRnDosageOrderNameMap.size() + columnBegin;
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
                        vehicleInnerRnDosageEntity.setDataCategory(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 2: {
                        vehicleInnerRnDosageEntity.setLocation(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 3: {
                        vehicleInnerRnDosageEntity.setRoute(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 4: {
                        vehicleInnerRnDosageEntity.setMonitorNumber(ExcelUtils.getIntegerValue(cell));
                        break;
                    }
                    case 5: {
                        vehicleInnerRnDosageEntity.setMeasureResultMax(getRnValue(cell));
                        break;
                    }
                    case 6: {
                        vehicleInnerRnDosageEntity.setMeasureResultMin(getRnValue(cell));
                        break;
                    }
                    case 7: {
                        vehicleInnerRnDosageEntity.setMeasureResultAverage(getRnValue(cell));

                        break;
                    }
                }
            }
            addCommonFieldValues(vehicleInnerRnDosageEntity);
            vehicleInnerRnDosageEntities.add(vehicleInnerRnDosageEntity);
        }
        return vehicleInnerRnDosageEntities;
    }

    private BigDecimal getRnValue(Cell cell) {
        BigDecimal v = ExcelUtils.getDecimalValue(cell);
        return v == null ? BigDecimal.valueOf(-1) : v;
    }

    private void addCommonFieldValues(VehicleInnerRnDosageEntity vehicleInnerRnDosageEntity) throws Exception {
        vehicleInnerRnDosageEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleInnerRnDosageEntity.setDeleted(0);
        vehicleInnerRnDosageEntity.setCreateTime(LocalDateTime.now());
        vehicleInnerRnDosageEntity.setUpdateTime(LocalDateTime.now());
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
            String errorInfo = " Excel 表头错误，标准表头" + vehicleInnerRnDosageOrderNameMap.values();
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
        int fieldIndex = VehicleInnerRnDosageFieldsEnum.SERIAL_NUMBER.getOrderNo();
        int maxFieldNo = VehicleInnerRnDosageFieldsEnum.MEASURE_RESULT_AVERAGE.getOrderNo();
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
                if (vehicleInnerRnDosageOrderNameMap.get(fieldIndex - correctionCount).equals(cell.getStringCellValue())) {
                    fieldCheck.set(fieldIndex - correctionCount, true);
                    if (VehicleInnerRnDosageFieldsEnum.SERIAL_NUMBER.getFieldName().equals(cell.getStringCellValue())) {
                        // 数值起始点坐标列设置
                        excelCellCoordinate.setColumn(cell.getColumnIndex());
                    }
                }
            }
        }
        return fieldCheck;
    }
}
