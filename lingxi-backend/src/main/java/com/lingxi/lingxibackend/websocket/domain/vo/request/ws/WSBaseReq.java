package com.lingxi.lingxibackend.websocket.domain.vo.request.ws;

import lombok.Data;

import java.io.Serializable;

/**
 * Description: websocket前端请求体
 */
@Data
public class WSBaseReq implements Serializable {
    /**
     * 请求类型 2心跳检测
     */
    private Integer type;

    /**
     * 每个请求包具体的数据，类型不同结果不同
     */
    private String data;
}
