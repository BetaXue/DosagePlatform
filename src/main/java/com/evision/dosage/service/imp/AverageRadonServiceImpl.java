package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.averageRadon.AverageRadonHeader;
import com.evision.dosage.mapper.AverageRadonMapper;
import com.evision.dosage.pojo.entity.AverageRadon;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.AverageRadonService;
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
import java.util.Collections;
import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/19
 * 平均氡浓度
 */
@Service
@DosageServiceConfig("averageRadon")
@Slf4j
public class AverageRadonServiceImpl extends ServiceImpl<AverageRadonMapper, AverageRadon> implements AverageRadonService {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";
    private static final String REGION = "地区";
    private static final String AVERAGE_RADON = "平均氡浓度";
    //模板最大列号
    private static final int COLUMN = 2;
    @Resource
    private AverageRadonMapper averageRadonMapper;

    @Override
    public DosageResponseBody getAverageRadons() {
        List<AverageRadon> averageRadons = averageRadonMapper.getAverageRadons(0);
        return DosageResponseBody.getInstance(1, "success", averageRadons);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAverageRadons(List<AverageRadon> averageRadons) {
        if (CollectionUtils.isEmpty(averageRadons)) {
            return 0;
        }
        List<AverageRadon> lists = averageRadonMapper.getAverageRadons(0);
        if (CollectionUtils.isEmpty(lists)) {
            return averageRadonMapper.insertAverageRadons(averageRadons);
        }
        //查出未修改的数据
        List<AverageRadon> removeData = new ArrayList<>();
        for (AverageRadon averageRadon : averageRadons) {
            for (AverageRadon obj : lists) {
                if (org.apache.commons.lang3.StringUtils.equals(obj.getRegion(), averageRadon.getRegion())) {
                    if (org.apache.commons.lang3.StringUtils.equals(obj.getAverageRadon(), averageRadon.getAverageRadon())) {
                        removeData.add(averageRadon);
                    }
                    break;
                }
            }
        }
        //查出未导入的数据
        List<AverageRadon> deleteData = new ArrayList<>();
        for (AverageRadon obj : lists) {
            boolean exit = false;
            for (AverageRadon averageRadon : averageRadons) {
                if (org.apache.commons.lang3.StringUtils.equals(obj.getRegion(), averageRadon.getRegion())) {
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
            for (AverageRadon averageRadon : deleteData) {
                averageRadonMapper.deleteAverageRadon(averageRadon);
            }
        }
        //剔除未修改的数据，只操作修改的数据
        if (removeData.size() > 0) {
            averageRadons.removeAll(removeData);
        }
        if (CollectionUtils.isEmpty(averageRadons)) {
            return 0;
        }
        for (AverageRadon averageRadon : averageRadons) {
            averageRadonMapper.updateAverageRadon(averageRadon);
        }
        return averageRadonMapper.insertAverageRadons(averageRadons);
    }


    @Override
    public List<AverageRadon> importExcel(MultipartFile multipartFile) {
        //获取Workbook
        Workbook workbook = getWorkbook(multipartFile);
        //取第一个Sheet
        Sheet sheet = workbook.getSheetAt(0);
        //获取最大行数
        int rownum = sheet.getPhysicalNumberOfRows();
        /*//获取最大列数
        int colnum = row.getPhysicalNumberOfCells();*/
        List<AverageRadon> averageRadons = new ArrayList<>();
        for (int i = 1; i < rownum; i++) {
            //获取第i行
            Row row = sheet.getRow(i);
            String region = (String) getCellFormatValue(row.getCell(0));
            String averageRadonStr = (String) getCellFormatValue(row.getCell(1));
            AverageRadon averageRadon = new AverageRadon();
            averageRadon.setRegion(region);
            averageRadon.setAverageRadon(averageRadonStr);
            averageRadon.setCreateTime(LocalDateTime.now());
            averageRadon.setUpdateTime(LocalDateTime.now());
            averageRadon.setDisabled(1);
            averageRadon.setDeleted(0);
            try {
                averageRadon.setUserId(UserUtils.getCurrentUserId());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取用户ID错误！");
            }
            averageRadons.add(averageRadon);
        }
        insertAverageRadons(averageRadons);
        return averageRadons;
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
        if (rownum < 1) return false;
        //获取第一行
        Row row = sheet.getRow(0);
        //获取最大列数
        int colnum = row.getPhysicalNumberOfCells();
        if (colnum != COLUMN) return false;
        String region = (String) getCellFormatValue(row.getCell(0));
        String averageRadon = (String) getCellFormatValue(row.getCell(1));
        return REGION.equals(region) && !StringUtils.isEmpty(averageRadon) && averageRadon.startsWith(AVERAGE_RADON);
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
    public List<AverageRadon> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) {
        List<AverageRadon> averageRadons = averageRadonMapper.getAverageRadons(incloudDel);
        //排序，把平均值固定在list集合最后一个位置
        int j = 0;
        boolean flag = false;
        if (!CollectionUtils.isEmpty(averageRadons)) {
            for (int i = 0; i < averageRadons.size(); i++) {
                if (averageRadons.get(i) != null && averageRadons.get(i).getRegion().contains("平均值")) {
                    j = i;
                    flag = true;
                    break;
                }
            }
            if (flag) Collections.swap(averageRadons, j, averageRadons.size() - 1);
        }
        return averageRadons;
    }

    @Override
    public List<DosageHeader> getHeader() {
        return null;
    }

    public List<AverageRadon> getHistory(int parameter) throws Exception {
        AverageRadon averageRadon = this.baseMapper.selectById(parameter);
        return this.baseMapper.getHistory(averageRadon.getRegion());
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return AverageRadonHeader.getDosageDbHeader();
    }
}
