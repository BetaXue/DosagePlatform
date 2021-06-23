package com.evision.dosage.constant;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.evision.dosage.pojo.entity.UniverseRay;
import com.evision.dosage.pojo.model.Coordinate;
import com.evision.dosage.service.UniverseRayService;
import com.evision.dosage.utils.GPSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 宇宙射线初始化到内存
 *
 * @author: AubreyXue
 * @date: 2020-02-29 21:55
 **/
@Component
public class UniverseRayConfig {

    @Autowired
    private UniverseRayService universeRayService;

    private final Map<String, Object> map = new HashMap<>(0);

    /**
     * 初始化数据
     */
    @PostConstruct
    public void initData() {

        for (LngLatEnum value : LngLatEnum.values()) {
            double[] doubles = GPSUtil.gps84_To_bd09(Double.valueOf(value.getLng()), Double.valueOf(value.getLat()));
            BigDecimal lng = BigDecimal.valueOf(doubles[0]).setScale(5, RoundingMode.HALF_UP);
            BigDecimal lat = BigDecimal.valueOf(doubles[1]).setScale(5, RoundingMode.HALF_UP);
            Coordinate c = new Coordinate(lng, lat);
            //西南角、东北角经纬度
            map.put(value.getFieldName(), c);
        }
        //网格N*N
        map.put("cellCount", DosageConstant.UVNIVERSE_RAY_CELL_COUNT);
        List list = new ArrayList();
        List<UniverseRay> universeRays = universeRayService.list(new QueryWrapper<UniverseRay>());
        for (UniverseRay universeRay : universeRays) {
            int[] ints = this.gridNumber2Coordinate(universeRay.getGridNumber());
            Object[] obj = new Object[]{ints[0], ints[1], universeRay};
            list.add(obj);
        }
        map.put("data", list);
    }

    /**
     * 获取map对象
     *
     * @return 获取数据
     */
    public Map<String, Object> getMap() {
        if (map.size() == 0) {
            initData();
        }
        return map;
    }

    /**
     * 网格编号转成坐标
     *
     * @param gridNumber
     * @return
     */
    private static int[] gridNumber2Coordinate(String gridNumber) {
        int[] cellCount = DosageConstant.UVNIVERSE_RAY_CELL_COUNT;
        //行数35
        BigDecimal row = BigDecimal.valueOf(cellCount[1]);
        //列数34
        BigDecimal column = BigDecimal.valueOf(cellCount[0]);
        BigDecimal num = BigDecimal.valueOf(Integer.valueOf(gridNumber));
        // y 坐标的值为 (column*row - num)/column 取商
        //x 坐标的值为 column -（（column*row - num)/column 取余的值）
        BigDecimal[] remainder = (row.multiply(column).subtract(num)).divideAndRemainder(column);
        int x = column.subtract(remainder[1].add(BigDecimal.ONE)).intValue();
        int y = remainder[0].intValue();
        return new int[]{x, y};
    }


}
