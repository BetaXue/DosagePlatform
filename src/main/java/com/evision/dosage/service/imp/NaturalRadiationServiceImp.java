package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.mapper.NaturalRadiationMapper;
import com.evision.dosage.pojo.entity.NaturalRadiation;
import com.evision.dosage.pojo.entity.NaturalRadiationResponse;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.NaturalRadiationService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/19
 */
@Service
@DosageServiceConfig("naturalRadiation")
@Slf4j
public class NaturalRadiationServiceImp extends ServiceImpl<NaturalRadiationMapper, NaturalRadiation> implements NaturalRadiationService {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";
    private static final String MOLD = "射线源";
    private static final String DOSE_RATE = "剂量率";
    private static final String ANNUAL_DOSE = "年剂量";
    //模板最大行号
    private static final int ROW = 28;
    //模板最大列号
    private static final int COLUMN = 6;
    @Resource
    private NaturalRadiationMapper naturalRadiationMapper;

    @Override
    public DosageResponseBody getNaturalRadiations() {
        List<NaturalRadiation> naturalRadiations = naturalRadiationMapper.getNaturalRadiations(0);
        double total = 0;
        //计算年剂量合计
        if (!CollectionUtils.isEmpty(naturalRadiations)) {
            for (NaturalRadiation naturalRadiation :
                    naturalRadiations) {
                try {
                    total += Double.valueOf(naturalRadiation.getAnnualDose());
                } catch (NumberFormatException ignored) {
                }
            }
        }
        NaturalRadiationResponse naturalRadiationResponse = new NaturalRadiationResponse();
        naturalRadiationResponse.setNaturalRadiations(naturalRadiations);
        naturalRadiationResponse.setTotal(total);
        return DosageResponseBody.getInstance(1, "success", naturalRadiationResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertNaturalRadiations(List<NaturalRadiation> naturalRadiations) {
        if (CollectionUtils.isEmpty(naturalRadiations)) {
            return 0;
        }
        List<NaturalRadiation> lists = naturalRadiationMapper.getNaturalRadiations(0);
        if (CollectionUtils.isEmpty(lists)) {
            return naturalRadiationMapper.insertNaturalRadiations(naturalRadiations);
        }
        //查出未修改的数据
        List<NaturalRadiation> removeData = new ArrayList<>();
        for (NaturalRadiation naturalRadiation : naturalRadiations) {
            for (NaturalRadiation obj : lists) {
                if (obj.getColNumber() == naturalRadiation.getColNumber()) {
                    if (org.apache.commons.lang3.StringUtils.equals(obj.getAnnualDose(), naturalRadiation.getAnnualDose())
                            && org.apache.commons.lang3.StringUtils.equals(obj.getDoseRate(), naturalRadiation.getDoseRate())) {
                        removeData.add(naturalRadiation);
                    }
                    break;
                }
            }
        }
        //剔除未修改的数据，只操作修改的数据
        if(removeData.size() > 0){
            naturalRadiations.removeAll(removeData);
        }
        if (CollectionUtils.isEmpty(naturalRadiations)) {
            return 0;
        }
        for (NaturalRadiation naturalRadiation : naturalRadiations) {
            naturalRadiationMapper.updateNaturalRadiation(naturalRadiation);
        }
        return naturalRadiationMapper.insertNaturalRadiations(naturalRadiations);
    }


    @Override
    public List<NaturalRadiation> importExcel(MultipartFile multipartFile) {
        //获取Workbook
        Workbook workbook = getWorkbook(multipartFile);
        //取第一个Sheet
        Sheet sheet = workbook.getSheetAt(0);
        //获取最大行数
        int rownum = sheet.getPhysicalNumberOfRows();
        /*//获取最大列数
        int colnum = row.getPhysicalNumberOfCells();*/
        List<NaturalRadiation> naturalRadiations = new ArrayList<>();
        for (int i = 1; i < rownum; i++) {
            NaturalRadiation naturalRadiation = new NaturalRadiation();
            //获取第I行
            Row row = sheet.getRow(i);
            String doseRate = (String) getCellFormatValue(row.getCell(4));
            String annualDose = (String) getCellFormatValue(row.getCell(5));
            naturalRadiation.setColNumber(i);
            naturalRadiation.setDoseRate(doseRate);
            naturalRadiation.setAnnualDose(annualDose);
            naturalRadiation.setDisabled(1);
            naturalRadiation.setDeleted(0);
            try {
                naturalRadiation.setUserId(UserUtils.getCurrentUserId());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取用户ID错误！");
            }
            naturalRadiations.add(naturalRadiation);
        }
        insertNaturalRadiations(naturalRadiations);
        return naturalRadiations;
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
        if (rownum != ROW) return false;
        //获取第一行
        Row row = sheet.getRow(0);
        //获取最大列数
        int colnum = row.getPhysicalNumberOfCells();
        if (colnum != COLUMN) return false;
        String mold = (String) getCellFormatValue(row.getCell(1));
        String doseRate = (String) getCellFormatValue(row.getCell(4));
        String annualDose = (String) getCellFormatValue(row.getCell(5));
        return MOLD.equals(mold) && !StringUtils.isEmpty(doseRate) && doseRate.startsWith(DOSE_RATE) && !StringUtils.isEmpty(annualDose) && annualDose.startsWith(ANNUAL_DOSE);
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
    public List<NaturalRadiation> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) {
        List<NaturalRadiation> naturalRadiations = naturalRadiationMapper.getNaturalRadiations(incloudDel);
        double total = 0;
        //计算年剂量合计
        if (!CollectionUtils.isEmpty(naturalRadiations)) {
            for (NaturalRadiation naturalRadiation :
                    naturalRadiations) {
                try {
                    total += Double.valueOf(naturalRadiation.getAnnualDose());
                } catch (NumberFormatException ignored) {
                }
            }
        }
        if (!CollectionUtils.isEmpty(naturalRadiations)) naturalRadiations.get(0).setTotal(total);
        return naturalRadiations;
    }

    @Override
    public List<DosageHeader> getHeader() {
        return null;
    }

    public List<NaturalRadiation> getHistory(int parameter) throws Exception {
        NaturalRadiation naturalRadiation = this.baseMapper.selectById(parameter);
        return this.baseMapper.getHistory(naturalRadiation.getColNumber() + "");
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return null;
    }
}
