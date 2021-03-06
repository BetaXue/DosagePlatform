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
        throw new DosageException(DosageExcelEnum.VEHICLE_INNER_RN_DOSAGE.getExcelType() + "?????????????????????getDataByExcelType");

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
            throw new DosageException(String.format("ID: %d ???????????????", parameter));
        }
        VehicleInnerRnDosageEntity entity = entities.get(0);
        return vehicleInnerRnDosageMapper.getHistory(entity.getDataCategory(), entity.getRoute());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return VehicleInnerRnDosageHeaderEnum.convertDbHeaderEntity();
    }

    /**
     * ?????????????????????????????
     *
     * @param sheet sheet
     * @return ???????????????
     */
    public List<VehicleInnerRnDosageEntity> processExcel(Sheet sheet) throws Exception {
        List<VehicleInnerRnDosageEntity> vehicleInnerRnDosageEntities;
        log.info("process excel: " + sheet.getSheetName());
        // ?????????????????????????????????????????????????????????
        ExcelCellCoordinate excelCellCoordinate = obtainExcelBodyBeginCoordinate(sheet);
        // ???Excel??????
        vehicleInnerRnDosageEntities = obtainExcelData(sheet, excelCellCoordinate);
        duplicateCheck(vehicleInnerRnDosageEntities);//????????????ID???????????????
        // ????????????
        dataUpdate(vehicleInnerRnDosageEntities);
        return vehicleInnerRnDosageMapper.queryValidData();
    }

    /**
     * ?????????????????????
     *
     * @param vehicleInnerRnDosageEntities excel??????
     */
    public void dataUpdate(List<VehicleInnerRnDosageEntity> vehicleInnerRnDosageEntities) throws Exception {
        // ??????excel???????????????
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
        //????????????????????????????????????
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
                    throw new DosageException("excel???????????????????????????: " + duplicate.getDataCategory() + " " + duplicate.getRoute());
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
     * ??????Excel??????
     *
     * @param sheet               sheet???
     * @param excelCellCoordinate ???????????????????????????
     * @return ???????????????
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
     * ??????Excel?????????????????????????????????????????????
     *
     * @param sheet Excel??????
     * @return ?????????????????????
     * @throws Exception exception
     */
    private ExcelCellCoordinate obtainExcelBodyBeginCoordinate(Sheet sheet) throws Exception {
        ExcelCellCoordinate excelCellCoordinate = new ExcelCellCoordinate();
        excelCellCoordinate.setCoordinate(0, 0);
        // Excel???????????????????????????????????????????????????
        List<Boolean> fieldNameValids = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < sheet.getLastRowNum(); rowIndex++) {
            // ?????????????????????????????????
            excelCellCoordinate.setRow(rowIndex);
            // ?????????????????????????????????, checkFieldNames?????????
            List<Boolean> fieldNameValidTmp = checkExcelFieldNames(sheet, excelCellCoordinate);
            // ????????????????????????
            if (CompareUtils.isAnyTrue(fieldNameValidTmp)) {
                // ???????????????????????????????????????
                if (CollectionUtils.isEmpty(fieldNameValids)) {
                    fieldNameValids.addAll(fieldNameValidTmp);
                } else {
                    // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    for (int i = 0; i < fieldNameValidTmp.size(); i++) {
                        fieldNameValids.set(i, fieldNameValids.get(i) || fieldNameValidTmp.get(i));
                    }
                }
            } else {
                // ??????????????????????????????????????????????????????????????????????????????
                if (!CollectionUtils.isEmpty(fieldNameValids)) {
                    break;
                }
            }
        }
        // ???????????????????????????????????????????????????
        if (!CompareUtils.isAllTrue(fieldNameValids)) {
            String errorInfo = " Excel ???????????????????????????" + vehicleInnerRnDosageOrderNameMap.values();
            log.error(errorInfo);
            throw new DosageException(errorInfo);
        }
        return excelCellCoordinate;
    }

    /**
     * ??????Excel????????????
     *
     * @param sheet               Excel??????
     * @param excelCellCoordinate ??????
     * @return ?????????????????????????????????
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
            String errorInfo = "excel???????????????????????????";
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
                        // ??????????????????????????????
                        excelCellCoordinate.setColumn(cell.getColumnIndex());
                    }
                }
            }
        }
        return fieldCheck;
    }
}
