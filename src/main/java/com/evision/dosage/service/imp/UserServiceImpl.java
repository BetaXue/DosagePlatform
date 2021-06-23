package com.evision.dosage.service.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evision.dosage.constant.DosageConstant;
import com.evision.dosage.constant.ValidConstant;
import com.evision.dosage.exception.DosageException;

import com.evision.dosage.mapper.RolePermissionRelationshipMapper;
import com.evision.dosage.mapper.UserMapper;
import com.evision.dosage.pojo.entity.user.UserEntity;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.pojo.model.PageRequest;
import com.evision.dosage.pojo.model.UserLoginResponseBody;
import com.evision.dosage.service.UserService;
import com.evision.dosage.utils.CookieUtils;
import com.evision.dosage.utils.MD5Util;
import com.evision.dosage.utils.TokenUtils;

import com.evision.dosage.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-18 18:32
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private CookieUtils cookieUtils;

    @Override
    public DosageResponseBody<UserLoginResponseBody> login(UserEntity user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userName = user.getUsername();
        String password = user.getPassword();
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            throw new IllegalAccessException("用户名或密码输入为空");
        }
        UserEntity dbUser = userMapper.queryUsersByName(userName);
        if (dbUser == null) {
            throw new IllegalAccessException("用户不存在");
        }
        if (dbUser.getValid() == 0) {
            throw new IllegalAccessException("用户已被停用");
        }
        String originPassword = dbUser.getPassword();
        System.out.println(MD5Util.getMD5(password));
        if (!originPassword.equals(MD5Util.getMD5(password))) {
            throw new IllegalAccessException("密码错误");
        }
        String token = TokenUtils.createJwtToken(String.valueOf(dbUser.getId()));
        String formattedDateTime = LocalDateTime.now().toString();
        cookieUtils.setCookie(request, response, token);
        return DosageResponseBody.success(new UserLoginResponseBody(token, formattedDateTime));
    }

    @Override
    public DosageResponseBody<String> logon(UserEntity user) throws Exception {
        String userName = user.getUsername();
        String password = user.getPassword();
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            throw new IllegalAccessException("用户名或密码输入为空");
        }
        UserEntity users = userMapper.queryUsersByName(userName);
        if (users != null) {
            throw new IllegalAccessException("用户存在");
        }
        user.setPassword(MD5Util.getMD5(password));
        user.setValid(1);
        user.setOperatorId(UserUtils.getCurrentUserId());
        userMapper.insertUser(user);
        return DosageResponseBody.success("注册成功");
    }

    @Override
    public IPage<UserEntity> page(PageRequest pageRequest, UserEntity userEntity) {
        Page<UserEntity> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        return this.baseMapper.getPageUser(page);
    }

    @Override
    public DosageResponseBody<String> changePassword(UserEntity user, String newPasswotd) {
        userMapper.updatePassword(user.getId(), MD5Util.getMD5(newPasswotd), 1);
        return DosageResponseBody.success("更新成功，请重新登陆");
    }

    @Override
    public DosageResponseBody<String> resetPassword(int userId) {
        userMapper.updatePassword(userId, MD5Util.getMD5(DosageConstant.DEFAULT_PASS), 0);
        return DosageResponseBody.success("重置成功，请重新登陆");
    }

    @Override
    public DosageResponseBody<String> delete(int userId) throws Exception {

        userMapper.delete(userId);
        return DosageResponseBody.success("删除用户成功");
    }

    @Override
    public UserEntity addUser(UserEntity userEntity) {
        if (userEntity != null) {
            //查看用户是不是存在
            Integer count = this.baseMapper.validateUsername(userEntity.getUsername());
            if (count > 0) {
                throw new DosageException("用户已注册");
            }
            try {
                userEntity.setPassword(MD5Util.getMD5(DosageConstant.DEFAULT_PASS));
                userEntity.setValid(ValidConstant.VALID.getValue());
                this.baseMapper.insert(userEntity);
                return userEntity;
            } catch (Exception e) {
                log.error("添加用户异常：{}", e);
            }
        }
        throw new DosageException("创建用户失败");
    }

    @Override
    public Integer changeRole(Integer userId, int roleId) {
        UserEntity userEntity = this.baseMapper.selectById(userId);
        userEntity.setRoleId(roleId);
        userEntity.setUpdateTime(LocalDateTime.now());
        return this.baseMapper.updateById(userEntity);
    }

}
