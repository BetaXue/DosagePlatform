package com.evision.dosage.pojo.entity.vehicle;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evision.dosage.pojo.entity.DosageCommonEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DingZhanYang
 * @date 2020/2/20 11:13
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("vehicle_inner_dosage")
public class VehicleInnerDosageEntity extends DosageCommonEntity {
    /**
     * ID
     */
    private int id;
    /**
     * 数据类别
     */
    private String dataCategory;
    /**
     * 公路类型
     */
    private String roadType;
    /**
     * 车辆类型
     */
    private String vehicleType;
    /**
     * 车厢监测数量
     */
    private Integer monitorNumber;
    /**
     * 测量结果-车厢前侧
     */
    private String frontResultRange;
    /**
     * 测量结果-车厢中部
     */
    private String middleResultRange;
    /**
     * 测量结果-车厢后侧
     */
    private String backResultRange;
    /**
     * 测量结果-车厢范围
     */
    private String entiretyResultRange;
}
