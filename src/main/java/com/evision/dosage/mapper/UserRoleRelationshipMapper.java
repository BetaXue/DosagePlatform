package com.evision.dosage.mapper;


import com.evision.dosage.pojo.entity.user.RolePermissionRelationshipEntity;
import com.evision.dosage.pojo.entity.user.UserEntity;
import com.evision.dosage.pojo.entity.user.UserRoleRelationshipEntity;

import java.util.List;

/**
 * @author kangwenxuan
 * @date 2020/2/18 19:55
 */
public interface UserRoleRelationshipMapper {
    /**
     * 根据用户id查询角色
     *
     * @param userId 权限主键
     * @return 角色ID
     */
    int queryRoleByUserId(int userId);

    /**
     * 给某一用户赋予角色
     * @param userRoleRelationshipEntity 对应关系实体
     */
    int insert(UserRoleRelationshipEntity userRoleRelationshipEntity);

}
