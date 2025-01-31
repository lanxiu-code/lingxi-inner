package com.lingxi.lingxibackend.websocket.domain.vo.response.ws;

import com.lingxi.lingxibackend.websocket.domain.enums.ChatActiveStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 群成员列表的成员信息
 * Date: 2023-03-23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberResp implements Serializable {
    private Long uid;
    /**
     * @see ChatActiveStatusEnum
     */
    private Integer activeStatus;

    /**
     * 角色ID
     */
    private Integer roleId;

    private Date lastOptTime;
}
