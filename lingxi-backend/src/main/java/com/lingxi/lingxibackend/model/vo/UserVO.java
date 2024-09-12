package com.lingxi.lingxibackend.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lingxi.lingxibackend.model.entity.Tag;
import com.lingxi.lingxibackend.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 用户视图（脱敏）
 *
 * 
 * 
 */
@Data
public class UserVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /*
     * 用户简介
     * */
    private String userProfile;
    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0 - 正常
     */
    private Integer userStatus;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    /**
     * 标签 json 列表
     */
    private String tags;
    /**
     * ip信息
     */
    private Object ipInfo;

    /**
     * 用户状态 0.正常 1.拉黑
     */
    private Integer status;

    /**
     * 在线状态 1在线 2离线
     */
    private Integer activeStatus;

    /**
     * 用户最近上下线时间
     */
    private Date lastOptTime;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
    /**
     * 对象转包装类
     *
     * @param user
     * @return
     */
    public static UserVO objToVo(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}