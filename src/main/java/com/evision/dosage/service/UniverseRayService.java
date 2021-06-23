package com.evision.dosage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.UniverseRay;
import com.evision.dosage.pojo.model.DosageResponseBody;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;


/**
 * @Author: Yu Xiao
 * @Date: 2020/2/18 17:07
 */
public interface UniverseRayService extends IService<UniverseRay> {
    /**
     * 上传excel(只上传一次)
     *
     * @param sheet
     * @return
     */
    DosageResponseBody uploadUniverseRayExcel(Sheet sheet);

    /**
     * 按区分组展示剂量率
     * @return
     */
    List<UniverseRay> selectGroupByAreaCode();

}
