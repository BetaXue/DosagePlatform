package com.evision.dosage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.FoodWaterRadionuclideEntity;
import com.evision.dosage.pojo.entity.NaturalRadionuclideEntity;
import com.evision.dosage.pojo.model.DosageResponseBody;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/2/21 13:07
 */
public interface NaturalRadionuclideService extends IService<NaturalRadionuclideEntity>,DosageService<NaturalRadionuclideEntity>{

}
