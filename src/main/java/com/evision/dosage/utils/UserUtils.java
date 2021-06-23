package com.evision.dosage.utils;

import com.evision.dosage.exception.DosageException;
import com.evision.dosage.pojo.entity.user.UserEntity;

/**
 * 当前用户全局上下文工具类
 *
 * @author DingZhanYang
 * @date 2020/2/18 21:57
 */
public class UserUtils {
    /**
     * 当前登录用户
     */
    private static ThreadLocal<UserEntity> currentUser = new ThreadLocal<>();

    public static UserEntity getCurrentUser() throws Exception {
        checkNull();
        return currentUser.get();
    }

    public static void setCurrentUser(UserEntity user) {
        currentUser.set(user);
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 当前登录用户ID
     * @throws Exception null
     */
    public static int getCurrentUserId() throws Exception {
        // TODO 本地测试，放开注释
        //init();
        checkNull();
        return currentUser.get().getId();
    }

    /**
     * 获取当前登录用户名
     *
     * @return 当前登录用户名
     * @throws Exception null
     */
    public static String getCurrentUserName() throws Exception {
        // TODO 本地测试，放开注释
        //init();
        checkNull();
        return currentUser.get().getUsername();
    }

    private static void checkNull() throws Exception {
        if (currentUser.get() == null) {
            throw new DosageException("当前用户未登录，数据为空");
        }
    }

    // TODO 本地测试，放开注释
//    public static void init(){
//        UserEntity userEntity = new UserEntity();
//        userEntity.setId(1);
//        userEntity.setUsername("admin");
//        setCurrentUser(userEntity);
//    }

}
