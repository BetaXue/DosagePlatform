package com.evision.dosage.pojo.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author DingZhanYang
 * @date 2020/2/17 17:14
 */
@Data
@TableName("permission")
public class PermissionEntity extends UserCommonEntity {
    /**
     * 权限ID
     */
    @TableId(type = IdType.AUTO)
    private int permissionId;
    /**
     * 权限内容
     */
    private String permissionContent;
    /**
     * 是否有效 0无效 1有效
     */
    private String valide;
}
