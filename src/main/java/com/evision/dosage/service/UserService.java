package com.evision.dosage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.evision.dosage.pojo.entity.user.UserEntity;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.pojo.model.PageRequest;
import com.evision.dosage.pojo.model.UserLoginResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author DingZhanYang
 * @date 2020/2/18 13:51
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 登录接口
     *
     * @param user 用户实体
     * @return 状态
     */
    DosageResponseBody<UserLoginResponseBody> login(UserEntity user, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception;

    /**
     * 注册接口
     *
     * @param user 用户实体
     * @return 状态
     */
    DosageResponseBody<String> logon(UserEntity user) throws Exception;


    /**
     * 用户数据分页查询
     *
     * @param pageRequest 分页实体
     * @param userEntity  用户查询参数
     * @return 用户分页对象
     */
    IPage<UserEntity> page(PageRequest pageRequest, UserEntity userEntity);

    /**
     * 修改密码接口
     *
     * @param userEntity 用户实体
     * @return 状态
     */
    DosageResponseBody<String> changePassword(UserEntity userEntity, String newPassword) throws Exception;

    /**
     * 重置密码接口
     *
     * @param userId 用户ID
     * @return 状态
     */
    DosageResponseBody<String> resetPassword(int userId) throws Exception;

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 状态
     */
    DosageResponseBody<String> delete(int userId) throws Exception;

    /**
     * 创建用户
     *
     * @param userEntity 用户实体
     */
    UserEntity addUser(UserEntity userEntity);

    /**
     * 修改角色
     *
     * @param userId 用户名
     * @param roleId 角色ID
     */
    Integer changeRole(Integer userId, int roleId);
}
