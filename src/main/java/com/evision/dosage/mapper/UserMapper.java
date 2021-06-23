package com.evision.dosage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evision.dosage.pojo.entity.user.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/17 19:55
 */
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 读取用户数据
     *
     * @param username 用户名
     * @return 启用的用户数据
     */
    UserEntity queryUsersByName(String username);

    /**
     * 读取用户数据
     *
     * @param userEntity 用户名
     * @return 启用的用户数据
     */
    int insertUser(UserEntity userEntity);

    /**
     * 修改密码
     *
     * @param userId   用户ID
     * @param password 新密码
     */
    int updatePassword(@Param("userId") int userId, @Param("password") String password, @Param("isActivated") int isActivated);

    /**
     * 分页查询数据
     *
     * @param page 分页对象
     * @return 分页对象
     */
    IPage<UserEntity> getPageUser(Page<UserEntity> page);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    int delete(@Param("userId") int userId);

    /**
     * 修改角色
     *
     * @param name   用户名
     * @param roleId 角色ID
     */
    int changeRole(@Param("name") String name, @Param("roleId") int roleId);

    /**
     * 根据用户名称获取用户信息
     *
     * @param username 用户名称
     * @return 获取到用户
     */
    Integer validateUsername(@Param("username") String username);
}
