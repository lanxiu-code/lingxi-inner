package com.lingxi.lingxibackend.websocket.service;

import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.CursorPageBaseReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;

import java.util.Date;

/**
* @author 蓝朽
* @description 针对表【message(消息表)】的数据库操作Service
* @createDate 2024-09-03 10:31:56
*/
public interface MessageService extends IService<Message> {
    CursorPageBaseResp<Message> getCursorPage(Long roomId, CursorPageBaseReq pageBaseReq, Long lastMsgId);

    Integer getUnReadCount(Long roomId, Date readTime);
}
