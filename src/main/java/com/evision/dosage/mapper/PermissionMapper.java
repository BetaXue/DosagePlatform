package com.evision.dosage.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evision.dosage.pojo.entity.user.PermissionEntity;
import com.evision.dosage.pojo.entity.user.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kangwenxuan
 * @date 2020/2/18 19:55
 */
public interface PermissionMapper {

    /**
     * 增加权限
     * @param permissionEntity 权限实体
     */
    int insertPermission(PermissionEntity permissionEntity);
    /**
     * 删除权限
     * @param permissionId 权限ID
     */
    int deletePermission(@Param("permissionId") String permissionId);
    /**
     * 查询权限ID
     * @param permissionContent 权限内容url
     */
    List<Integer> quaryId(@Param("permissionContent") String permissionContent);

    /**
     * 分页查询数据
     *
     * @param page       分页对象
     * @param permissionEntity 参数实体
     * @return 分页对象
     */
    IPage<PermissionEntity> getPagePermission(Page<UserEntity> page, PermissionEntity permissionEntity);

    /**
     * 查询权限内容
     * @param permissionId 权限ID
     */
    List<String> quaryContent(@Param("permissionId") int permissionId);
}
