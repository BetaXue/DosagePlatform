package com.evision.dosage.manager;

import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.service.DosageService;
import com.evision.dosage.service.PagingDosageService;
import org.springframework.stereotype.Component;

/**
 * @author DingZhanYang
 * @date 2020/2/23 8:12
 */
@Component
public class PagingDosageServiceManager extends DosageServiceManager {
    @Override
    public PagingDosageService getDosageService(String type) throws Exception{
        DosageExcelEnum dosageExcelEnum = DosageExcelEnum.of(type);
        if (dosageExcelEnum == null || !dosageServiceMap.containsKey(dosageExcelEnum)) {
            throw new IllegalAccessException(type + "类型无分页查询服务");
        }
        DosageService dosageService = dosageServiceMap.get(dosageExcelEnum);
        if (dosageService instanceof PagingDosageService){
            return (PagingDosageService)dosageService;
        }else{
            throw new IllegalAccessException(type + "类型无分页查询服务");
        }
    }
}
