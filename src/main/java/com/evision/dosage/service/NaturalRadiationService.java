package com.evision.dosage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.NaturalRadiation;
import com.evision.dosage.pojo.entity.NaturalRadiationResponse;
import com.evision.dosage.pojo.model.DosageResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 小宇宙
 * @date 2020/2/19
 * 天然辐射
 */
public interface NaturalRadiationService extends IService<NaturalRadiation>, DosageService<NaturalRadiation> {
    /**
     * 天然辐射 查询数据
     *
     * @return 返回表所有数据
     */
    DosageResponseBody getNaturalRadiations();

    /**
     * 批量插入数据
     *
     * @param naturalRadiations 要插入的数据
     * @return 返回成功条数
     */
    int insertNaturalRadiations(List<NaturalRadiation> naturalRadiations);

    /**
     * 导入数据接口
     *
     * @param multipartFile 上传文件
     * @return 对象集合
     */
    List<NaturalRadiation> importExcel(MultipartFile multipartFile);
}
