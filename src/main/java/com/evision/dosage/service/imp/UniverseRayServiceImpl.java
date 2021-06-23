package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.constant.AreaEnum;
import com.evision.dosage.constant.UniverseRayEnum;
import com.evision.dosage.mapper.UniverseRayMapper;
import com.evision.dosage.pojo.entity.UniverseRay;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.UniverseRayService;
import com.evision.dosage.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 宇宙射线
 *
 * @Author: Yu Xiao
 * @Date: 2020/2/17 18:43
 */
@Slf4j
@Service
public class UniverseRayServiceImpl extends ServiceImpl<UniverseRayMapper, UniverseRay> implements UniverseRayService {
    @Autowired
    private UniverseRayMapper universeRayMapper;

    @Override
    @Deprecated
    public DosageResponseBody uploadUniverseRayExcel(Sheet sheet) {
        try {
            String sheetName = sheet.getSheetName();
            log.info("excel sheet名称: " + sheetName);
            List<String> cellKeys = new ArrayList<>();
            for (UniverseRayEnum value : UniverseRayEnum.values()) {
                cellKeys.add(value.getEntityName());
            }
            Class<UniverseRay> c = UniverseRay.class;
            List<UniverseRay> result = ExcelUtils.readExcel(sheet, c, cellKeys, 1, 0, 2);
            // 数据写库
            if (dealData(result)) {
                return DosageResponseBody.success();
            }
        } catch (Exception e) {
            log.error("宇宙射线 error: " + e.toString());
            return DosageResponseBody.failure(e.toString());
        }
        return null;
    }

    @Override
    public List<UniverseRay> selectGroupByAreaCode() {
        List<UniverseRay> universeRaysDb = universeRayMapper.selectGroupByAreaCode();
        List<UniverseRay> universeRays = new ArrayList<>();
        for (UniverseRay rayDb : universeRaysDb) {
            String areaCode = rayDb.getAreaCode();
            for (AreaEnum areas : AreaEnum.values()) {
                if (areaCode.equals(areas.getAreaCode())) {
                    rayDb.setAreaCode(areas.getAreaName());
                    break;
                }
            }
            universeRays.add(rayDb);
        }
        return universeRays;
    }

    /**
     * 处理读取的数据到数据库
     *
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public boolean dealData(List<UniverseRay> list) throws Exception {
        //只新增
        if (!CollectionUtils.isEmpty(list)) {
            for (UniverseRay universeRay : list) {
                //新增
                universeRay.setCreateTime(LocalDateTime.now());
                //universeRay.setUserId(UserUtils.getCurrentUserId());
                universeRayMapper.insert(universeRay);
            }
        }
        return true;
    }

}
