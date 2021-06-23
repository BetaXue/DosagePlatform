package com.evision.dosage.service.imp;

import com.evision.dosage.mapper.*;
import com.evision.dosage.pojo.entity.user.PermissionEntity;
import com.evision.dosage.pojo.entity.user.RolePermissionRelationshipEntity;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kangwenxuan
 * @version 1.0
 * @date 2020-02-19 18:32
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Resource
    UserMapper userMapper;

    @Resource
    PermissionMapper permissionMapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    RolePermissionRelationshipMapper rolePermissionRelationshipMapper;

    @Resource
    UserRoleRelationshipMapper userRoleRelationshipMapper;

    //todo 我觉得这里这是传入一个列表吧。。
    @Override
    public DosageResponseBody<String> distributionPermission(int roleId, int permissionId) throws Exception {
        RolePermissionRelationshipEntity rolePermissionRelationshipEntity = new RolePermissionRelationshipEntity();
        rolePermissionRelationshipEntity.setRoleId(roleId);
        rolePermissionRelationshipEntity.setPermissionId(permissionId);
        rolePermissionRelationshipEntity.setValid(1);
        rolePermissionRelationshipMapper.insert(rolePermissionRelationshipEntity);
        return DosageResponseBody.success("权限分配成功");
    }

    @Override
    public DosageResponseBody<String> addPermission(PermissionEntity permissionEntity) throws Exception {
        permissionMapper.insertPermission(permissionEntity);
        return DosageResponseBody.success("权限增加成功");
    }

    @Override
    public List<Integer> inquireByRoleId(int roleId) {
        List<Integer> result = new ArrayList<>(1);

        List<PermissionEntity> permissionEntities = rolePermissionRelationshipMapper.queryPermissionsByRoleId(roleId);
        if (permissionEntities != null && permissionEntities.size() > 0) {
            for (PermissionEntity permissionEntity : permissionEntities) {
                result.add(permissionEntity.getPermissionId());
            }
        }
        return result;
    }

    //todo 感觉合理的状况是每次重新分配权限，查出来全删了再赋值一次？
    @Override
    public DosageResponseBody<String> inquireByRoleId(int roleId, int permissionId) throws Exception {
        RolePermissionRelationshipEntity rolePermissionRelationshipEntity = new RolePermissionRelationshipEntity();
        rolePermissionRelationshipEntity.setRoleId(roleId);
        rolePermissionRelationshipEntity.setPermissionId(permissionId);
        rolePermissionRelationshipEntity.setValid(1);
        rolePermissionRelationshipMapper.delete(rolePermissionRelationshipEntity);
        return DosageResponseBody.success("权限取消成功");
    }
}
