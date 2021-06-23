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
public class UserRoleRelationshipEntity extends UserCommonEntity {
    /**
     * 用户ID
     */
    private int userId;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 是否有效 0无效 1有效
     */
    private String valide;
}
