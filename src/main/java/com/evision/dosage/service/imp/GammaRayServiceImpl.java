package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.DosageGammaRayConfig;
import com.evision.dosage.constant.GammaRayEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.GammaRayMapper;
import com.evision.dosage.pojo.entity.GammaRay;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.service.GammaRayService;
import com.evision.dosage.utils.ExcelUtils;
import com.evision.dosage.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 陆地γ射线
 *
 * @Author: Yu Xiao
 * @Date: 2020/2/17 18:43
 */
@Slf4j
@Service
@DosageServiceConfig("gammaRay")
public class GammaRayServiceImpl extends ServiceImpl<GammaRayMapper, GammaRay> implements GammaRayService {
    @Autowired
    private GammaRayMapper gammaRayMapper;
    @Autowired
    private DosageGammaRayConfig dosageGammaRayConfig;

    /**
     * 处理读取的数据到数据库
     *
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean dealData(List<GammaRay> list) throws Exception {
        //点位名称和网格编号唯一确定一条
        if (!CollectionUtils.isEmpty(list)) {
            Map<String, GammaRay> map = new HashMap<>();
            //格式化数据
            for (GammaRay gammaRay : list) {
                if (!"".equals(gammaRay.getGridNumber())) {
                    map.put(gammaRay.getGridNumber() + gammaRay.getPlaceName(), gammaRay);
                }
            }
            log.info("此次上传数据：" + map.size() + "条");
            for (GammaRay gammaRay : map.values()) {
                //查找有效的样本
                QueryWrapper<GammaRay> query = new QueryWrapper<GammaRay>();
                query.eq("grid_number", gammaRay.getGridNumber())
                        .eq("place_name", gammaRay.getPlaceName())
                        .eq("disabled", 1).eq("deleted", 0);
                List<GammaRay> existsEntityList = gammaRayMapper.selectList(query);
                if (CollectionUtils.isEmpty(existsEntityList)) {
                    //新增
                    gammaRay.setCreateTime(LocalDateTime.now());
                    gammaRay.setUserId(UserUtils.getCurrentUserId());
                    gammaRayMapper.insert(gammaRay);
                } else {
                    //数据不一致 此处比较需要重写实体equals方法
                    GammaRay existsEntity = existsEntityList.get(0);
                    if (!gammaRay.equals(existsEntity)) {
                        //将旧记录设为禁用状态
                        existsEntity.setDisabled(0);
                        gammaRayMapper.updateById(existsEntity);
                        //新增
                        gammaRay.setCreateTime(LocalDateTime.now());
                        gammaRay.setUserId(UserUtils.getCurrentUserId());
                        gammaRayMapper.insert(gammaRay);
                    }
                }
            }

            List<GammaRay> existsEntityDb = gammaRayMapper.selectList(new QueryWrapper<GammaRay>().eq("disabled", 1));
            if (!CollectionUtils.isEmpty(existsEntityDb)) {
                for (GammaRay entityDb : existsEntityDb) {
                    boolean flag = false;
                    for (GammaRay gammaRay : map.values()) {
                        if (gammaRay.getGridNumber().equals(entityDb.getGridNumber())
                                && gammaRay.getPlaceName().equals(entityDb.getPlaceName())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        entityDb.setDeleted(1);
                        gammaRayMapper.updateById(entityDb);
                    }
                }
            }
        }
        //重新加载数据
        dosageGammaRayConfig.initData();
        return true;
    }

    @Override
    public List<GammaRay> importExcel(MultipartFile multipartFile) throws Exception {
        List<GammaRay> response = new ArrayList<>();
        Workbook workbook = ExcelUtils.getWorkbook(multipartFile);
        Sheet sheet = workbook.getSheetAt(0);
        String sheetName = sheet.getSheetName();
        String fileName = multipartFile.getOriginalFilename();
        if (fileName.startsWith("陆地伽马辐射") && "合计".equals(sheetName)) {
            List<GammaRay> result = readExcel(sheet);
            // 数据写库
            if (dealData(result)) {
                log.info("excel sheet名称: " + sheetName + "上传成功！");
                //response.addAll(result);
            }
        } else {
            log.error("上传的excel文件与数据源类别不符！");
            throw new DosageException("上传的excel文件与数据源类别不符！");
        }
        return response;

    }

    @Override
    public List<GammaRay> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDe) throws Exception {
        throw new Exception("请调用分页查询接口：getPagingDataByExcelType()");
    }

    @Override
    public List<DosageHeader> getHeader() {
        return GammaRayEnum.convertHeaderEntity();
    }

    public List<GammaRay> getHistory(int parameter) throws Exception {
        GammaRay gammaRay = gammaRayMapper.selectById(parameter);
        QueryWrapper<GammaRay> query = new QueryWrapper<GammaRay>();
        query.eq("place_name", gammaRay.getPlaceName())
                .eq("grid_number", gammaRay.getGridNumber())
                .eq("disabled", 0)
                .eq("deleted", 0)
                .orderByDesc("create_time");
        return gammaRayMapper.selectList(query);
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return GammaRayEnum.convertDbHeaderEntity();
    }

    @Override
    public IPage<GammaRay> getDataList(String queryName, Integer pageSize, Integer pageNum, String sortName, Integer isAsc, Integer incloudDel) throws Exception {
        QueryWrapper<GammaRay> query = new QueryWrapper<GammaRay>();
        query.eq("disabled", 1);
        //模糊查询
        if (!StringUtils.isEmpty(queryName)) {
            query.like("place_name", queryName)
                    .or()
                    .like("surface_type", queryName);
        }
        //排序
        //String defaultSortName = "grid_number";
        boolean defaultIsAsc = true;
        if (isAsc != null && isAsc == 0) {
            defaultIsAsc = false;
        }
        if (!StringUtils.isEmpty(sortName)) {
            String defaultSortName = GammaRayEnum.getDbFieldName(sortName);
            query.orderBy(true, defaultIsAsc, defaultSortName + "+0");
        }
        IPage<GammaRay> page = new Page(pageNum, pageSize);
        return gammaRayMapper.selectPage(page, query);
    }

    /**
     * 伽马辐射读取数据
     *
     * @param sheet
     * @return
     * @throws Exception
     */
    private static List<GammaRay> readExcel(Sheet sheet) throws Exception {
        int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
        List<GammaRay> list = new ArrayList<>();
        for (int rowNum = 1; rowNum < sheet.getLastRowNum(); rowNum++) {
            GammaRay entity = new GammaRay();
            Row row = sheet.getRow(rowNum);
            // 解析公式结果
            FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
            for (int coloum = 3; coloum < coloumNum; coloum++) {
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
                            value = ExcelUtils.numberFormat(cellValue.getNumberValue(), 2);
                        }
                    }
                    switch (coloum) {
                        case 3:
                            entity.setEncryGrid(value);
                            break;
                        case 4:
                            entity.setGridNumber(value);
                            break;
                        case 5:
                            entity.setPlaceName(value);
                            break;
                        case 6:
                            entity.setSurfaceType(value);
                            break;
                        case 30:
                            entity.setAverageValue(value);
                            break;
                        case 31:
                            entity.setMinValue(value);
                            break;
                        case 32:
                            entity.setMaxValue(value);
                            break;
                        default:
                            break;
                    }
                }
            }
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<GammaRay> findByGridNumber(String gridNumber) {
        QueryWrapper<GammaRay> query = new QueryWrapper<GammaRay>();
        IPage<GammaRay> page = new Page(1, 25);
        query.eq("disabled", 1)
                .eq("deleted", 0)
                .eq("grid_number", gridNumber);
        IPage<GammaRay> gammaRayIPage = gammaRayMapper.selectPage(page, query);
        return gammaRayIPage.getRecords();
    }

    @Override
    public List<GammaRay> findAllGroupByGridNumber() {
        QueryWrapper<GammaRay> query = new QueryWrapper<GammaRay>();
        query.eq("disabled", 1)
                .eq("deleted", 0)
                .groupBy("grid_number")
                .orderByAsc("grid_number");
        return gammaRayMapper.selectList(query);
    }
}
