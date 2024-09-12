package com.lingxi.lingxibackend.websocket.domain.dto;

import com.lingxi.lingxibackend.websocket.domain.enums.WSPushTypeEnum;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.WSBaseResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Description: 推送给用户的消息对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushMessageDTO implements Serializable {
    /**
     * 推送的ws消息
     */
    private WSBaseResp<?> wsBaseMsg;
    /**
     * 推送的uid
     */
    private List<Long> uidList;

    /**
     * 推送类型 1个人 2全员
     *
     * @see WSPushTypeEnum
     */
    private Integer pushType;

    public PushMessageDTO(Long uid, WSBaseResp<?> wsBaseMsg) {
        this.uidList = Collections.singletonList(uid);
        this.wsBaseMsg = wsBaseMsg;
        this.pushType = WSPushTypeEnum.USER.getType();
    }

    public PushMessageDTO(List<Long> uidList, WSBaseResp<?> wsBaseMsg) {
        this.uidList = uidList;
        this.wsBaseMsg = wsBaseMsg;
        this.pushType = WSPushTypeEnum.USER.getType();
    }

    public PushMessageDTO(WSBaseResp<?> wsBaseMsg) {
        this.wsBaseMsg = wsBaseMsg;
        this.pushType = WSPushTypeEnum.ALL.getType();
    }
}
