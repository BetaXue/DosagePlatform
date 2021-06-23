package com.evision.dosage.manager;

import com.evision.dosage.constant.DosageExcelEnum;
import com.evision.dosage.service.DosageService;
import com.evision.dosage.annotation.DosageServiceConfig;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring 上下文管理器
 *
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-21 13:49
 */
@Component
public class DosageServiceManager implements ApplicationContextAware {
    @Getter(AccessLevel.PROTECTED)
    private ApplicationContext applicationContext;

    protected Map<DosageExcelEnum, DosageService> dosageServiceMap = new HashMap<>();

    public DosageService getDosageService(String type) throws Exception{
        DosageExcelEnum dosageExcelEnum = DosageExcelEnum.of(type);
        if (dosageExcelEnum == null || !dosageServiceMap.containsKey(dosageExcelEnum)) {
            //TODO 抛出异常
        }
        return dosageServiceMap.get(dosageExcelEnum);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        init(applicationContext);
    }

    private void init(ApplicationContext applicationContext) {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(DosageServiceConfig.class);
        for (Object obj : map.values()) {
            if (obj instanceof DosageService) {
                DosageServiceConfig config = AnnotationUtils.findAnnotation(obj.getClass(), DosageServiceConfig.class);
                DosageExcelEnum dosageExcelEnum = DosageExcelEnum.of(config.value());
                if (dosageExcelEnum == null) {
                    //TODO
                }
                dosageServiceMap.put(dosageExcelEnum, (DosageService) obj);
            }
        }
    }
}
