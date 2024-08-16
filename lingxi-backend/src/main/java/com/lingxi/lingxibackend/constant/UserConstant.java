package com.lingxi.lingxibackend.constant;

/**
 * 用户常量
 *
 * 
 * 
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    //  region 权限

    /**
     * 默认角色
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员角色
     */
    int ADMIN_ROLE = 1;

    /**
     * 被封号
     */
    int BAN_ROLE = -1;

    // endregion
}
