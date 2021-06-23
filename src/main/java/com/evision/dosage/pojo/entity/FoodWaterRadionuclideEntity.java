package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 食材和饮水中放射性核素活度浓度
 *
 * @Author: Yu Xiao
 * @Date: 2020/2/17 18:04
 */
@Data
@TableName("food_water_radionuclide")
public class FoodWaterRadionuclideEntity extends DosageCommonEntity {
    @TableId(type = IdType.AUTO)
    private int id;
    //样品编号
    private String sampleNumber;
    //列值：Po-210（Bq/m3）
    private String po210;
    //Po-210不确定度
    private String po210Uncertainty;
    //列值：Pb-210（Bq/m3）
    private String pb210;
    //Pb-210不确定度
    private String pb210Uncertainty;
    //列值：Ra-226（Bq/m3）
    private String ra226;
    //Ra-226不确定度
    private String ra226Uncertainty;
    //列值：Ra-228（Bq/m3）
    private String ra228;
    //Ra-228不确定度
    private String ra228Uncertainty;
    //0食物1水样
    private int type;

    public FoodWaterRadionuclideEntity() {
        this.setDisabled(1);
        this.setDeleted(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodWaterRadionuclideEntity that = (FoodWaterRadionuclideEntity) o;
        return Objects.equals(sampleNumber, that.sampleNumber) &&
                Objects.equals(po210, that.po210) &&
                Objects.equals(po210Uncertainty, that.po210Uncertainty) &&
                Objects.equals(pb210, that.pb210) &&
                Objects.equals(pb210Uncertainty, that.pb210Uncertainty) &&
                Objects.equals(ra226, that.ra226) &&
                Objects.equals(ra226Uncertainty, that.ra226Uncertainty) &&
                Objects.equals(ra228, that.ra228) &&
                Objects.equals(ra228Uncertainty, that.ra228Uncertainty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sampleNumber, po210, po210Uncertainty, pb210, pb210Uncertainty, ra226, ra226Uncertainty, ra228, ra228Uncertainty);
    }
}
