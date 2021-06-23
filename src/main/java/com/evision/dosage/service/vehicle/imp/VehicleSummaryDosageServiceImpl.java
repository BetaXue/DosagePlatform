package com.evision.dosage.service.vehicle.imp;

import com.evision.dosage.annotation.DosageServiceConfig;
import com.evision.dosage.constant.vehicle.VehicleSummaryDosageConstantsEnum;
import com.evision.dosage.constant.vehicle.VehicleSummaryDosageHeaderEnum;
import com.evision.dosage.mapper.vehicle.VehicleDosageRateMapper;
import com.evision.dosage.pojo.entity.vehicle.VehicleDosageRateEntity;
import com.evision.dosage.pojo.entity.vehicle.VehicleSummaryDosageEntity;
import com.evision.dosage.pojo.model.DosageDbHeader;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.service.DosageService;
import com.evision.dosage.utils.BigDecimalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author DingZhanYang
 * @date 2020/2/24 9:34
 */
@Slf4j
@Service
@DosageServiceConfig("vehicleSummaryDosage")
public class VehicleSummaryDosageServiceImpl implements DosageService<VehicleSummaryDosageEntity> {

    private static final String SUV = "越野车";
    private static final String CAR = "小轿车";
    @Resource
    VehicleDosageRateMapper vehicleDosageRateMapper;

    public List<VehicleSummaryDosageEntity> importExcel(MultipartFile multipartFile) throws Exception {
        return null;
    }

    public List<VehicleSummaryDosageEntity> getDataList(String queryName, String sortName, Integer isAsc, Integer incloudDel) throws Exception {
        List<VehicleDosageRateEntity> vehicleDosageRateEntityList = vehicleDosageRateMapper.queryValidDataForCompute();
        List<VehicleSummaryDosageEntity> vehicleSummaryDosageEntityList = initDataFromEnum();
        setGammaDosageRate(vehicleDosageRateEntityList, vehicleSummaryDosageEntityList);
        computeOtherValues(vehicleSummaryDosageEntityList);
        addSummary(vehicleSummaryDosageEntityList);
        return vehicleSummaryDosageEntityList;
    }

    public List<VehicleSummaryDosageEntity> getHistory(String parameterOne, String parameterTwo, String parameterThree) throws Exception {
        return null;
    }

    public List<DosageHeader> getHeader() throws Exception {
        return VehicleSummaryDosageHeaderEnum.convertHeaderEntity();
    }

    public List<VehicleSummaryDosageEntity> getHistory(int parameter) throws Exception {
        return null;
    }

    public List<DosageDbHeader> getDbHeader() throws Exception {
        return null;
    }

    /**
     * 从枚举类里获取写死的常量值
     *
     * @return
     */
    private List<VehicleSummaryDosageEntity> initDataFromEnum() {
        List<VehicleSummaryDosageEntity> vehicleSummaryDosageEntityList = new ArrayList<>();
        for (VehicleSummaryDosageConstantsEnum vehicleSummaryDosageConstantsEnum : VehicleSummaryDosageConstantsEnum.values()) {
            VehicleSummaryDosageEntity vehicleSummaryDosageEntity = new VehicleSummaryDosageEntity();
            vehicleSummaryDosageEntity.setEachTravelDuration(BigDecimal.valueOf(vehicleSummaryDosageConstantsEnum.getEachTravelDuration()));
            vehicleSummaryDosageEntity.setVehicleCategory(vehicleSummaryDosageConstantsEnum.getVehicleCategory());
            vehicleSummaryDosageEntity.setWorkdayTravelNumber(vehicleSummaryDosageConstantsEnum.getWorkdayTravelNumber());
            vehicleSummaryDosageEntity.setDaysTravelAveragePersonNumber(BigDecimal.valueOf(vehicleSummaryDosageConstantsEnum.getDaysTravelAveragePersonNumber()));
            vehicleSummaryDosageEntity.setPersonDaysTravelAverageFrequency(BigDecimal.valueOf(vehicleSummaryDosageConstantsEnum.getPersonDaysTravelAverageFrequency()));
            vehicleSummaryDosageEntity.setGammaDosageRate(BigDecimal.valueOf(vehicleSummaryDosageConstantsEnum.getGammaDosageRate()));
            vehicleSummaryDosageEntity.setLandDosageRate(BigDecimal.valueOf(vehicleSummaryDosageConstantsEnum.getLandDosageRate()));
            vehicleSummaryDosageEntityList.add(vehicleSummaryDosageEntity);
        }
        return vehicleSummaryDosageEntityList;
    }

    /**
     * 使用数据库表vehicle_dosage_rate数据计算剂量率
     *
     * @param vehicleDosageRateEntityList
     * @param vehicleSummaryDosageEntityList
     */
    private void setGammaDosageRate(List<VehicleDosageRateEntity> vehicleDosageRateEntityList, List<VehicleSummaryDosageEntity> vehicleSummaryDosageEntityList) {
        // {"小轿车" : 1.1, "越野车" : 1.1, "地铁" : 1.1, "公交车" : 1.1}
        Map<String, List<BigDecimal>> gammaDosageRatesMap = new HashMap<>();
        for (VehicleDosageRateEntity vehicleDosageRateEntity : vehicleDosageRateEntityList) {
            String category = vehicleDosageRateEntity.getVehicleCategory();
            BigDecimal gammaDosageRate;
            if (!SUV.equals(category)) {
                gammaDosageRate = vehicleDosageRateEntity.getCalibrationDosageRateAverage();
            } else {
                gammaDosageRate = vehicleDosageRateEntity.getDosageRateAverage();
            }
            if (gammaDosageRatesMap.containsKey(category)) {
                gammaDosageRatesMap.get(category).add(gammaDosageRate);
            } else {
                List<BigDecimal> gammaDosageRates = new ArrayList<>();
                gammaDosageRates.add(gammaDosageRate);
                gammaDosageRatesMap.put(category, gammaDosageRates);
            }
        }
        Map<String, BigDecimal> gammaDosageRateMap = new HashMap<>();
        for (String key : gammaDosageRatesMap.keySet()) {
            List<BigDecimal> bigDecimals = gammaDosageRatesMap.get(key);
            BigDecimal gammaRateAdd = new BigDecimal(0);
            for (BigDecimal bigDecimal : bigDecimals) {
                gammaRateAdd = BigDecimalUtils.safeAdd(gammaRateAdd, bigDecimal);
            }
            gammaDosageRateMap.put(key, BigDecimalUtils.safeDivide(gammaRateAdd, bigDecimals.size()));
        }
        List<String> preProcessName = Arrays.asList(VehicleSummaryDosageConstantsEnum.BUS.getVehicleCategory(),
                VehicleSummaryDosageConstantsEnum.METRO.getVehicleCategory(),
                VehicleSummaryDosageConstantsEnum.TAXI.getVehicleCategory(),
                VehicleSummaryDosageConstantsEnum.CAR.getVehicleCategory());
        // VehicleSummaryDosageEntity::vehicleCategory {"常规公交", "轨道交通", "出租车", "小汽车"}
        for (VehicleSummaryDosageEntity vehicleSummaryDosageEntity : vehicleSummaryDosageEntityList) {
            String category = vehicleSummaryDosageEntity.getVehicleCategory();
            if (!preProcessName.contains(category)) {
                continue;
            }
            // 小汽车
            if (VehicleSummaryDosageConstantsEnum.CAR.getVehicleCategory().equals(category)) {
                BigDecimal suv = gammaDosageRateMap.get(SUV);
                BigDecimal car = gammaDosageRateMap.get(CAR);
                BigDecimal gammaRate = BigDecimalUtils.safeDivide(BigDecimalUtils.safeAdd(suv, car), 2);
                vehicleSummaryDosageEntity.setGammaDosageRate(gammaRate);
                continue;
            }
            String key = VehicleSummaryDosageConstantsEnum.getNameWithCategory(category);
            vehicleSummaryDosageEntity.setGammaDosageRate(gammaDosageRateMap.get(key));
        }
    }

    /**
     * 计算其它字段值
     *
     * @param vehicleSummaryDosageEntityList
     */
    private void computeOtherValues(List<VehicleSummaryDosageEntity> vehicleSummaryDosageEntityList) {
        for (VehicleSummaryDosageEntity vehicleSummaryDosageEntity : vehicleSummaryDosageEntityList) {
            vehicleSummaryDosageEntity.setDosageRateDiff(BigDecimalUtils.safeSubtract(false, vehicleSummaryDosageEntity.getGammaDosageRate(), vehicleSummaryDosageEntity.getLandDosageRate()));
            BigDecimal daysChange = BigDecimalUtils.safeMultiply(BigDecimalUtils.safeMultiply(vehicleSummaryDosageEntity.getDosageRateDiff(), vehicleSummaryDosageEntity.getEachTravelDuration()), vehicleSummaryDosageEntity.getPersonDaysTravelAverageFrequency());
            vehicleSummaryDosageEntity.setDaysDosageChange(BigDecimalUtils.safeDivide(daysChange, 60));
            vehicleSummaryDosageEntity.setYearDosageChange(BigDecimalUtils.safeMultiply(vehicleSummaryDosageEntity.getDaysDosageChange(), 0.365));
            vehicleSummaryDosageEntity.setYearCollectiveDosage(BigDecimalUtils.safeMultiply(vehicleSummaryDosageEntity.getYearDosageChange(), 21.542));
        }
    }

    /**
     * 添加合计行
     *
     * @param vehicleSummaryDosageEntityList
     */
    private void addSummary(List<VehicleSummaryDosageEntity> vehicleSummaryDosageEntityList) {
        VehicleSummaryDosageEntity summary = new VehicleSummaryDosageEntity();
        Integer workdayTravelNumbers = 0;
        BigDecimal daysDosageChanges = new BigDecimal(0);
        BigDecimal yearDosageChanges = new BigDecimal(0);
        BigDecimal yearCollectiveDosages = new BigDecimal(0);
        for (VehicleSummaryDosageEntity vehicleSummaryDosageEntity : vehicleSummaryDosageEntityList) {
            workdayTravelNumbers += vehicleSummaryDosageEntity.getWorkdayTravelNumber();
            daysDosageChanges = BigDecimalUtils.safeAdd(daysDosageChanges, vehicleSummaryDosageEntity.getDaysDosageChange());
            yearDosageChanges = BigDecimalUtils.safeAdd(yearDosageChanges, vehicleSummaryDosageEntity.getYearDosageChange());
            yearCollectiveDosages = BigDecimalUtils.safeAdd(yearCollectiveDosages, vehicleSummaryDosageEntity.getYearCollectiveDosage());
        }
        summary.setVehicleCategory("合计");
        summary.setWorkdayTravelNumber(workdayTravelNumbers);
        summary.setDaysDosageChange(daysDosageChanges);
        summary.setYearDosageChange(yearDosageChanges);
        summary.setYearCollectiveDosage(yearCollectiveDosages);
        vehicleSummaryDosageEntityList.add(summary);
    }
}
