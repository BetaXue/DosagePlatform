package com.evision.dosage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.user.PermissionEntity;
import com.evision.dosage.pojo.entity.user.UserEntity;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.pojo.model.PageRequest;
import com.evision.dosage.pojo.model.UserLoginResponseBody;

import java.security.Permission;
import java.util.List;

/**
 * @author kangwenxuan
 * @date 2020/2/19 13:51
 */
public interface PermissionService {

    /**
     * 为角色分配一项权限接口
     *
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 状态
     */
    DosageResponseBody<String> distributionPermission(int roleId, int permissionId) throws Exception;

    /**
     * 增加一项新权限
     *
     * @param permissionEntity 权限实体
     * @return 状态
     */
    DosageResponseBody<String> addPermission(PermissionEntity permissionEntity) throws Exception;

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Integer> inquireByRoleId(int roleId) throws Exception;

    /**
     * 取消某个角色的权限
     *
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 状态
     */
    DosageResponseBody<String> inquireByRoleId(int roleId,int permissionId) throws Exception;
}
