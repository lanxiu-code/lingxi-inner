package com.lingxi.lingxibackend.websocket.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lingxi.lingxibackend.websocket.domain.enums.HotFlagEnum;
import lombok.Data;

import java.util.Date;

/**
 * Description: 房间详情
 */
@Data
public class RoomBaseInfo {
    //房间id
    private Long roomId;
    //会话名称
    private String name;
    //会话头像
    private String avatar;
    /**
     * 房间类型 1群聊 2单聊
     */
    private Integer type;

    /**
     * 是否全员展示 0否 1是
     *
     * @see HotFlagEnum
     */
    private Integer hotFlag;

    /**
     * 群最后消息的更新时间
     */
    @TableField("activeTime")
    private Date activeTime;

    /**
     * 最后一条消息id
     */
    @TableField("lastMsgId")
    private Long lastMsgId;
}
