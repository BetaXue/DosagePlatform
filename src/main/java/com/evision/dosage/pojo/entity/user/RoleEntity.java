package com.evision.dosage.pojo.entity.user;

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
public class RoleEntity extends UserCommonEntity{
    /**
     * 角色ID
     */
    private int id;
    /**
     * 角色名
     */
    private String roleName;
}
