package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.regionRadon.RegionRadonHeader;
import com.evision.dosage.mapper.RegionRadonMapper;
import com.evision.dosage.pojo.entity.RegionRadon;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.RegionRadonService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/19
 * 各地区氡浓度
 */
@Service
@DosageServiceConfig("regionRadon")
@Slf4j
public class RegionRadonServiceImp extends ServiceImpl<RegionRadonMapper, RegionRadon> implements RegionRadonService {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";
    private static final String REGION_RADON = "各地区Tn浓度";
    private static final String REGION = "站点";
    private static final String AVERAGE_RADON = "平均Tn浓度";
    private static final String MISTAKE = "误差";
    //模板最大列号
    private static final int COLUMN = 3;
    @Resource
    private RegionRadonMapper regionRadonMapper;

    @Override
    public DosageResponseBody getRegionRadons() {
        List<RegionRadon> regionRadons = regionRadonMapper.getRegionRadons(0);
        double average;
        double total = 0;
        //计算总体平均TN浓度值
        if (!CollectionUtils.isEmpty(regionRadons)) {
            for (RegionRadon regionRadon :
                    regionRadons) {
                try {
                    total += Double.valueOf(regionRadon.getAverageRadon());
                } catch (NumberFormatException ignored) {
                }
            }
            if (regionRadons.size() > 0) {
                average = total / regionRadons.size();
                BigDecimal b = new BigDecimal(average);
                average = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                regionRadons.get(0).setAverage(average);
            }
        }
        return DosageResponseBody.getInstance(1, "success", regionRadons);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRegionRadons(List<RegionRadon> regionRadons) {
        if (CollectionUtils.isEmpty(regionRadons)) {
            return 0;
        }
        List<RegionRadon> lists = regionRadonMapper.getRegionRadons(0);
        if (CollectionUtils.isEmpty(lists)) {
            return regionRadonMapper.insertRegionRadons(regionRadons);
        }
        //查出未修改的数据
        List<RegionRadon> removeData = new ArrayList<>();
        for (RegionRadon regionRadon : regionRadons) {
            for (RegionRadon obj : lists) {
                if (org.apache.commons.lang3.StringUtils.equals(obj.getRegion(), regionRadon.getRegion())){
                    if(org.apache.commons.lang3.StringUtils.equals(obj.getAverageRadon(), regionRadon.getAverageRadon())
                    && org.apache.commons.lang3.StringUtils.equals(obj.getMistake(), regionRadon.getMistake())) {
                        removeData.add(regionRadon);
                    }
                    break;
                }
            }
        }
        //查出未导入的数据
        List<RegionRadon> deleteData = new ArrayList<>();
        for (RegionRadon obj : lists) {
            boolean exit = false;
            for (RegionRadon regionRadon : regionRadons) {
                if (org.apache.commons.lang3.StringUtils.equals(obj.getRegion(), regionRadon.getRegion())) {
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
            for (RegionRadon averageRadon : deleteData) {
                regionRadonMapper.deleteRegionRadon(averageRadon);
            }
        }
        //剔除未修改的数据，只操作修改的数据
        if(removeData.size() > 0){
            regionRadons.removeAll(removeData);
        }
        if (CollectionUtils.isEmpty(regionRadons)) {
            return 0;
        }
        for (RegionRadon regionRadon : regionRadons) {
            regionRadonMapper.updateRegionRadon(regionRadon);
        }
        return regionRadonMapper.insertRegionRadons(regionRadons);
    }


    @Override
    public List<RegionRadon> importExcel(MultipartFile multipartFile) {
        //获取Workbook
        Workbook workbook = getWorkbook(multipartFile);
        //取第一个Sheet
        Sheet sheet = workbook.getSheetAt(0);
        //获取最大行数
        int rownum = sheet.getPhysicalNumberOfRows();
        /*//获取最大列数
        int colnum = row.getPhysicalNumberOfCells();*/
        List<RegionRadon> regionRadons = new ArrayList<>();
        for (int i = 2; i < rownum; i++) {
            //获取第i行
            Row row = sheet.getRow(i);
            RegionRadon regionRadon = new RegionRadon();
            String region = (String) getCellFormatValue(row.getCell(0));
            String averageRadonStr = (String) getCellFormatValue(row.getCell(1));
            String mistake = (String) getCellFormatValue(row.getCell(2));
            regionRadon.setRegion(region);
            regionRadon.setAverageRadon(averageRadonStr);
            regionRadon.setMistake(mistake);
            regionRadon.setDisabled(1);
            regionRadon.setDeleted(0);
            try {
                regionRadon.setUserId(UserUtils.getCurrentUserId());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取用户ID错误！");
            }
            regionRadons.add(regionRadon);
        }
        insertRegionRadons(regionRadons);
        return regionRadons;
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
        if (rownum < 3) return false;
        //获取第一行
        Row row1 = sheet.getRow(0);
        String regionRadon = (String) getCellFormatValue(row1.getCell(0));
        //获取第二行
        Row row = sheet.getRow(1);
        //获取最大列数
        int colnum = row.getPhysicalNumberOfCells();
        if (colnum != COLUMN) return false;
        String region = (String) getCellFormatValue(row.getCell(0));
        String averageRadon = (String) getCellFormatValue(row.getCell(1));
        String mistake = (String) getCellFormatValue(row.getCell(2));
        return REGION_RADON.equals(regionRadon) && REGION.equals(region) &&
                !StringUtils.isEmpty(averageRadon) && averageRadon.startsWith(AVERAGE_RADON)
                && !StringUtils.isEmpty(mistake) && mistake.startsWith(MISTAKE);
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
    public List<RegionRadon> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) {
        List<RegionRadon> regionRadons = regionRadonMapper.getRegionRadons(incloudDel);
        double average;
        double total = 0;
        //计算总体平均TN浓度值
        if (!CollectionUtils.isEmpty(regionRadons)) {
            for (RegionRadon regionRadon :
                    regionRadons) {
                try {
                    total += Double.valueOf(regionRadon.getAverageRadon());
                } catch (NumberFormatException ignored) {
                }
            }
            if (regionRadons.size() > 0) {
                average = total / regionRadons.size();
                BigDecimal b = new BigDecimal(average);
                average = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                regionRadons.get(0).setAverage(average);
            }
        }
        return regionRadons;
    }

    @Override
    public List<DosageHeader> getHeader() {
        return null;
    }

    public List<RegionRadon> getHistory(int parameter) throws Exception {
        RegionRadon regionRadon = this.baseMapper.selectById(parameter);
        return this.baseMapper.getHistory(regionRadon.getRegion());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return RegionRadonHeader.getDosageDbHeader();
    }
}
