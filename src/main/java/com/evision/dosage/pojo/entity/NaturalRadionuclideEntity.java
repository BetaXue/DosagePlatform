package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 天然放射性核素活度浓度
 *
 * @Author: kangwenxuan
 * @Date: 2020/2/21 15:04
 */
@Data
@TableName("natural_radionuclide")
public class NaturalRadionuclideEntity extends DosageCommonEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    //名称
    private String name;
    //列值：Po-210（Bq/a）
    private String injectPo210;
    //列值：Pb-210（Bq/a）
    private String injectPb210;
    //列值：Ra-226（Bq/a）
    private String injectRa226;
    //列值：Ra-228（Bq/a）
    private String injectRa228;
    //列值：Po-210（nSv/a）
    private String po210;
    //列值：Pb-210（nSv/a）
    private String pb210;
    //列值：Ra-226（nSv/a）
    private String ra226;
    //列值：Ra-228（nSv/a）
    private String ra228;
    //合计
    private String sum;
    //占比
    private String proportion;

    private Integer orders;

    public NaturalRadionuclideEntity() {
        this.setDisabled(1);
        this.setDeleted(0);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NaturalRadionuclideEntity that = (NaturalRadionuclideEntity) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(new BigDecimal(po210).setScale(6,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(that.po210).setScale(6,BigDecimal.ROUND_HALF_UP)),0) &&
                Objects.equals(new BigDecimal(injectPo210).setScale(6,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(that.injectPo210).setScale(6,BigDecimal.ROUND_HALF_UP)), 0) &&
                Objects.equals(new BigDecimal(pb210).setScale(6,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(that.pb210).setScale(6,BigDecimal.ROUND_HALF_UP)), 0) &&
                Objects.equals(new BigDecimal(injectPb210).setScale(6,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(that.injectPb210).setScale(6,BigDecimal.ROUND_HALF_UP)), 0) &&
                Objects.equals(new BigDecimal(ra226).setScale(6,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(that.ra226).setScale(6,BigDecimal.ROUND_HALF_UP)), 0) &&
                Objects.equals(new BigDecimal(injectRa226).setScale(6,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(that.injectRa226).setScale(6,BigDecimal.ROUND_HALF_UP)), 0) &&
                Objects.equals(new BigDecimal(ra228).setScale(6,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(that.ra228).setScale(6,BigDecimal.ROUND_HALF_UP)), 0) &&
                Objects.equals(new BigDecimal(injectRa228).setScale(6,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(that.injectRa228).setScale(6,BigDecimal.ROUND_HALF_UP)), 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, injectPo210, injectPb210, injectRa226, injectRa228, po210, pb210, ra226, ra228, sum, proportion);
    }
}
