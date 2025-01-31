package com.lingxi.lingxibackend.model.dto.team;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 创建请求
 */
@Data
public class TeamAddRequest {

    /**
     * 队伍名称
     */
    private String name;
    /**
     * 队伍图标
     */
    private String icon;
    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;


    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;

}
