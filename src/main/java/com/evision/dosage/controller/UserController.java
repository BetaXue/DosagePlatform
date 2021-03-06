package com.evision.dosage.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.evision.dosage.annotation.PassToken;
import com.evision.dosage.constant.DosageConstant;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.RolePermissionRelationshipMapper;
import com.evision.dosage.pojo.model.BaseController;
import com.evision.dosage.pojo.entity.user.UserEntity;
import com.evision.dosage.pojo.model.DosageResponseBody;
import com.evision.dosage.pojo.model.PageRequest;
import com.evision.dosage.pojo.model.UserLoginResponseBody;
import com.evision.dosage.service.UserService;
import com.evision.dosage.utils.CookieUtils;
import com.evision.dosage.utils.MD5Util;
import com.evision.dosage.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author DingZhanYang
 * @date 2020/2/18 11:13
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {
    private UserService userService;

    @Resource
    RolePermissionRelationshipMapper rolePermissionRelationshipMapper;
    @Resource
    private CookieUtils cookieUtils;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PassToken
    @PostMapping(value = "/login")
    public DosageResponseBody<UserLoginResponseBody> login(@RequestBody UserEntity user, HttpServletRequest re, HttpServletResponse httpServletResponse) {
        try {
            return userService.login(user, re, httpServletResponse);
        } catch (Exception e) {
            String errorInfo = "login error: " + e.getMessage();
            log.error(errorInfo);
            cookieUtils.clearCookie(re, httpServletResponse);
            throw new DosageException(e.getMessage());
        }
    }

    @PostMapping(value = "/logon")
    public DosageResponseBody<String> logon(@RequestBody UserEntity user) {
        try {
            return userService.logon(user);
        } catch (Exception e) {
            String errorInfo = "logon error: " + e.getMessage();
            log.error(errorInfo);
            return DosageResponseBody.failure(errorInfo);
        }
    }

    @GetMapping("page")
    public DosageResponseBody getUserPage(PageRequest pageRequest) {
        UserEntity userEntity = new UserEntity();
        try {
            Map<String, Object> dataTable = getDataTable(userService.page(pageRequest, userEntity));
            DosageResponseBody responseBody = DosageResponseBody.success();
            responseBody.setData(dataTable);
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DosageResponseBody.failure("????????????");
    }

    /**
     * ????????????
     *
     * @param re                  re
     * @param httpServletResponse res
     * @return ??????
     */
    @PostMapping(value = "/loginOut")
    public DosageResponseBody loginOut(HttpServletRequest re, HttpServletResponse httpServletResponse) {
        try {
            cookieUtils.clearCookie(re, httpServletResponse);
            return DosageResponseBody.success("????????????");
        } catch (Exception e) {
            String errorInfo = "login error: " + e.getMessage();
            log.error(errorInfo);
            return DosageResponseBody.failure(errorInfo);
        }
    }

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    @GetMapping("getUserInfo")
    public DosageResponseBody getUserInfo() {
        try {
            UserEntity currentUser = UserUtils.getCurrentUser();
            // ???????????????
            UserEntity user = this.userService.getById(currentUser.getId());
            user.setPassword(null);
            user.setId(null);
            return DosageResponseBody.success(user);
        } catch (Exception e) {
            throw new DosageException("???????????????");
        }
    }

    /**
     * ?????????????????????
     *
     * @param valid ????????????
     * @return ????????????
     */
    @PostMapping("stopOrStart")
    public DosageResponseBody stop(@RequestParam Integer userId, @RequestParam Integer valid) {
        try {
            UserEntity userEntity = userService.getById(userId);
            if (userEntity != null) {
                userEntity.setValid(valid);
                userEntity.setUpdateTime(LocalDateTime.now());
            }
            userService.updateById(userEntity);
        } catch (Exception e) {
            throw new DosageException("????????????");
        }
        return DosageResponseBody.success("????????????");
    }

    /**
     * ????????????
     *
     * @param userEntity ????????????
     * @return ????????????
     */
    @PostMapping("add")
    public DosageResponseBody add(@RequestBody UserEntity userEntity) {
        userService.addUser(userEntity);
        return DosageResponseBody.success("????????????");
    }

    /**
     * ????????????
     *
     * @param userEntity ????????????
     * @return ????????????
     */
    @PostMapping("update")
    public DosageResponseBody update(@RequestBody UserEntity userEntity) {
        try {
            userService.updateById(userEntity);
        } catch (Exception e) {
            throw new DosageException("????????????");
        }
        return DosageResponseBody.success();
    }


    /**
     * ????????????
     *
     * @param userEntity ????????????
     * @return ????????????
     */
    @PostMapping("delete")
    public DosageResponseBody delete(UserEntity userEntity) {
        try {
            userEntity.setValid(0);
            userEntity.setUpdateTime(LocalDateTime.now());
            userService.updateById(userEntity);
        } catch (Exception e) {
            throw new DosageException("????????????");
        }
        return DosageResponseBody.success();
    }

    /**
     * ????????????
     *
     * @return ????????????
     */
    @PostMapping("resetPassword")
    public DosageResponseBody resetPassword() {
        try {
            UserEntity currentUser = UserUtils.getCurrentUser();
            userService.resetPassword(currentUser.getId());
        } catch (Exception e) {
            throw new DosageException("????????????");
        }
        return DosageResponseBody.success();
    }

    /**
     * ????????????
     *
     * @param userEntity ?????????
     * @return ????????????
     */
    @PostMapping("changePassword")
    public DosageResponseBody resetPassword(@RequestBody UserEntity userEntity) {
        try {
            UserEntity currentUser = UserUtils.getCurrentUser();
            userService.changePassword(currentUser, userEntity.getPassword());
        } catch (Exception e) {
            throw new DosageException("????????????");
        }
        return DosageResponseBody.success();
    }

    /**
     * ??????????????????
     */
    @PostMapping("getUserPermission")
    public DosageResponseBody getUserPermission() {
        try {
            UserEntity currentUser = UserUtils.getCurrentUser();
            return DosageResponseBody.success(rolePermissionRelationshipMapper.queryPermissionsByRoleId(currentUser.getRoleId()));
        } catch (Exception e) {
            throw new DosageException("????????????????????????!");
        }
    }

    /**
     * ????????????
     *
     * @param userEntity ??????
     * @return ????????????
     */
    @PostMapping("changeRole")
    public DosageResponseBody changeRole(@RequestBody UserEntity userEntity) {
        try {
            userService.changeRole(userEntity.getId(), userEntity.getRoleId());
        } catch (Exception e) {
            throw new DosageException("????????????");
        }
        return DosageResponseBody.success("????????????");
    }

    /**
     * ???????????????????????????
     *
     * @return ?????????????????????????????????
     */
    @PassToken
    @GetMapping("/resetAdminPassword/{uuid}")
    public DosageResponseBody resetAdminPassword(@PathVariable String uuid) {
        if (!StringUtils.isEmpty(uuid) && DosageConstant.ADMIN_TOKEN.equals(uuid)) {
            UserEntity userEntity = this.userService.getById(1);
            userEntity.setIsActivated(0);
            userEntity.setPassword(MD5Util.getMD5(DosageConstant.DEFAULT_PASS));
            this.userService.updateById(userEntity);
        } else {
            throw new DosageException("????????????Token?????????????????????????????????!");
        }
        return DosageResponseBody.success("????????????,????????????:1QAZ2wsx!@");
    }


}
