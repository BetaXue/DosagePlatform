package com.evision.dosage.service;

import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 导入Excel基类
 *
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-19 16:12
 */
public interface DosageService<T> {

    /**
     * 导入数据接口
     *
     * @param multipartFile 上传文件
     * @return 对象集合
     */
    List<T> importExcel(MultipartFile multipartFile) throws Exception;

    /**
     * 根据文件类别获取数据集合
     *
     * @param queryName 模糊查询字段
     * @param sortName  排序字段
     * @param isAsc     是否正序
     * @return 数据集合
     */
    List<T> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) throws Exception;
    
    /**
     * 获取历史记录数据，使用主键ID
     *
     * @param parameter 主键ID
     * @return 获取此编号的历史记录数据集合
     */
    List<T> getHistory(int parameter) throws Exception;

    /**
     * 获取列表头数据
     *
     * @return 列表头集合
     */
    List<DosageHeader> getHeader() throws Exception;

    /**
     * 获取列表头数据，用于数据库页面
     *
     * @return 列表头集合
     */
    List<DosageDbHeader> getDbHeader() throws Exception;
}
