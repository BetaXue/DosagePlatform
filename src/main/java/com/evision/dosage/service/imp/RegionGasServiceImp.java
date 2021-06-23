package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.regionGas.RegionGasHeader;
import com.evision.dosage.mapper.RegionGasMapper;
import com.evision.dosage.pojo.entity.RegionGas;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.RegionGasService;
import com.evision.dosage.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/19
 * 氡、钍射气及子体实体 --- 各地区、类型氡浓度
 */
@Service
@DosageServiceConfig("regionGas")
@Slf4j
public class RegionGasServiceImp extends ServiceImpl<RegionGasMapper, RegionGas> implements RegionGasService {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";
    private static final String MEAN = "mean";
    private static final String ACQUISITIONRATE = "数据获取率";
    private static final String CHANGERANGE = "变化范围";
    //模板最大列号
    private static final int COLUMN = 5;
    @Resource
    private RegionGasMapper regionGasMapper;

    @Override
    public DosageResponseBody getRegionGass(String region) {
        List<RegionGas> regionGases = regionGasMapper.getRegionGass(0);
        return DosageResponseBody.getInstance(1, "success", regionGases);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRegionGass(List<RegionGas> regionGass) {
        if (CollectionUtils.isEmpty(regionGass)) {
            return 0;
        }
        List<RegionGas> lists = regionGasMapper.getRegionGass(0);
        if (CollectionUtils.isEmpty(lists)) {
            return regionGasMapper.insertRegionGass(regionGass);
        }
        //查出未修改的数据
        List<RegionGas> removeData = new ArrayList<>();
        for (RegionGas regionRadon : regionGass) {
            for (RegionGas obj : lists) {
                if (org.apache.commons.lang3.StringUtils.equals(obj.getRegion(), regionRadon.getRegion())
                        && org.apache.commons.lang3.StringUtils.equals(obj.getType(), regionRadon.getType())){
                    if(org.apache.commons.lang3.StringUtils.equals(obj.getMean(), regionRadon.getMean())
                            && org.apache.commons.lang3.StringUtils.equals(obj.getAcquisitionRate(), regionRadon.getAcquisitionRate())
                            && org.apache.commons.lang3.StringUtils.equals(obj.getChangeRange(), regionRadon.getChangeRange())) {
                        removeData.add(regionRadon);
                    }
                    break;
                }
            }
        }
        //查出未导入的数据
        List<RegionGas> deleteData = new ArrayList<>();
        for (RegionGas obj : lists) {
            boolean exit = false;
            for (RegionGas regionRadon : regionGass) {
                if (org.apache.commons.lang3.StringUtils.equals(obj.getRegion(), regionRadon.getRegion())
                        && org.apache.commons.lang3.StringUtils.equals(obj.getType(), regionRadon.getType())) {
                    exit = true;
                    break;
                }
            }
            if (!exit) {
                deleteData.add(obj);
            }
        }
        //删除操作---逻辑删除，更新deleted字段
        if (deleteData.size() > 0) {
            for (RegionGas regionGas : deleteData) {
                regionGasMapper.deleteRegionGas(regionGas);
            }
        }
        //剔除未修改的数据，只操作修改的数据
        if(removeData.size() > 0){
            regionGass.removeAll(removeData);
        }
        if (CollectionUtils.isEmpty(regionGass)) {
            return 0;
        }
        for (RegionGas regionGas : regionGass) {
            regionGasMapper.updateRegionGas(regionGas);
        }
        return regionGasMapper.insertRegionGass(regionGass);
    }


    @Override
    public List<RegionGas> importExcel(MultipartFile multipartFile) throws Exception {
        //获取Workbook
        Workbook workbook = getWorkbook(multipartFile);
        //取第一个Sheet
        Sheet sheet = workbook.getSheetAt(0);
        //获取最大行数
        int rownum = sheet.getPhysicalNumberOfRows();
        /*//获取最大列数
        int colnum = row.getPhysicalNumberOfCells();*/
        List<RegionGas> regionGass = new ArrayList<>();
        String region = "";
        for (int i = 1; i < rownum; i++) {
            //获取第i行
            Row row = sheet.getRow(i);
            RegionGas regionGas = new RegionGas();
            String name = (String) getCellFormatValue(row.getCell(0));
            if (name != null && !"".equals(name)) {
                region = name;
            }
            String type = (String) getCellFormatValue(row.getCell(1));
            String mean = (String) getCellFormatValue(row.getCell(2));
            String acquisitionRate = (String) getCellFormatValue(row.getCell(3));
            String changeRange = (String) getCellFormatValue(row.getCell(4));
            regionGas.setRegion(region);
            regionGas.setType(type);
            regionGas.setMean(mean);
            regionGas.setAcquisitionRate(acquisitionRate);
            regionGas.setChangeRange(changeRange);
            regionGas.setCreateTime(LocalDateTime.now());
            regionGas.setUpdateTime(LocalDateTime.now());
            regionGas.setDisabled(1);
            regionGas.setDeleted(0);
            try {
                regionGas.setUserId(UserUtils.getCurrentUserId());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取用户ID错误！");
            }
            regionGass.add(regionGas);
        }
        insertRegionGass(regionGass);
        return regionGass;
    }

    private Workbook getWorkbook(MultipartFile multipartFile) {
        try {
            Workbook workbook = null;
            InputStream inputStream = multipartFile.getInputStream();
            String fileName = multipartFile.getOriginalFilename();
            if (!StringUtils.isEmpty(fileName)) {
                if (fileName.endsWith(EXCEL_XLS)) {
                    workbook = new HSSFWorkbook(inputStream);
                } else if (fileName.endsWith(EXCEL_XLSX)) {
                    workbook = new XSSFWorkbook(inputStream);
                }
            } else {
                throw new IllegalAccessException("上传数据Excel文件名为空");
            }
            if (!templateVerification(workbook)) {
                throw new IllegalAccessException("上传Excel文件模板错误");
            }
            return workbook;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 模板校验
     *
     * @param workbook
     * @return true：校验通过，false：校验有问题
     */
    private boolean templateVerification(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        //获取最大行数
        int rownum = sheet.getPhysicalNumberOfRows();
        if (rownum < 2) return false;
        //获取第二行
        Row row = sheet.getRow(1);
        //获取最大列数
        int colnum = row.getPhysicalNumberOfCells();
        if (colnum != COLUMN) return false;
        String mean = (String) getCellFormatValue(row.getCell(0));
        String type = (String) getCellFormatValue(row.getCell(1));
        String acquisitionRate = (String) getCellFormatValue(row.getCell(2));
        String changeRange = (String) getCellFormatValue(row.getCell(3));
        return !StringUtils.isEmpty(mean) && !StringUtils.isEmpty(acquisitionRate) &&
                !StringUtils.isEmpty(changeRange) && !StringUtils.isEmpty(type);
    }

    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                case NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }

                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

    /**
     * 公用方法获取列表信息
     *
     * @param queryName 模糊查询字段
     * @param sortName  排序字段
     * @param isAsc     是否正序
     * @return 数据集合
     */
    @Override
    public List<RegionGas> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) {
        List<RegionGas> regionGases = regionGasMapper.getRegionGass(incloudDel);
        return regionGases;
    }

    @Override
    public List<DosageHeader> getHeader() {
        return null;
    }

    public List<RegionGas> getHistory(int parameter) throws Exception {
        RegionGas regionGas = this.baseMapper.selectById(parameter);
        return this.baseMapper.getHistory(regionGas.getRegion(), regionGas.getType());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return RegionGasHeader.getDosageDbHeader();
    }
}
