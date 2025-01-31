package com.lingxi.lingxibackend.websocket.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 群成员表
 * @TableName group_member
 */
@TableName(value ="group_member")
@Data
public class GroupMember implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 群组id
     */
    private Long groupId;

    /**
     * 成员uid
     */
    private Long uid;

    /**
     * 成员角色 1群主 2管理员 3普通成员
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}