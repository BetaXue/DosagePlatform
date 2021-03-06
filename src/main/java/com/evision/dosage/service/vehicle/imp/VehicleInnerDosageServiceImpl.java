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
        throw new DosageException(DosageExcelEnum.VEHICLE_INNER_DOSAGE.getExcelType() + "?????????????????????getDataByExcelType");

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
            throw new DosageException(String.format("ID: %d ???????????????", parameter));
        }
        VehicleInnerDosageEntity entity = entities.get(0);
        return vehicleInnerDosageMapper.getHistory(entity.getDataCategory(), entity.getRoadType());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception{
        return VehicleInnerDosageHeaderEnum.convertDbHeaderEntity();
    }

    /**
     * ?????????????????????????????
     *
     * @param sheet sheet
     * @return ???????????????
     */
    public List<VehicleInnerDosageEntity> processExcel(Sheet sheet) throws Exception {
        List<VehicleInnerDosageEntity> vehicleDosageEntities;
        log.info("process excel: " + sheet.getSheetName());
        // ?????????????????????????????????????????????????????????
        ExcelCellCoordinate excelCellCoordinate = obtainExcelBodyBeginCoordinate(sheet);
        // ???Excel??????
        vehicleDosageEntities = obtainExcelData(sheet, excelCellCoordinate);
        duplicateCheck(vehicleDosageEntities);//????????????ID???????????????
        // ????????????
        dataUpdate(vehicleDosageEntities);
        return vehicleInnerDosageMapper.queryValidData();
    }

    /**
     * ?????????????????????
     *
     * @param vehicleDosageEntities excel??????
     */
    public void dataUpdate(List<VehicleInnerDosageEntity> vehicleDosageEntities) throws Exception {
        // ??????excel???????????????
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
        //????????????????????????????????????
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
                    throw new DosageException("excel???????????????????????????: " + duplicate.getDataCategory() + " " + duplicate.getRoadType());
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
     * ??????Excel??????
     *
     * @param sheet               sheet???
     * @param excelCellCoordinate ???????????????????????????
     * @return ???????????????
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
            String errorInfo = " Excel ??????????????????????????????" + vehicleInnerDosageOrderNameMap.values();
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
                if (cell.getStringCellValue().contains(vehicleInnerDosageOrderNameMap.get(fieldIndex - correctionCount))) {
                    fieldCheck.set(fieldIndex - correctionCount, true);
                    if (VehicleInnerDosageFieldsEnum.SERIAL_NUMBER.getFieldName().equals(cell.getStringCellValue())) {
                        // ??????????????????????????????
                        excelCellCoordinate.setColumn(cell.getColumnIndex());
                    }
                }
            }
        }
        return fieldCheck;
    }
}
