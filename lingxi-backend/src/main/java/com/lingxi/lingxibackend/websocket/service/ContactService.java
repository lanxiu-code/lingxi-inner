package com.lingxi.lingxibackend.websocket.service;

import com.lingxi.lingxibackend.websocket.domain.entity.Contact;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.CursorPageBaseReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;

import java.util.Date;
import java.util.List;

/**
* @author 蓝朽
* @description 针对表【contact(会话列表)】的数据库操作Service
* @createDate 2024-09-03 10:31:41
*/
public interface ContactService extends IService<Contact> {
    void refreshOrCreateActiveTime(Long id, List<Long> memberUidList, Long messageId, Date createTime);

    CursorPageBaseResp<Contact> getContactPage(Long uid, CursorPageBaseReq pageBaseReq);
}
