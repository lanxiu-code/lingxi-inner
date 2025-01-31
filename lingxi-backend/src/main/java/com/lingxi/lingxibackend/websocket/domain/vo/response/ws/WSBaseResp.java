package com.lingxi.lingxibackend.websocket.domain.vo.response.ws;

import com.lingxi.lingxibackend.websocket.domain.enums.WSRespTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: ws的基本返回信息体
 */
@Data
public class WSBaseResp<T> implements Serializable {
    /**
     * ws推送给前端的消息
     *
     * @see WSRespTypeEnum
     */
    private Integer type;
    private T data;
}
