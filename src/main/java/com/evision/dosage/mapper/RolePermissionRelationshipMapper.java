package com.evision.dosage.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evision.dosage.pojo.entity.user.PermissionEntity;
import com.evision.dosage.pojo.entity.user.RolePermissionRelationshipEntity;
import com.evision.dosage.pojo.entity.user.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kangwenxuan
 * @date 2020/2/18 19:55
 */
public interface RolePermissionRelationshipMapper {
    /**
     * 根据角色id查询权限
     *
     * @param roleId 权限主键
     * @return 权限实体
     */
    List<PermissionEntity> queryPermissionsByRoleId(@Param("roleId") int roleId);

    /**
     * 给某一角色增加权限
     * @param rolePermissionRelationshipEntity 对应关系实体
     * @return 启用的用户数据
     */
    int insert(RolePermissionRelationshipEntity rolePermissionRelationshipEntity);

    /**
     * 给某一角色删除权限
     * @param rolePermissionRelationshipEntity 对应关系实体
     */
    int delete(RolePermissionRelationshipEntity rolePermissionRelationshipEntity);
}
