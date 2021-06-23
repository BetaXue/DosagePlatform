package com.evision.dosage.service.vehicle.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.constant.vehicle.VehicleDosageFieldsEnum;
import com.evision.dosage.constant.vehicle.VehicleDosageHeaderEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.vehicle.VehicleDosageMapper;
import com.evision.dosage.pojo.entity.vehicle.VehicleDosageEntity;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.pojo.model.ExcelCellCoordinate;
import com.evision.dosage.service.vehicle.VehicleDosageService;
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
 * @date 2020/2/21 9:21
 */
@Slf4j
@Service
@DosageServiceConfig("vehicleDosage")
public class VehicleDosageServiceImpl extends ServiceImpl<VehicleDosageMapper, VehicleDosageEntity> implements VehicleDosageService {
    static Map<Integer, String> vehicleDosageOrderNameMap = Arrays.stream(VehicleDosageFieldsEnum.values())
            .collect(Collectors.toMap(VehicleDosageFieldsEnum::getOrderNo, VehicleDosageFieldsEnum::getFieldName));

    @Resource
    VehicleDosageMapper vehicleDosageMapper;

    @Transactional(rollbackFor = Exception.class)
    public List<VehicleDosageEntity> importExcel(MultipartFile file) throws Exception {
        Workbook workbook = ExcelUtils.getWorkbook(file);
        Sheet sheet = ExcelUtils.getSheet(workbook, DosageExcelEnum.VEHICLE_DOSAGE);
        return processExcel(sheet);
    }

    public List<VehicleDosageEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) throws Exception {
        throw new DosageException(DosageExcelEnum.VEHICLE_DOSAGE.getExcelType() + "请调用分页查询接口getPagingDataByExcelType，添加pageSize, pageNum参数");
    }

    @Override
    public IPage<VehicleDosageEntity> getDataList(String queryName, Integer pageSize, Integer pageNum, String sortName, Integer isAsc, Integer incloudDel) {
        Page<VehicleDosageEntity> page = new Page<>(pageNum, pageSize);
        isAsc = isAsc == null ? 1 : isAsc;
        String order = isAsc == 1 ? "asc" : "desc";
        if (StringUtils.isEmpty(sortName)) {
            sortName = VehicleDosageFieldsEnum.DATA_CATEGORY.getDbFieldName();
        } else {
            sortName = VehicleDosageFieldsEnum.getDbFiledName(sortName);
        }
        if (StringUtils.isEmpty(queryName)) {
            queryName = "%";
        } else {
            queryName = "%" + queryName + "%";
        }
        return vehicleDosageMapper.queryValidDataFowWeb(page, queryName, sortName, order, incloudDel);
    }

    public List<VehicleDosageEntity> getHistory(String parameterOne, String parameterTwo, String parameterThree) throws Exception {
        return vehicleDosageMapper.getHistory(parameterOne, parameterTwo);
    }

    public List<DosageHeader> getHeader() throws Exception {
        return VehicleDosageHeaderEnum.convertHeaderEntity();
    }

    public List<VehicleDosageEntity> getHistory(int parameter) throws Exception{
        List<VehicleDosageEntity> entities = vehicleDosageMapper.getDataById(parameter);
        if (CollectionUtils.isEmpty(entities)){
            throw new DosageException(String.format("ID: %d 无对应记录", parameter));
        }
        VehicleDosageEntity entity = entities.get(0);
        return vehicleDosageMapper.getHistory(entity.getDataCategory(), entity.getRoute());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception{
        return VehicleDosageHeaderEnum.convertDbHeaderEntity();
    }

    /**
     * 车厢γ剂量率监测数据
     *
     * @param sheet sheet
     * @return 实体类数据
     */
    public List<VehicleDosageEntity> processExcel(Sheet sheet) throws Exception {
        List<VehicleDosageEntity> vehicleDosageEntities;
        log.info("process excel: " + sheet.getSheetName());
        // 获取数据起始单元格坐标，并校验表列名行
        ExcelCellCoordinate excelCellCoordinate = obtainExcelBodyBeginCoordinate(sheet);
        // 取Excel数据
        vehicleDosageEntities = obtainExcelData(sheet, excelCellCoordinate);
        duplicateCheck(vehicleDosageEntities);//主键改为ID，不需检查
        // 数据写库
        dataUpdate(vehicleDosageEntities);
        return vehicleDosageMapper.queryValidData();
    }

    /**
     * 更新数据库数据
     *
     * @param vehicleDosageEntities excel数据
     */
    public void dataUpdate(List<VehicleDosageEntity> vehicleDosageEntities) throws Exception {
        // 遍历excel，新增数据
        for (VehicleDosageEntity vehicleDosageEntity : vehicleDosageEntities) {
            List<VehicleDosageEntity> dbVehicleDosageEntities = vehicleDosageMapper.queryValidByKeys(vehicleDosageEntity);
            if (CollectionUtils.isEmpty(dbVehicleDosageEntities)) {
                add(vehicleDosageEntity);
                continue;
            }
            for (VehicleDosageEntity dbVehicleDosageEntity : dbVehicleDosageEntities) {
                if (isSameKeys(vehicleDosageEntity, dbVehicleDosageEntity)) {
                    if (isModify(vehicleDosageEntity, dbVehicleDosageEntity)) {
                        vehicleDosageMapper.updateDisabled(dbVehicleDosageEntity);
                        add(vehicleDosageEntity);
                    }
                }
            }
        }
        List<VehicleDosageEntity> dbVehicleDosageEntities = vehicleDosageMapper.queryValidData();
        //遍历数据库，标识删除数据
        for (VehicleDosageEntity dbVehicleDosageEntity : dbVehicleDosageEntities) {
            boolean deleted = false;
            for (VehicleDosageEntity vehicleDosageEntity : vehicleDosageEntities) {
                if (isSameKeys(vehicleDosageEntity, dbVehicleDosageEntity)) {
                    deleted = true;
                }
            }
            if (!deleted) {
                delete(dbVehicleDosageEntity);
            }
        }
    }

    private void add(VehicleDosageEntity vehicleDosageEntity) throws Exception {
        vehicleDosageEntity.setDisabled(1);
        vehicleDosageEntity.setDeleted(0);
        vehicleDosageEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleDosageMapper.add(vehicleDosageEntity);
    }

    private void delete(VehicleDosageEntity vehicleDosageEntity) throws Exception {
        vehicleDosageEntity.setDeleted(1);
        vehicleDosageEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleDosageMapper.delete(vehicleDosageEntity);
    }

    private void duplicateCheck(List<VehicleDosageEntity> vehicleDosageEntities) throws Exception {
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
                    VehicleDosageEntity duplicate = vehicleDosageEntities.get(i);
                    throw new DosageException("excel存在主键重复的数据: " + duplicate.getDataCategory() + " " + duplicate.getRoute());
                }
            }
            hasCheck.add(i);
        }
    }

    private boolean isSameKeys(VehicleDosageEntity vehicleDosageEntity, VehicleDosageEntity dbVehicleDosageEntity) throws Exception {
        return CompareUtils.equals(vehicleDosageEntity.getDataCategory(), dbVehicleDosageEntity.getDataCategory(), true)
                && CompareUtils.equals(vehicleDosageEntity.getRoute(), dbVehicleDosageEntity.getRoute(), true)
                && CompareUtils.equals(vehicleDosageEntity.getLocation(), dbVehicleDosageEntity.getLocation())
                && CompareUtils.equals(vehicleDosageEntity.getRoadName(), dbVehicleDosageEntity.getRoadName());
    }

    private boolean isModify(VehicleDosageEntity vehicleDosageEntity, VehicleDosageEntity dbVehicleDosageEntity) throws Exception {
//        if (!CompareUtils.equals(vehicleDosageEntity.getLocation(), dbVehicleDosageEntity.getLocation())) {
//            return true;
//        }
//        if (!CompareUtils.equals(vehicleDosageEntity.getRoadName(), dbVehicleDosageEntity.getRoadName())) {
//            return true;
//        }
        if (!CompareUtils.equals(vehicleDosageEntity.getDrivingState(), dbVehicleDosageEntity.getDrivingState())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageEntity.getMonitorNumber(), dbVehicleDosageEntity.getMonitorNumber())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageEntity.getMeasureResultMinRange(), dbVehicleDosageEntity.getMeasureResultMinRange())) {
            return true;
        }
        if (!CompareUtils.equals(vehicleDosageEntity.getMeasureResultMaxRange(), dbVehicleDosageEntity.getMeasureResultMaxRange())) {
            return true;
        }
        return !CompareUtils.equals(vehicleDosageEntity.getMeasureResultAverageRange(), dbVehicleDosageEntity.getMeasureResultAverageRange());
    }

    /**
     * 获取Excel数据
     *
     * @param sheet               sheet页
     * @param excelCellCoordinate 数据起始单元格坐标
     * @return 实体类数据
     */
    private List<VehicleDosageEntity> obtainExcelData(Sheet sheet, ExcelCellCoordinate excelCellCoordinate) throws Exception {
        List<VehicleDosageEntity> vehicleDosageEntities = new ArrayList<>();
        int rowIndex = excelCellCoordinate.getRow();
        int columnBegin = excelCellCoordinate.getColumn();
        int rowEnd = sheet.getLastRowNum();
        for (; rowIndex <= rowEnd; rowIndex++) {
            VehicleDosageEntity vehicleDosageEntity = new VehicleDosageEntity();
            Row row = sheet.getRow(rowIndex);
            int columnEnd = vehicleDosageOrderNameMap.size() + columnBegin;
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
                        vehicleDosageEntity.setDataCategory(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 2: {
                        vehicleDosageEntity.setLocation(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 3: {
                        vehicleDosageEntity.setRoadName(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 4: {
                        vehicleDosageEntity.setRoute(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 5: {
                        vehicleDosageEntity.setDrivingState(ExcelUtils.getStringValue(cell));
                        break;
                    }
                    case 6: {
                        vehicleDosageEntity.setMonitorNumber(ExcelUtils.getIntegerValue(cell));
                        break;
                    }
                    case 7: {
                        vehicleDosageEntity.setMeasureResultMaxRange(ExcelUtils.getStringValue(cell));

                        break;
                    }
                    case 8: {
                        vehicleDosageEntity.setMeasureResultMinRange(ExcelUtils.getStringValue(cell));

                        break;
                    }
                    case 9: {
                        vehicleDosageEntity.setMeasureResultAverageRange(ExcelUtils.getStringValue(cell));

                        break;
                    }
                }
            }
            addCommonFieldValues(vehicleDosageEntity);
            vehicleDosageEntities.add(vehicleDosageEntity);
        }
        return vehicleDosageEntities;
    }

    private void addCommonFieldValues(VehicleDosageEntity vehicleDosageEntity) throws Exception {
        vehicleDosageEntity.setUserId(UserUtils.getCurrentUserId());
        vehicleDosageEntity.setDeleted(0);
        vehicleDosageEntity.setCreateTime(LocalDateTime.now());
        vehicleDosageEntity.setUpdateTime(LocalDateTime.now());
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
            String errorInfo = " Excel 表头错误，标准表头：" + vehicleDosageOrderNameMap.values();
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
        int fieldIndex = VehicleDosageFieldsEnum.SERIAL_NUMBER.getOrderNo();
        int maxFieldNo = VehicleDosageFieldsEnum.MEASURE_RESULT_AVERAGE_RANGE.getOrderNo();
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
                if (cell.getStringCellValue().contains(vehicleDosageOrderNameMap.get(fieldIndex - correctionCount))) {
                    fieldCheck.set(fieldIndex - correctionCount, true);
                    if (VehicleDosageFieldsEnum.SERIAL_NUMBER.getFieldName().equals(cell.getStringCellValue())) {
                        // 数值起始点坐标列设置
                        excelCellCoordinate.setColumn(cell.getColumnIndex());
                    }
                }
            }
        }
        return fieldCheck;
    }
}
