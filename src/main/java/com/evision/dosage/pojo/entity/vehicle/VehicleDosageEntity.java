package com.evision.dosage.pojo.entity.vehicle;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evision.dosage.pojo.entity.DosageCommonEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 车辆Gamma剂量监测数据
 * @author DingZhanYang
 * @date 2020/2/20 11:24
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("vehicle_dosage")
public class VehicleDosageEntity extends DosageCommonEntity{
    /**
     * ID
     */
    private int id;
    /**
     * 数据类别
     */
    private String dataCategory;
    /**
     * 位置
     */
    private String location;
    /**
     * 名称
     */
    private String roadName;
    /**
     * 公交线路
     */
    private String route;
    /**
     * 行驶状态
     */
    private String drivingState;
    /**
     * 监测数量
     */
    private Integer monitorNumber;
    /**
     * 测量结果最大值范围
     * 单位 nSv/h
     */
    private String measureResultMaxRange;
    /**
     * 测量结果最小值范围
     * 单位 nSv/h
     */
    private String measureResultMinRange;
    /**
     * 测量结果均值范围
     * 单位 nSv/h
     */
    private String measureResultAverageRange;
}
