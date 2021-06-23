package com.evision.dosage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 气溶胶中放射性核素活度浓度
 * @Author: Yu Xiao
 * @Date: 2020/2/17 18:04
 */
@Data
@TableName("aerosol_radionuclide")
public class AerosolRadionuclideEntity extends DosageCommonEntity{
    @TableId(type = IdType.AUTO)
    private int id;
    //样品编号
    private String sampleNumber;
    //列值：Ra-226（Bq/m3）
    private String ra226;
    //Ra-226不确定度（+-）
    private String ra226Uncertainty;
    //列值：Th-232（Bq/m3）
    private String th232;
    //Th-232不确定度
    private String th232Uncertainty;
    //列值：K-40（Bq/m3）
    private String k40;
    //K-40不确定度
    private String k40Uncertainty;
    //列值：U-238（Bq/m3）
    private String u238;
    //U-238不确定度
    private String u238Uncertainty;
    //列值：Cs-137（Bq/m3）
    private String cs137;
    //Cs-137不确定度
    private String cs137Uncertainty;

    public AerosolRadionuclideEntity() {
        this.setDisabled(1);
        this.setDeleted(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AerosolRadionuclideEntity that = (AerosolRadionuclideEntity) o;
        return Objects.equals(sampleNumber, that.sampleNumber) &&
                Objects.equals(ra226, that.ra226) &&
                Objects.equals(ra226Uncertainty, that.ra226Uncertainty) &&
                Objects.equals(th232, that.th232) &&
                Objects.equals(th232Uncertainty, that.th232Uncertainty) &&
                Objects.equals(k40, that.k40) &&
                Objects.equals(k40Uncertainty, that.k40Uncertainty) &&
                Objects.equals(u238, that.u238) &&
                Objects.equals(u238Uncertainty, that.u238Uncertainty) &&
                Objects.equals(cs137, that.cs137) &&
                Objects.equals(cs137Uncertainty, that.cs137Uncertainty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sampleNumber, ra226, ra226Uncertainty, th232, th232Uncertainty, k40, k40Uncertainty, u238, u238Uncertainty, cs137, cs137Uncertainty);
    }
}
