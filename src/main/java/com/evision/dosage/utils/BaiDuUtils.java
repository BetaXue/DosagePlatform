package com.evision.dosage.utils;

import com.evision.dosage.exception.DosageException;
import com.evision.dosage.pojo.model.BaiDuEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 百度地图接口
 *
 * @author: AubreyXue
 * @date: 2020-03-10 20:19
 **/
@Component
public class BaiDuUtils {

    @Value("${baidu.ak}")
    private String ak;

    /**
     * 转化地址
     */
    private String url = "http://api.map.baidu.com/geoconv/v1/?coords=COORDS&from=1&to=5&ak=AK";

    /**
     * 坐标转化
     *
     * @param x 经度
     * @param y 维度
     * @return 返回百度坐标系对应的经纬度
     */
    public BaiDuEntity convertToBaidu(String x, String y) {
        if (StringUtils.isEmpty(x) || StringUtils.isEmpty(y)) {
            throw new DosageException("转化坐标不能为空");
        }
        String coords = x + "," + y;
        url = url.replace("COORDS", coords).replace("AK", ak);
        return HttpClientUtil.doGet(url);
    }
}
