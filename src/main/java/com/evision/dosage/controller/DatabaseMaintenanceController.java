package com.evision.dosage.controller;

import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.manager.DosageServiceManager;
import com.evision.dosage.manager.PagingDosageServiceManager;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.DosageService;
import com.evision.dosage.service.PagingDosageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据库维护，控制器
 *
 * @author lijn
 * @version 1.0
 * @date 2020/2/19 14:25
 */
@Slf4j
@RestController
@RequestMapping(value = "/dosage")
public class DatabaseMaintenanceController {

    private DosageServiceManager dosageServiceManager;
    private PagingDosageServiceManager pagingDosageServiceManager;

    @Autowired
    public DatabaseMaintenanceController(DosageServiceManager dosageServiceManager, PagingDosageServiceManager pagingDosageServiceManager) {
        this.dosageServiceManager = dosageServiceManager;
        this.pagingDosageServiceManager = pagingDosageServiceManager;
    }

    /**
     * 查询数据库维护页面下拉框
     *
     * @return 查询结果
     */
    @PostMapping(value = "/all/excel")
    public DosageResponseBody<Map> getAllExcel() {
        // 遍历DosageTableNameEnum，获取Excel名称和类型
        Map<String, String> map = Arrays.stream(DosageExcelEnum.values())
                .filter(o -> !o.equals(DosageExcelEnum.VEHICLE_SUMMARY_DOSAGE))
                .collect(Collectors.toMap(DosageExcelEnum::getExcelName, DosageExcelEnum::getExcelType));

        return DosageResponseBody.success(map);
    }

    /**
     * 根据excelType查询不同数据表的表头
     *
     * @param excelType excel的类别
     * @return 数据集合
     * @link com.evision.dosage.constant.DosageExcelEnum
     */
    @GetMapping("getHeader/{excelType}")
    public DosageResponseBody getHeader(@PathVariable String excelType) {
        try {
            DosageService dosageService = dosageServiceManager.getDosageService(excelType);
            return DosageResponseBody.success(dosageService.getHeader());
        } catch (Exception e) {
            String errorInfo = "getHeader error: " + e.getMessage();
            log.error(errorInfo);
            return DosageResponseBody.failure(errorInfo);
        }
    }

    /**
     * 根据excelType查询不同数据表的表头，用于数据库页面
     *
     * @param excelType excel的类别
     * @return 数据集合
     * @link com.evision.dosage.constant.DosageExcelEnum
     */
    @GetMapping("getDbHeader/{excelType}")
    public DosageResponseBody getDbHeader(@PathVariable String excelType) {
        try {
            DosageService dosageService = dosageServiceManager.getDosageService(excelType);
            return DosageResponseBody.success(dosageService.getDbHeader());
        } catch (Exception e) {
            String errorInfo = "getDbHeader error: " + e.getMessage();
            log.error(errorInfo);
            return DosageResponseBody.failure(errorInfo);
        }
    }


    /**
     * 根据excelType查询不同数据表的数据
     *
     * @param excelType excel的类别
     * @param queryName 模糊查询参数
     * @param sortName  排序字段
     * @param isAsc     是否正序
     * @return 数据集合
     * @link com.evision.dosage.constant.DosageExcelEnum
     */
    @GetMapping("getDataByExcelType/{excelType}")
    public DosageResponseBody getDataByExcelType(@PathVariable String excelType, @RequestParam(required = false) String queryName, @RequestParam(required = false) String sortName, @RequestParam(required = false) Integer isAsc, @RequestParam(required = false, defaultValue = "0") Integer incloudDel) {
        try {
            DosageService dosageService = dosageServiceManager.getDosageService(excelType);
            return DosageResponseBody.success(dosageService.getDataList(queryName, sortName, isAsc, incloudDel));
        } catch (Exception e) {
            String errorInfo = "getDataByExcelType error: " + e.getMessage();
            log.error(errorInfo);
            return DosageResponseBody.failure(errorInfo);
        }
    }

    /**
     * 根据excelType查询不同数据表的数据，分页查询
     *
     * @param excelType excel的类别
     * @param queryName 模糊查询参数
     * @param pageSize  每页数量
     * @param pageNum   当前页
     * @param sortName  排序字段
     * @param isAsc     是否正序
     * @return 数据集合
     * @link com.evision.dosage.constant.DosageExcelEnum
     */
    @GetMapping("getPagingDataByExcelType/{excelType}")
    public DosageResponseBody getPagingDataByExcelType(@PathVariable String excelType, @RequestParam(required = false) String queryName, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) String sortName, @RequestParam(required = false) Integer isAsc, @RequestParam(required = false, defaultValue = "0") Integer incloudDel) {
        try {
            pageNum = pageNum == null ? 1 : pageNum;
            pageSize = pageSize == null ? 10 : pageSize;
            PagingDosageService dosageService = pagingDosageServiceManager.getDosageService(excelType);
            return DosageResponseBody.success(dosageService.getDataList(queryName, pageSize, pageNum, sortName, isAsc, incloudDel));
        } catch (Exception e) {
            String errorInfo = "getDataByExcelType error: " + e.getMessage();
            log.error(errorInfo);
            return DosageResponseBody.failure(errorInfo);
        }
    }

    /**
     * 根据excelType查询不同数据表的数据
     *
     * @param excelType excel的类别
     * @param id 主键ID
     * @return 数据集合
     * @link com.evision.dosage.constant.DosageExcelEnum
     */
    @GetMapping("getHistory/{excelType}")
    public DosageResponseBody getHistory(@PathVariable String excelType, @RequestParam(required = false) Integer id) {
        try {
            DosageService dosageService = dosageServiceManager.getDosageService(excelType);
            return DosageResponseBody.success(dosageService.getHistory(id));
        } catch (Exception e) {
            String errorInfo = "getHistory error: " + e.getMessage();
            log.error(errorInfo);
            return DosageResponseBody.failure(errorInfo);
        }
    }

    /**
     * 通用导入excel接口
     *
     * @param file      上传文件
     * @param excelType Excel英文类别
     * @return 返回对象信息
     */
    @PostMapping(value = "/import/{excelType}")
    public DosageResponseBody uploadBusExcel(@RequestParam("file") MultipartFile file, @PathVariable String excelType) {
        try {
            DosageService dosageService = dosageServiceManager.getDosageService(excelType);
            return DosageResponseBody.success(dosageService.importExcel(file));
        } catch (DosageException e) {
            return DosageResponseBody.failure("上传文件类型与模版文件不匹配，请仔细检查当前上传excel: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return DosageResponseBody.failure("上传文件类型与模版文件不匹配，请仔细检查当前上传excel");
        }
    }

}
