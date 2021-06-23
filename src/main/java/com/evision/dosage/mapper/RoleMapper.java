package com.evision.dosage.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evision.dosage.pojo.entity.user.RoleEntity;
import com.evision.dosage.pojo.entity.user.UserEntity;

import java.util.List;

/**
 * @author kangwenxuan
 * @date 2020/2/18 19:55
 */
public interface RoleMapper {

    /**
     * 增加角色
     * @param roleEntity 角色实体
     * @return 启用的用户数据
     */
    int insertRole(RoleEntity roleEntity);

    /**
     * 删除角色
     * @param roleId 角色实体
     * @return 启用的用户数据
     */
    int deleteRole(String roleId);

    /**
     * 分页查询数据
     *
     * @param page       分页对象
     * @param roleEntity 参数实体
     * @return 分页对象
     */
    IPage<RoleEntity> getPageRole(Page<RoleEntity> page, RoleEntity roleEntity);
}
