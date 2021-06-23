package com.evision.dosage.pojo.entity.user;

import com.evision.dosage.pojo.entity.user.UserCommonEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DingZhanYang
 * @date 2020/2/17 17:14
 */
@Getter
@Setter
@NoArgsConstructor
public class RolePermissionRelationshipEntity extends UserCommonEntity {
    /**
     * 角色ID
     */
    private int roleId;
    /**
     * 权限ID
     */
    private int permissionId;
    /**
     * 是否有效 0无效 1有效
     */
    private String valide;
}
