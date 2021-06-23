package com.evision.dosage.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.pojo.model.DosageResponseBody;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DingZhanYang
 * @date 2020/2/13 15:12
 */
public class ExcelUtils {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @param file excel 文件
     * @return excel workbook
     * @throws IOException
     */
    public static Workbook getWorkbook(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file); // 文件流
        if (file.getName().endsWith(EXCEL_XLS)) {
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @param multipartFile excel 文件
     * @return excel workbook
     * @throws IOException
     */
    public static Workbook getWorkbook(MultipartFile multipartFile) throws Exception {
        Workbook wb = null;
        InputStream inputStream = multipartFile.getInputStream();
        String fileName = multipartFile.getOriginalFilename();
        FileInputStream fileInputStream;
        if (inputStream instanceof FileInputStream) {
            fileInputStream = (FileInputStream) inputStream;
        } else {
            throw new DosageException("上传数据非Excel文件数据");
        }
        if (!StringUtils.isEmpty(fileName)) {
            if (fileName.endsWith(EXCEL_XLS)) {
                wb = new HSSFWorkbook(fileInputStream);
            } else if (fileName.endsWith(EXCEL_XLSX)) {
                wb = new XSSFWorkbook(fileInputStream);
            }
        } else {
            throw new DosageException("上传数据Excel文件名为空");
        }
        return wb;
    }

    /**
     * 根据sheet表格名获取Sheet对象
     * @param workbook excel工作表
     * @param dosageExcelEnum sheet表格名枚举
     * @return Sheet
     * @throws Exception 获取失败
     */
    public static Sheet getSheet(Workbook workbook, DosageExcelEnum dosageExcelEnum) throws Exception{
        int sheetCount = workbook.getNumberOfSheets();
        for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex += 1) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            String sheetName = sheet.getSheetName();
            if (dosageExcelEnum.getExcelName().equals(sheetName)) {
                return sheet;
            }
        }
        throw new DosageException("Excel中不包含Sheet: " +  dosageExcelEnum.getExcelName());
    }

    /**
     * 判断文件是否是Excel文件
     *
     * @throws Exception
     */
    public static void checkExcelVaild(File file) throws Exception {
        if (!file.exists()) {
            throw new Exception("文件不存在");
        }
        if (!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))) {
            throw new Exception("文件不是Excel");
        }
    }

    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet  Excel sheet
     * @param row    行下标
     * @param column 列下标
     * @return true: 是
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取合并单元格的值
     *
     * @param sheet  Excel sheet
     * @param row    行下标
     * @param column 列下标
     * @return 单元格
     */
    public static Cell getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    return fRow.getCell(firstColumn);
                }
            }
        }

        return null;
    }

    /**
     * 小数格式化
     *
     * @param fractionDigits 最大保存位数
     * @param num
     * @return
     */
    public static String numberFormat(double num, int fractionDigits) {
        NumberFormat df = NumberFormat.getInstance();
        //默认以“,”分组
        df.setGroupingUsed(false);
        //最大小数点位置，可避免0冗余
        df.setMaximumFractionDigits(fractionDigits);
        return df.format(num);
    }

    /**
     * 读取excel与实体映射
     *
     * @param sheet
     * @param c              实体对象class
     * @param cellKeys       map中的key要与实体属性对应，即表头的列名，顺序也要一致
     * @param fromRow        从哪一行开始读（下标值）例如：从第二行开始获取数据，则fromRow=1
     * @param fromColoum     从哪一列开始读（下标值）
     * @param fractionDigits 最大小数位
     * @return 实体集合
     * @throws Exception
     */
    public static List readExcel(Sheet sheet, Class c, List<String> cellKeys, int fromRow, int fromColoum, int fractionDigits) throws Exception {
        int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
        List list = new ArrayList<>();
        Map<String, String> valueMap = new HashMap<String, String>();
        for (int rowNum = fromRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            Object o = c.newInstance();
            // 解析公式结果
            FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
            for (int coloum = fromColoum; coloum < coloumNum; coloum++) {
                String value = "";
                if (row != null) {
                    Cell cell = row.getCell(coloum);
                    // 经过公式解析，最后只存在Boolean、Numeric和String三种数据类型，此外就是Error了
                    CellValue cellValue = evaluator.evaluate(cell);
                    if (cellValue != null) {
                        if (CellType.STRING.equals(cellValue.getCellType())) {
                            value = cell.getStringCellValue();
                        }
                        if (CellType.NUMERIC.equals(cellValue.getCellType())) {
                            value = ExcelUtils.numberFormat(cellValue.getNumberValue(), fractionDigits);
                        }
                    }
                    valueMap.put(cellKeys.get(coloum), value);
                    BeanUtils.populate(o, valueMap);
                }
            }
            list.add(o);
        }
        return list;
    }

    /**
     * 从cell中读取字符串，不是字符串类型尝试转型
     * @param cell 单元格
     * @return 字符串值
     */
    public static String getStringValue(Cell cell){
        if (cell == null){
            return null;
        }
        if (CellType.STRING.equals(cell.getCellType())) {
            return cell.getStringCellValue();
        }else if(CellType.NUMERIC.equals(cell.getCellType())){
            return String.valueOf(getIntegerValue(cell));
        }else if (CellType.BOOLEAN.equals(cell.getCellType())) {
            return cell.getBooleanCellValue() ? "true" : "false";
        }else if (CellType.FORMULA.equals(cell.getCellType())) {
            try {
                return String.valueOf(cell.getNumericCellValue());
            } catch (IllegalStateException e) {
                return String.valueOf(cell.getRichStringCellValue());
            }
        }else{
            return null;
        }
    }

    /**
     * 从cell中读取BigDecimal，不是NUMBER类型尝试转型
     * @param cell 单元格
     * @return BigDecimal
     */
    public static BigDecimal getDecimalValue(Cell cell){
        if (cell == null){
            return null;
        }
        Double v = getNumberValue(cell);
        if (v != null) {
            return BigDecimal.valueOf(v).setScale(2, RoundingMode.FLOOR);
        }else{
            return null;
        }
    }

    /**
     * 从cell中读取BigDecimal，不是NUMBER类型尝试转型
     * @param cell 单元格
     * @param scale 小数点后保留位数
     * @return BigDecimal
     */
    public static BigDecimal getDecimalValue(Cell cell, int scale){
        if (cell == null){
            return null;
        }
        Double v = getNumberValue(cell);
        if (v != null) {
            return BigDecimal.valueOf(v).setScale(scale, RoundingMode.FLOOR);
        }else{
            return null;
        }
    }

    /**
     * 从cell中读取整数，不是NUMBER类型尝试转型
     * @param cell 单元格
     * @return 整数
     */
    public static Integer getIntegerValue(Cell cell){
        if (cell == null){
            return null;
        }
        Double v = getNumberValue(cell);
        if (v != null) {
            return v.intValue();
        }else{
            return null;
        }
    }

    /**
     * 从cell中读取Double，不是NUMBER类型尝试转型
     * @param cell 单元格
     * @return double
     */
    public static Double getNumberValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            return cell.getNumericCellValue();
        } else if (CellType.STRING.equals(cell.getCellType())) {
            try {
                return Double.valueOf(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (CellType.BOOLEAN.equals(cell.getCellType())) {
            return cell.getBooleanCellValue() ? 1. : 0.;
        } else if (CellType.FORMULA.equals(cell.getCellType())) {
            try {
                return Double.valueOf(String.valueOf(cell.getNumericCellValue()));
            } catch (IllegalStateException e) {
                return Double.valueOf(String.valueOf(cell.getRichStringCellValue()));
            }
        } else {
            return null;
        }
    }
}
