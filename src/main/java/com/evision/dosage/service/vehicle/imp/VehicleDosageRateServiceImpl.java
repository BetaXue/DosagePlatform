package com.evision.dosage.service.vehicle.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.constant.vehicle.VehicleDosageRateFieldsEnum;
import com.evision.dosage.constant.vehicle.VehicleDosageRateHeaderEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.vehicle.VehicleDosageRateMapper;
import com.evision.dosage.pojo.entity.vehicle.VehicleDosageRateEntity;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.pojo.model.ExcelCellCoordinate;
import com.evision.dosage.service.vehicle.VehicleDosageRateService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author DingZhanYang
 * @date 2020/2/21 16:09
 */
@Slf4j
@Service
@DosageServiceConfig("vehicleDosageRate")
public class VehicleDosageRateServiceImpl extends ServiceImpl<VehicleDosageRateMapper, VehicleDosageRateEntity> implements VehicleDosageRateService {
    static Map<Integer, String> vehicleDosageRateOrderNameMap = Arrays.stream(VehicleDosageRateFieldsEnum.values())
            .collect(Collectors.toMap(VehicleDosageRateFieldsEnum::getOrderNo, VehicleDosageRateFieldsEnum::getFieldName));

    @Resource
    VehicleDosageRateMapper vehicleDosageRateMapper;

    @Transactional(rollbackFor = Exception.class)
    public List<VehicleDosageRateEntity> importExcel(MultipartFile file) throws Exception {
        Workbook workbook = ExcelUtils.getWorkbook(file);
        Sheet sheet = ExcelUtils.getSheet(workbook, DosageExcelEnum.VEHICLE_DOSAGE_RATE);
        return processExcel(sheet);
    }

    public List<VehicleDosageRateEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) throws Exception {
        throw new DosageException(DosageExcelEnum.VEHICLE_DOSAGE_RATE.getExcelType() + "请调用分页查询接口getPagingDataByExcelType，添加pageSize, pageNum参数");
    }

    @Override
    public IPage<VehicleDosageRateEntity> getDataList(String queryName, Integer pageSize, Integer pageNum, String sortName, Integer isAsc, Integer incloudDel) {
        Page<VehicleDosageRateEntity> page = new Page<>(pageNum, pageSize);
        isAsc = isAsc == null ? 1 : isAsc;
        String order = isAsc == 1 ? "asc" : "desc";
        if (StringUtils.isEmpty(sortName)) {
            sortName = VehicleDosageRateFieldsEnum.VEHICLE_CATEGORY.getDbFieldName();
        } else {
            sortName = VehicleDosageRateFieldsEnum.getDbFiledName(sortName);
        }
        if (StringUtils.isEmpty(queryName)) {
            queryName = "%";
        } else {
            queryName = "%" + queryName + "%";
        }
        return vehicleDosageRateMapper.queryValidDataFowWeb(page, queryName, sortName, order, incloudDel);
    }

    public List<VehicleDosageRateEntity> getHistory(String parameterOne, String parameterTwo, String parameterThree) throws Exception {
        return vehicleDosageRateMapper.getHistory(parameterOne, parameterTwo, parameterThree);
    }

    public List<DosageHeader> getHeader() throws Exception {
        return VehicleDosageRateHeaderEnum.convertHeaderEntity();
    }

    public List<VehicleDosageRateEntity> getHistory(int parameter) throws Exception {
        List<VehicleDosageRateEntity> entities = vehicleDosageRateMapper.getDataById(parameter);
        if (CollectionUtils.isEmpty(entities)){
            throw new DosageException(String.format("ID: %d 无对应记录", parameter));
        }
        VehicleDosageRateEntity entity = entities.get(0);
        return vehicleDosageRateMapper.getHistory(entity.getVehicleCategory(), entity.getRoute(), entity.getStation());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return VehicleDosageRateHeaderEnum.convertDbHeaderEntity();
    }

    /**
     * 车厢γ剂量率监测数据
     *
     * @param sheet sheet
     * @return 实体类数据
     */
    public List<VehicleDosageRateEntity> processExcel(Sheet sheet) throws Exception {
        List<VehicleDosageRateEntity> vehicleDosageEntities;
        log.info("process excel: " + sheet.getSheetName());
        // 获取数据起始单元格坐标，并校验表列名行
        ExcelCellCoordinate excelCellCoordinate = obtainExcelBodyBeginCoordinate(sheet);
        // 取Excel数据
        vehicleDosageEntities = obtainExcelData(sheet, excelCellCoordinate);
        duplicateCheck(vehicleDosageEntities);//主键改为ID，不需检查
        // 数据写库
        dataUpdate(vehicleDosageEntities);
        return vehicleDosageRateMapper.queryValidData();
    }

    /**
     * 更新数据库数据
     *
     * @param vehicleDosageEntities excel数据
     */
    public void dataUpdate(List<VehicleDosageRateEntity> vehicleDosageEntities) throws Exception {
        // 遍历excel，新增数据
        for (VehicleDosageRateEntity vehicleDosageRateEntity : vehicleDosageEntities) {
            List<VehicleDosageRateEntity> dbVehicleDosageEntities = vehicleDosageRateMapper.queryValidByKeys(vehicleDosageRateEntity);
            if (CollectionUtils.isEmpty(dbVehicleDosageEntities)) {
                add(vehicleDosageRateEntity);
                continue;
            }
            VehicleDosageRateEntity dbVehicleDosageRateEntity = dbVehicleDosageEntities.get(0);
            if (isModify(vehicleDosageRateEntity, dbVehicleDosageRateEntity)) {
                vehicleDosageRateMapper.updateDisabled(dbVehicleDosageRateEntity);
                add(vehicleDosageRateEntity);
            }
        }
        List<VehicleDosageRateEntity> dbVehicleDosageEntities = vehicleDosageRateMapper.queryValidData();
        //遍历数据库，标识删除数据
        for (VehicleDosageRateEntity dbVehicleDosageRateEntity : dbVehicleDosageEntities) {
            boolean deleted = false;
            for (VehicleDosageRateEntity vehicleDosageRateEntity : vehicleDosageEntities) {
                if (isSameKeys(vehicleDosageRateEntity, dbVehicleDosageRateEntity)) {
                    deleted = true;
                }
            }
            if (!deleted) {
                delete(dbVehicleDosageRateEntity);
            }
        }
    }

    private void add(VehicleDosageRateEntity vehicleDosageRateEntity) throws Exception {
        vehicleDosageRateEntity.setDisabled(1);
        vehicleDosageRateEntity.setDeleted(0);
        vehicleDosageRateEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleDosageRateMapper.add(vehicleDosageRateEntity);
    }

    private void delete(VehicleDosageRateEntity vehicleDosageRateEntity) throws Exception {
        vehicleDosageRateEntity.setDeleted(1);
        vehicleDosageRateEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleDosageRateMapper.delete(vehicleDosageRateEntity);
    }

    private void duplicateCheck(List<VehicleDosageRateEntity> vehicleDosageEntities) throws Exception {
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
                    VehicleDosageRateEntity duplicate = vehicleDosageEntities.get(i);
                    throw new DosageException("excel存在主键重复的数据: " + duplicate.getVehicleCategory() + " " + duplicate.getRoute() + " " + duplicate.getStation());
                }
            }
            hasCheck.add(i);
        }
    }

    private boolean isSameKeys(VehicleDosageRateEntity vehicleDosageRateEntity, VehicleDosageRateEntity dbVehicleDosageRateEntity) throws Exception {
        return CompareUtils.equals(vehicleDosageRateEntity.getVehicleCategory(), dbVehicleDosageRateEntity.getVehicleCategory(), true)
                && CompareUtils.equals(vehicleDosageRateEntity.getRoute(), dbVehicleDosageRateEntity.getRoute(), true)
                && CompareUtils.equals(vehicleDosageRateEntity.getStation(), dbVehicleDosageRateEntity.getStation(), true);
    }

    private boolean isModify(VehicleDosageRateEntity vehicleDosageRateEntity, VehicleDosageRateEntity dbVehicleDosageRateEntity) {
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateOne(), dbVehicleDosageRateEntity.getDosageRateOne())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateTwo(), dbVehicleDosageRateEntity.getDosageRateTwo())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateThree(), dbVehicleDosageRateEntity.getDosageRateThree())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateFour(), dbVehicleDosageRateEntity.getDosageRateFour())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateFive(), dbVehicleDosageRateEntity.getDosageRateFive())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateSix(), dbVehicleDosageRateEntity.getDosageRateSix())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateEight(), dbVehicleDosageRateEntity.getDosageRateEight())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateNine(), dbVehicleDosageRateEntity.getDosageRateNine())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateTen(), dbVehicleDosageRateEntity.getDosageRateTen())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateAverage(), dbVehicleDosageRateEntity.getDosageRateAverage())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateMin(), dbVehicleDosageRateEntity.getDosageRateMin())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getDosageRateMax(), dbVehicleDosageRateEntity.getDosageRateMax())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getCalibrationFactor(), dbVehicleDosageRateEntity.getCalibrationFactor())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getCalibrationDosageRateAverage(), dbVehicleDosageRateEntity.getCalibrationDosageRateAverage())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageRateEntity.getCalibrationDosageRateMin(), dbVehicleDosageRateEntity.getCalibrationDosageRateMin())) {
            return true;
        }
        return !CompareUtils.equals(vehicleDosageRateEntity.getCalibrationDosageRateMax(), dbVehicleDosageRateEntity.getCalibrationDosageRateMax());
    }

    /**
     * 获取Excel数据
     *
     * @param sheet               sheet页
     * @param excelCellCoordinate 数据起始单元格坐标
     * @return 实体类数据
     */
    private List<VehicleDosageRateEntity> obtainExcelData(Sheet sheet, ExcelCellCoordinate excelCellCoordinate) throws Exception {
        List<VehicleDosageRateEntity> vehicleDosageRateEntities = new ArrayList<>();
        int rowIndex = excelCellCoordinate.getRow();
        int columnBegin = excelCellCoordinate.getColumn();
        int rowEnd = sheet.getLastRowNum();
        for (; rowIndex <= rowEnd; rowIndex++) {
            VehicleDosageRateEntity vehicleDosageRateEntity = new VehicleDosageRateEntity();
            Row row = sheet.getRow(rowIndex);
            int columnEnd = vehicleDosageRateOrderNameMap.size() + columnBegin;
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
                        vehicleDosageRateEntity.setVehicleCategory(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 2: {
                        vehicleDosageRateEntity.setRoute(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 3: {
                        vehicleDosageRateEntity.setStation(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 4: {
                        vehicleDosageRateEntity.setDosageRateOne(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 5: {
                        vehicleDosageRateEntity.setDosageRateTwo(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 6: {
                        vehicleDosageRateEntity.setDosageRateThree(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 7: {
                        vehicleDosageRateEntity.setDosageRateFour(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 8: {
                        vehicleDosageRateEntity.setDosageRateFive(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 9: {
                        vehicleDosageRateEntity.setDosageRateSix(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 10: {
                        vehicleDosageRateEntity.setDosageRateSeven(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 11: {
                        vehicleDosageRateEntity.setDosageRateEight(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 12: {
                        vehicleDosageRateEntity.setDosageRateNine(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 13: {
                        vehicleDosageRateEntity.setDosageRateTen(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 14: {
                        vehicleDosageRateEntity.setDosageRateAverage(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 15: {
                        vehicleDosageRateEntity.setDosageRateMin(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 16: {
                        vehicleDosageRateEntity.setDosageRateMax(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 17: {
                        vehicleDosageRateEntity.setCalibrationFactor(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 18: {
                        vehicleDosageRateEntity.setCalibrationDosageRateAverage(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 19: {
                        vehicleDosageRateEntity.setCalibrationDosageRateMin(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                    case 20: {
                        vehicleDosageRateEntity.setCalibrationDosageRateMax(ExcelUtils.getDecimalValue(cell));
                        break;
                    }
                }
            }
            addCommonFieldValues(vehicleDosageRateEntity);
            vehicleDosageRateEntities.add(vehicleDosageRateEntity);
        }
        return vehicleDosageRateEntities;
    }

    private void addCommonFieldValues(VehicleDosageRateEntity vehicleDosageRateEntity) throws Exception {
        vehicleDosageRateEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleDosageRateEntity.setDeleted(0);
        vehicleDosageRateEntity.setCreateTime(LocalDateTime.now());
        vehicleDosageRateEntity.setUpdateTime(LocalDateTime.now());
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
        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
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
            String errorInfo = " Excel 表头错误，标准表头：" + vehicleDosageRateOrderNameMap.values();
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
        int fieldIndex = VehicleDosageRateFieldsEnum.SERIAL_NUMBER.getOrderNo();
        int maxFieldNo = VehicleDosageRateFieldsEnum.CALIBRATION_DOSAGE_RATE_MAX.getOrderNo();
        for (; fieldIndex <= maxFieldNo; fieldIndex++) {
            fieldCheck.add(false);
        }
        int rowBegin = excelCellCoordinate.getRow();
        int columnBegin = excelCellCoordinate.getColumn();
        Row row = sheet.getRow(excelCellCoordinate.getRow());
        if (row == null) {
            return fieldCheck;
        }
        int columnEnd = row.getLastCellNum() - 1;
        if (columnEnd < maxFieldNo) {
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
                if (cell.getStringCellValue().contains(vehicleDosageRateOrderNameMap.get(fieldIndex - correctionCount))) {
                    fieldCheck.set(fieldIndex - correctionCount, true);
                    if (VehicleDosageRateFieldsEnum.SERIAL_NUMBER.getFieldName().equals(cell.getStringCellValue())) {
                        // 数值起始点坐标列设置
                        excelCellCoordinate.setColumn(cell.getColumnIndex());
                    }
                }
            }
        }
        return fieldCheck;
    }
}
