package com.evision.dosage.service.vehicle.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.constant.vehicle.VehicleInnerDosageFieldsEnum;
import com.evision.dosage.constant.vehicle.VehicleInnerDosageHeaderEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.vehicle.VehicleInnerDosageMapper;
import com.evision.dosage.pojo.entity.vehicle.VehicleInnerDosageEntity;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.pojo.model.ExcelCellCoordinate;
import com.evision.dosage.service.vehicle.VehicleInnerDosageService;
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
@DosageServiceConfig("vehicleInnerDosage")
public class VehicleInnerDosageServiceImpl extends ServiceImpl<VehicleInnerDosageMapper, VehicleInnerDosageEntity> implements VehicleInnerDosageService {
    static Map<Integer, String> vehicleInnerDosageOrderNameMap = Arrays.stream(VehicleInnerDosageFieldsEnum.values())
            .collect(Collectors.toMap(VehicleInnerDosageFieldsEnum::getOrderNo, VehicleInnerDosageFieldsEnum::getFieldName));

    @Resource
    VehicleInnerDosageMapper vehicleInnerDosageMapper;

    @Transactional(rollbackFor = Exception.class)
    public List<VehicleInnerDosageEntity> importExcel(MultipartFile file) throws Exception {
        Workbook workbook = ExcelUtils.getWorkbook(file);
        Sheet sheet = ExcelUtils.getSheet(workbook, DosageExcelEnum.VEHICLE_INNER_DOSAGE);
        return processExcel(sheet);
    }

    public List<VehicleInnerDosageEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) throws Exception {
        return vehicleInnerDosageMapper.queryValidData();
    }

    @Override
    public IPage<VehicleInnerDosageEntity> getDataList(String queryName, Integer pageSize, Integer pageNum, String sortName, Integer isAsc, Integer incloudDel) throws Exception{
//        Page<VehicleInnerDosageEntity> page = new Page<>(pageNum, pageSize);
//        isAsc = isAsc == null ? 1 : isAsc;
//        String order = isAsc == 1 ? "asc" : "desc";
//        if (StringUtils.isEmpty(sortName)){
//            sortName = VehicleInnerDosageFieldsEnum.DATA_CATEGORY.getDbFieldName();
//        }else {
//            sortName = VehicleInnerDosageFieldsEnum.getDbFiledName(sortName);
//        }
//        if (StringUtils.isEmpty(queryName)){
//            queryName = "%";
//        }else {
//            queryName = "%" + queryName + "%";
//        }
//        return vehicleInnerDosageMapper.queryValidDataFowWeb(page, queryName, sortName, order);
        throw new DosageException(DosageExcelEnum.VEHICLE_INNER_DOSAGE.getExcelType() + "请调用查询接口getDataByExcelType");

    }

    public List<VehicleInnerDosageEntity> getHistory(String parameterOne, String parameterTwo, String parameterThree) throws Exception {
        return vehicleInnerDosageMapper.getHistory(parameterOne, parameterTwo);
    }

    public List<DosageHeader> getHeader() throws Exception {
        return VehicleInnerDosageHeaderEnum.convertHeaderEntity();
    }

    public List<VehicleInnerDosageEntity> getHistory(int parameter) throws Exception{
        List<VehicleInnerDosageEntity> entities = vehicleInnerDosageMapper.getDataById(parameter);
        if (CollectionUtils.isEmpty(entities)){
            throw new DosageException(String.format("ID: %d 无对应记录", parameter));
        }
        VehicleInnerDosageEntity entity = entities.get(0);
        return vehicleInnerDosageMapper.getHistory(entity.getDataCategory(), entity.getRoadType());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception{
        return VehicleInnerDosageHeaderEnum.convertDbHeaderEntity();
    }

    /**
     * 车厢γ剂量率监测数据
     *
     * @param sheet sheet
     * @return 实体类数据
     */
    public List<VehicleInnerDosageEntity> processExcel(Sheet sheet) throws Exception {
        List<VehicleInnerDosageEntity> vehicleDosageEntities;
        log.info("process excel: " + sheet.getSheetName());
        // 获取数据起始单元格坐标，并校验表列名行
        ExcelCellCoordinate excelCellCoordinate = obtainExcelBodyBeginCoordinate(sheet);
        // 取Excel数据
        vehicleDosageEntities = obtainExcelData(sheet, excelCellCoordinate);
        duplicateCheck(vehicleDosageEntities);//主键改为ID，不需检查
        // 数据写库
        dataUpdate(vehicleDosageEntities);
        return vehicleInnerDosageMapper.queryValidData();
    }

    /**
     * 更新数据库数据
     *
     * @param vehicleDosageEntities excel数据
     */
    public void dataUpdate(List<VehicleInnerDosageEntity> vehicleDosageEntities) throws Exception {
        // 遍历excel，新增数据
        for (VehicleInnerDosageEntity vehicleInnerDosageEntity : vehicleDosageEntities) {
            List<VehicleInnerDosageEntity> dbVehicleDosageEntities = vehicleInnerDosageMapper.queryValidByKeys(vehicleInnerDosageEntity);
            if (CollectionUtils.isEmpty(dbVehicleDosageEntities)) {
                add(vehicleInnerDosageEntity);
                continue;
            }
            VehicleInnerDosageEntity dbVehicleInnerDosageEntity = dbVehicleDosageEntities.get(0);
            if (isModify(vehicleInnerDosageEntity, dbVehicleInnerDosageEntity)) {
                vehicleInnerDosageMapper.updateDisabled(dbVehicleInnerDosageEntity);
                add(vehicleInnerDosageEntity);
            }
        }
        List<VehicleInnerDosageEntity> dbVehicleDosageEntities = vehicleInnerDosageMapper.queryValidData();
        //遍历数据库，标识删除数据
        for (VehicleInnerDosageEntity dbVehicleInnerDosageEntity : dbVehicleDosageEntities) {
            boolean deleted = false;
            for (VehicleInnerDosageEntity vehicleInnerDosageEntity : vehicleDosageEntities) {
                if (isSameKeys(vehicleInnerDosageEntity, dbVehicleInnerDosageEntity)) {
                    deleted = true;
                }
            }
            if (!deleted) {
                delete(dbVehicleInnerDosageEntity);
            }
        }
    }

    private void add(VehicleInnerDosageEntity vehicleInnerDosageEntity) throws Exception {
        vehicleInnerDosageEntity.setDisabled(1);
        vehicleInnerDosageEntity.setDeleted(0);
        vehicleInnerDosageEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleInnerDosageMapper.add(vehicleInnerDosageEntity);
    }

    private void delete(VehicleInnerDosageEntity vehicleInnerDosageEntity) throws Exception {
        vehicleInnerDosageEntity.setDeleted(1);
        vehicleInnerDosageEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleInnerDosageMapper.delete(vehicleInnerDosageEntity);
    }

    private void duplicateCheck(List<VehicleInnerDosageEntity> vehicleDosageEntities) throws Exception {
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
                    VehicleInnerDosageEntity duplicate = vehicleDosageEntities.get(i);
                    throw new DosageException("excel存在主键重复的数据: " + duplicate.getDataCategory() + " " + duplicate.getRoadType());
                }
            }
            hasCheck.add(i);
        }
    }

    private boolean isSameKeys(VehicleInnerDosageEntity vehicleInnerDosageEntity, VehicleInnerDosageEntity dbVehicleInnerDosageEntity) throws Exception {
        return CompareUtils.equals(vehicleInnerDosageEntity.getDataCategory(), dbVehicleInnerDosageEntity.getDataCategory(), true)
                && CompareUtils.equals(vehicleInnerDosageEntity.getRoadType(), dbVehicleInnerDosageEntity.getRoadType(), true);
    }

    private boolean isModify(VehicleInnerDosageEntity vehicleInnerDosageEntity, VehicleInnerDosageEntity dbVehicleInnerDosageEntity) throws Exception {
        if (!CompareUtils.equals(vehicleInnerDosageEntity.getVehicleType(), dbVehicleInnerDosageEntity.getVehicleType())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleInnerDosageEntity.getMonitorNumber(), dbVehicleInnerDosageEntity.getMonitorNumber())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleInnerDosageEntity.getFrontResultRange(), dbVehicleInnerDosageEntity.getFrontResultRange())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleInnerDosageEntity.getMiddleResultRange(), dbVehicleInnerDosageEntity.getMiddleResultRange())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleInnerDosageEntity.getBackResultRange(), dbVehicleInnerDosageEntity.getBackResultRange())) {
            return true;
        }
        return !CompareUtils.equals(vehicleInnerDosageEntity.getEntiretyResultRange(), dbVehicleInnerDosageEntity.getEntiretyResultRange());
    }

    /**
     * 获取Excel数据
     *
     * @param sheet               sheet页
     * @param excelCellCoordinate 数据起始单元格坐标
     * @return 实体类数据
     */
    private List<VehicleInnerDosageEntity> obtainExcelData(Sheet sheet, ExcelCellCoordinate excelCellCoordinate) throws Exception {
        List<VehicleInnerDosageEntity> vehicleDosageEntities = new ArrayList<>();
        int rowIndex = excelCellCoordinate.getRow();
        int columnBegin = excelCellCoordinate.getColumn();
        int rowEnd = sheet.getLastRowNum();
        for (; rowIndex <= rowEnd; rowIndex++) {
            VehicleInnerDosageEntity vehicleInnerDosageEntity = new VehicleInnerDosageEntity();
            Row row = sheet.getRow(rowIndex);
            int columnEnd = vehicleInnerDosageOrderNameMap.size() + columnBegin;
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
                        vehicleInnerDosageEntity.setDataCategory(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 2: {
                        vehicleInnerDosageEntity.setRoadType(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 3: {
                        vehicleInnerDosageEntity.setVehicleType(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 4: {
                        vehicleInnerDosageEntity.setMonitorNumber(ExcelUtils.getIntegerValue(cell));
                        break;
                    }
                    case 5: {
                        vehicleInnerDosageEntity.setFrontResultRange(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 6: {
                        vehicleInnerDosageEntity.setMiddleResultRange(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 7: {
                        vehicleInnerDosageEntity.setBackResultRange(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 8: {
                        vehicleInnerDosageEntity.setEntiretyResultRange(ExcelUtils.getStringValue(cell));
                        break;
                    }
                }
            }
            addCommonFieldValues(vehicleInnerDosageEntity);
            vehicleDosageEntities.add(vehicleInnerDosageEntity);
        }
        return vehicleDosageEntities;
    }

    private void addCommonFieldValues(VehicleInnerDosageEntity vehicleInnerDosageEntity) throws Exception {
        vehicleInnerDosageEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleInnerDosageEntity.setDeleted(0);
        vehicleInnerDosageEntity.setCreateTime(LocalDateTime.now());
        vehicleInnerDosageEntity.setUpdateTime(LocalDateTime.now());
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
            String errorInfo = " Excel 表头错误，标准表头：" + vehicleInnerDosageOrderNameMap.values();
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
        int fieldIndex = VehicleInnerDosageFieldsEnum.SERIAL_NUMBER.getOrderNo();
        int maxFieldNo = VehicleInnerDosageFieldsEnum.ENTIRE_RESULT_RANGE.getOrderNo();
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
                if (cell.getStringCellValue().contains(vehicleInnerDosageOrderNameMap.get(fieldIndex - correctionCount))) {
                    fieldCheck.set(fieldIndex - correctionCount, true);
                    if (VehicleInnerDosageFieldsEnum.SERIAL_NUMBER.getFieldName().equals(cell.getStringCellValue())) {
                        // 数值起始点坐标列设置
                        excelCellCoordinate.setColumn(cell.getColumnIndex());
                    }
                }
            }
        }
        return fieldCheck;
    }
}
