package com.evision.dosage.service.imp;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.IndividualizedDoseResultMapper;
import com.evision.dosage.mapper.StatisticalResultMapper;
import com.evision.dosage.mapper.UserRoleRelationshipMapper;
import com.evision.dosage.pojo.entity.GammaRay;
import com.evision.dosage.pojo.entity.individualized.*;
import com.evision.dosage.service.IndividualizedDoseService;
import com.evision.dosage.utils.DoubleUtils;
import com.evision.dosage.utils.UserUtils;
import com.google.gson.*;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @Classname IndividualizedDoseServiceImpl
 * @Description 个体化剂量服务
 * @Date 2020/2/20 下午2:44
 * @Created by liufree
 */
@Service
public class IndividualizedDoseServiceImpl implements IndividualizedDoseService {

    @Resource
    private StatisticalResultMapper statisticalResultMapper;
    @Resource
    private IndividualizedDoseResultMapper individualizedDoseResultMapper;
    @Resource
    private UserRoleRelationshipMapper userRoleRelationshipMapper;
    BeanCopier bc = BeanCopier.create(IndividualizedDose.class, IndividualizedDoseConvert.class,
            false);

    @Override
    public List<IndividualizedItem> compute(IndividualizedDose individualizedDose) {
        List<IndividualizedItem> resultList = new ArrayList<>(35);

        Double[] D = new Double[36];
        for (int i = 0; i <= 35; i++) {
            D[i] = 0d;
        }
        Double J3 = individualizedDose.getStayTime();
        Double F2 = 26.55;
        Double G2 = F2 * 0.365 * (24 - J3);
        Double F3 = 37.39;
        Double G3 = F3 * 0.365 * J3;

        D[2] = G2 + G3;

        Double F4 = 1.67;
        Double G4 = F4 * 0.365 * (24 - J3);
        Double F5 = 3.10;
        Double G5 = F5 * 0.365 * J3;

        D[3] = G4 + G5;

        D[4] = 12.19;

        Double F7 = 98.32;
        Double G7 = F7 * 0.365 * (24 - J3);
        Double F8 = 28.68;
        Double G8 = F8 * 0.365 * J3;
        D[5] = G7 + G8;

        D[6] = 1213.00 + 36.00 + 57.00 + 1.00 + 27.00;
        D[7] = 41.00 + 1.00 + 4.00 + 0.00;
        D[8] = 167.9;

        Integer age = individualizedDose.getAge();
        if (age>=0 && age<=7){
            // 吸入途径
            D[9] = 8.45;
            // 食入途径
            D[10] = 490.79;
        }else if(age>7 && age<=18){
            D[9] = 9.73;
            D[10] = 380.96;
        }else if(age>18){
            D[9] = 10.18;
            D[10] = 207.11;
        }
//        // 吸入途径
//        D[9] = 0.28 + 2.46 + 0.00 + 0.31 + 0.00 + 3.01+3.99;
//        // 食入途径
//        D[10] = 146.45 + 19.14 + 3.10 + 10.25;

        for (int i = 2; i <= 10; i++) {
            D[11] += D[i];
        }
        F2 = 0.15 * individualizedDose.getXray().getChestRight() * 1000;
        F3 = 0.92 * individualizedDose.getXray().getChestSide() * 1000;
        F4 = 0.54 * individualizedDose.getXray().getChestDoubleOblique() * 1000;
        F5 = 1.15 * individualizedDose.getXray().getHeadRight() * 1000;
        Double F6 = 1.03 * individualizedDose.getXray().getHeadSide() * 1000;
        F7 = 0.97 * individualizedDose.getXray().getNeckRight() * 1000;
        F8 = 0.97 * individualizedDose.getXray().getNeckSide() * 1000;
        Double F9 = 0.97 * individualizedDose.getXray().getNeckDoubleOblique() * 1000;
        Double F10 = 2.05 * individualizedDose.getXray().getThoracicVertebraRight() * 1000;
        Double F11 = 2.64 * individualizedDose.getXray().getThoracicVertebraSide() * 1000;
        Double F12 = 1.42 * individualizedDose.getXray().getLumbarVertebraRight() * 1000;
        Double F13 = 2.28 * individualizedDose.getXray().getLumbarVertebraSide() * 1000;
        Double F14 = 1.85 * individualizedDose.getXray().getLumbarVertebraDoubleOblique() * 1000;
        Double F15 = 0d;
        Double F16 = 2.45 * individualizedDose.getXray().getPelvis() * 1000;
        Double F17 = 0.52 * individualizedDose.getXray().getHip() * 1000;
        Double F18 = 0.06 * individualizedDose.getXray().getLimbsAndJoints() * 1000;
        Double F19 = 0d;
        Double F20 = 2.89 * individualizedDose.getXray().getBelly() * 1000;
        Double F21 = 0d;
        Double F22 = 1.04 * individualizedDose.getXray().getChestPerspectiveClinic() * 1000;
        Double F23 = 1.04 * individualizedDose.getXray().getChestPerspectivePhysical() * 1000;
        Double F24 = 0d;

        D[12] = F2 + F3 + F4 + F5 + F6 + F7 + F8 + F9 + F10 + F11 + F12 + F13 + F14 + F15 + F16 + F17 + F18 + F19 + F20 + F21 + F22 + F23 + F24;

        Double F26 = 4 * individualizedDose.getDental().getInsideTheMouth() * 1000d;
        Double F27 = 1.8 * individualizedDose.getDental().getHologram() * 1000d;
        Double F29 = F26 + F27;
        D[13] = F29;

        Double F30 = 1.59 * individualizedDose.getBreast().getHeadAndTailBit() * 1000;
        Double F31 = 1.82 * individualizedDose.getBreast().getInsideSlope() * 1000;
        D[14] = F30 + F31;

        Double F33 = 0.93 * individualizedDose.getCt().getHead() * 1000;
        Double F34 = 3.48 * individualizedDose.getCt().getNeck() * 1000;
        Double F35 = 3.48 * individualizedDose.getCt().getFiveSenses() * 1000;
        Double F36 = 4.6 * individualizedDose.getCt().getChest() * 1000;
        Double F37 = 4.9 * individualizedDose.getCt().getBelly() * 1000;
        Double F38 = 3.48 * individualizedDose.getCt().getThoracicVertebra() * 1000;
        Double F39 = 3.48 * individualizedDose.getCt().getLumbarVertebra() * 1000;
        Double F40 = 3.48 * individualizedDose.getCt().getPelvisAndHip() * 1000;
        Double F41 = 3.48 * individualizedDose.getCt().getPelvicCavity() * 1000;
        Double F42 = 3.48 * individualizedDose.getCt().getLimps() * 1000;
        Double F43 = 3.48 * individualizedDose.getCt().getBloodCTACoronary() * 1000;
        Double F44 = 3.48 * individualizedDose.getCt().getBloodCTANeck() * 1000;
        Double F45 = 3.48 * individualizedDose.getCt().getBloodCTAPeriphery() * 1000;
        Double F46 = 3.48 * individualizedDose.getCt().getOther() * 1000;
        D[15] = F33 + F34 + F35 + F36 + F37 + F38 + F39 + F40 + F41 + F42 + F43 + F44 + F45 + F46;

        Double F48 = 9.11 * individualizedDose.getInterventionalRadiology().getCardiovascular() * 1000;
        Double F49 = 23.1 * individualizedDose.getInterventionalRadiology().getTumor() * 1000;
        Double F50 = 10.6 * individualizedDose.getInterventionalRadiology().getNerve() * 1000;
        Double F51 = 5.7 * individualizedDose.getInterventionalRadiology().getPv() * 1000;
        Double F52 = 8.6 * individualizedDose.getInterventionalRadiology().getNonVascular() * 1000;
        Double F53 = 9.76 * individualizedDose.getInterventionalRadiology().getDiagnose() * 1000;
        D[16] = F48 + F49 + F50 + F51 + F52 + F53;
        D[17] = D[12] + D[13] + D[14] + D[15] + D[16];

        Double E2 = (1.39E-01) * individualizedDose.getMedicalApplication().getDiagnosticRadiology() * 1000;
        Double E3 = (1.38E-01) * individualizedDose.getMedicalApplication().getInterventionalRadiology() * 1000;
        Double E4 = (1.53E-01) * individualizedDose.getMedicalApplication().getInterventionalRadiology() * 1000;
        Double E5 = (2.90E-01) * individualizedDose.getMedicalApplication().getNuclearMedicine() * 1000;
        Double E6 = (1.19E-01) * individualizedDose.getMedicalApplication().getOther() * 1000;
        Double E7 = (1.19E-01) * individualizedDose.getMedicalApplication().getDentalRadiology() * 1000;
        D[18] = E2 + E3 + E4 + E5 + E6 + E7;

        Double E9 = (1.98E-01) * individualizedDose.getIndustrialApplication().getIndustrial() * 1000;
        Double E10 = (8.84E-02) * individualizedDose.getIndustrialApplication().getAcceleratorRunning() * 1000;
        Double E11 = (1.45E-01) * individualizedDose.getIndustrialApplication().getIndustrialIrradiation() * 1000;
        Double E12 = (1.72E-01) * individualizedDose.getIndustrialApplication().getOther() * 1000;
        Double E13 = (1.41E-01) * individualizedDose.getIndustrialApplication().getWellLog() * 1000;
        Double E14 = (4.38E-02) * individualizedDose.getIndustrialApplication().getRadioisotopeProduction() * 1000;
        D[19] = E9 + E10 + E11 + E12 + E13 + E14;

        Double E16 = (2.38E-01) * individualizedDose.getOccupationalExposureOther().getScienceStudy() * 1000;
        Double E17 = (1.20E-01) * individualizedDose.getOccupationalExposureOther().getOther() * 1000;
        Double E18 = (7.73E-02) * individualizedDose.getOccupationalExposureOther().getEducation() * 1000;
        Double E19 = E16 + E17 + E18;
        D[20] = E19;
        D[21] = D[18] + D[19] + D[20];

        D[22] = 40.08 * individualizedDose.getPublicExposure().getDailyCigarette() * 0.365;
        D[23] = individualizedDose.getPublicExposure().getYearlyDomesticFlight() * 3.03 + individualizedDose.getPublicExposure().getYearlyInternationalFlight() * 61.89;
        D[24] = (-12.24 * individualizedDose.getPublicExposure().getDailyBus() - 11.32* individualizedDose.getPublicExposure().getDailySubway() - 6.37* individualizedDose.getPublicExposure().getDailyTaxi() - 8.88 * individualizedDose.getPublicExposure().getDailyCar()) * 0.365;
        D[25] = 0d;
        D[26] = 0d;
        D[27] = 4 * individualizedDose.getPublicExposure().getYearlyGasCooking() / 365d;
        D[28] = 3d;
        D[29] = 1.6;
        D[30] = 0.35;
        D[31] = 0.0007;
        D[32] = 0d;
        D[33] = 49.76 * individualizedDose.getPublicExposure().getYearlyBanana() / 1000;
        D[34] = D[22] + D[23] + D[24] + D[25] + D[26] + D[27] + D[28] + D[29] + D[30] + D[31] + D[32] + D[33];
        D[35] = D[11] + D[17] + D[21] + D[34];

        List<IndividualizedItem> list = new ArrayList<>();
        IndividualizedItem[] in = new IndividualizedItem[35];

        in[2] = new IndividualizedItem("天然辐射", "宇宙射线-电离成份");
        in[3] = new IndividualizedItem("天然辐射", "宇宙射线-中子成份");
        in[4] = new IndividualizedItem("天然辐射", "宇宙射线-宇生放射性核素");
        in[5] = new IndividualizedItem("天然辐射", "陆地伽马辐射");
        in[6] = new IndividualizedItem("天然辐射", "氡、钍射气-氡及其短寿命子体");
        in[7] = new IndividualizedItem("天然辐射", "氡、钍射气-钍射气及其短寿命子体");
        in[8] = new IndividualizedItem("天然辐射", "40K");
        in[9] = new IndividualizedItem("天然辐射", "原生放射性核素-吸入途径");
        in[10] = new IndividualizedItem("天然辐射", "原生放射性核素-食入途径及饮水");
        in[11] = new IndividualizedItem("天然辐射", "天然辐射 合计");
        for (int i = 2; i <= 11; i++) {
            in[i].setYearlyEffectiveDose(doubleToString(D[i]));
            in[i].setComponentProportion(DoubleUtils.divided(D[i],D[11]));
            in[i].setSumDoseProportion(DoubleUtils.divided(D[i] , D[35]));
            list.add(in[i]);
        }
        in[12] = new IndividualizedItem("医疗照射", "普通X射线诊断");
        in[13] = new IndividualizedItem("医疗照射", "牙科摄影");
        in[14] = new IndividualizedItem("医疗照射", "乳腺摄影");
        in[15] = new IndividualizedItem("医疗照射", "计算机断层摄影（CT）");
        in[16] = new IndividualizedItem("医疗照射", "介入放射学");
        in[17] = new IndividualizedItem("医疗照射", "医疗照射 合计");
        for (int i = 12; i <= 17; i++) {
            in[i].setYearlyEffectiveDose(doubleToString(D[i]));
            in[i].setComponentProportion(DoubleUtils.divided(D[i],D[17]));
            in[i].setSumDoseProportion(DoubleUtils.divided(D[i] ,D[35]));
            list.add(in[i]);
        }

        in[18] = new IndividualizedItem("职业照射", "医学应用");
        in[19] = new IndividualizedItem("职业照射", "其他");
        in[20] = new IndividualizedItem("职业照射", "工业应用");
        in[21] = new IndividualizedItem("职业照射", "职业照射 合计");
        for (int i = 18; i <= 21; i++) {
            in[i].setYearlyEffectiveDose(doubleToString(D[i]));
            in[i].setComponentProportion(DoubleUtils.divided(D[i] , D[21]));
            in[i].setSumDoseProportion(DoubleUtils.divided(D[i], D[35]));
            list.add(in[i]);
        }

        in[22] = new IndividualizedItem("公众照射", "抽烟");
        in[23] = new IndividualizedItem("公众照射", "交通方式-民用航空");
        in[24] = new IndividualizedItem("公众照射", "交通方式-轨道交通、公交车及小汽车");
        in[25] = new IndividualizedItem("公众照射", "温泉");
        in[26] = new IndividualizedItem("公众照射", "化石燃料燃烧");
        in[27] = new IndividualizedItem("公众照射", "使用天然气做饭");
        in[28] = new IndividualizedItem("公众照射", "核燃料循环和核电站运行");
        in[29] = new IndividualizedItem("公众照射", "核事故-切尔诺贝利核事故");
        in[30] = new IndividualizedItem("公众照射", "核事故-福岛核事故");
        in[31] = new IndividualizedItem("公众照射", "核设施-核能与新能源研究院");
        in[32] = new IndividualizedItem("公众照射", "核设施-中国原子能科学研究院");
        in[33] = new IndividualizedItem("公众照射", "香蕉");
        in[34] = new IndividualizedItem("公众照射", "公众照射 合计");
        for (int i = 22; i <= 34; i++) {
            in[i].setYearlyEffectiveDose(doubleToString(D[i]));
            in[i].setComponentProportion(DoubleUtils.divided(D[i] , D[34]));
            in[i].setSumDoseProportion(DoubleUtils.divided(D[i] , D[35]));
            list.add(in[i]);
        }
        IndividualizedItem h35 = new IndividualizedItem("合计", "");
        h35.setYearlyEffectiveDose(doubleToString(D[35]));
        h35.setComponentProportion("100%");
        h35.setSumDoseProportion("100%");
        list.add(h35);

        StatisticalResult statisticalResult = new StatisticalResult();
        statisticalResult.setUserId(individualizedDose.getUserId());
        try {
            statisticalResult.setUsername(UserUtils.getCurrentUserName());
        } catch (Exception e) {
            throw new DosageException("无法获取当前用户名字");
        }

        statisticalResult.setSex(individualizedDose.getSex() == 0?"男":"女");
        statisticalResult.setAge(individualizedDose.getAge());
        statisticalResult.setNaturalExposure(doubleToString(D[11]));
        statisticalResult.setMedicalExposure(doubleToString(D[17]));
        statisticalResult.setOccupationalExposure(doubleToString(D[21]));
        statisticalResult.setPublicExposure(doubleToString(D[34]));
        statisticalResult.setSum(doubleToString(D[11] + D[17] + D[21] + D[34]));
        statisticalResult.setCreateTime(LocalDateTime.now());


        saveResult(individualizedDose, statisticalResult, list);

        return list;
    }

    @Override
    public IPage<StatisticalResult> getStatisticalResultList(Integer pageSize, Integer pageNum, Integer userId) {
        IPage<StatisticalResult> statisticalResults;

        IPage<StatisticalResult> page = new Page(pageNum, pageSize);
        int roleId = userRoleRelationshipMapper.queryRoleByUserId(userId);
        if (roleId == 3) { //普通用户
            QueryWrapper<StatisticalResult> query = new QueryWrapper<StatisticalResult>();
            query.eq("user_id", userId);
            statisticalResults = statisticalResultMapper.selectPage(page, query);
        } else {
            QueryWrapper<StatisticalResult> query = new QueryWrapper<StatisticalResult>();
            statisticalResults = statisticalResultMapper.selectPage(page, query);
        }
        return statisticalResults;
    }

    @Override
    public IndividualizedResultJson getSingleResult(Integer id) {
        QueryWrapper<IndividualizedDoseResult> query = new QueryWrapper<>();
        query.eq("id", id);

        IndividualizedDoseResult individualizedDoseResult = individualizedDoseResultMapper.selectOne(query);
        String result = individualizedDoseResult.getResult();
        List<IndividualizedItem> individualizedItemList = parseList(result, IndividualizedItem.class);
        IndividualizedResultJson resultJson = new IndividualizedResultJson();
        resultJson.setUserId(individualizedDoseResult.getUserId());

        String input = individualizedDoseResult.getInput();
        Gson gson = new Gson();
        IndividualizedDoseConvert individualizedDoseConvert = gson.fromJson(input, IndividualizedDoseConvert.class);

        resultJson.setInput(individualizedDoseConvert);
        resultJson.setOutput(individualizedItemList);
        return resultJson;
    }

    /**
     * @param json list的序列化字符串
     * @param <T>  T类型
     * @return List<T>
     */
    public <T> List<T> parseList(String json, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(new Gson().fromJson(elem, clazz));
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }




    /**
     * 转换为string
     *
     * @param d
     * @return
     */
    private String doubleToString(Double d) {
        String str = String.format("%.4f", d);
        str = DoubleUtils.subZeroAndDot(str);
        return str;
    }

    /**
     * 储存结果
     *
     * @param statisticalResult
     * @param list
     */

    @Transactional
    public void saveResult(IndividualizedDose individualizedDose, StatisticalResult statisticalResult, List list) {
        IndividualizedDoseConvert individualizedDoseConvert = new IndividualizedDoseConvert();
        bc.copy(individualizedDose,individualizedDoseConvert,null);
        individualizedDoseConvert.setSex(individualizedDose.getSex()==0?"男":"女");


        Gson gson = new Gson();
        String result = gson.toJson(list);
        String input = gson.toJson(individualizedDoseConvert);
        IndividualizedDoseResult individualizedDoseResult = new IndividualizedDoseResult();
        individualizedDoseResult.setUserId(statisticalResult.getUserId());
        individualizedDoseResult.setInput(input);
        individualizedDoseResult.setResult(result);
        individualizedDoseResult.setCreateTime(LocalDateTime.now());
        individualizedDoseResultMapper.insert(individualizedDoseResult);
        int idvId = individualizedDoseResult.getId();
        statisticalResult.setIdvId(idvId);
        statisticalResultMapper.insert(statisticalResult);
    }
}
