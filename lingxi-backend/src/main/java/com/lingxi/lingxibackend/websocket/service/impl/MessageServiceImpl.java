package com.lingxi.lingxibackend.websocket.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import com.lingxi.lingxibackend.websocket.domain.enums.MessageStatusEnum;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.CursorPageBaseReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;
import com.lingxi.lingxibackend.websocket.service.MessageService;
import com.lingxi.lingxibackend.websocket.mapper.MessageMapper;
import com.lingxi.lingxibackend.websocket.utils.CursorUtils;
import com.lingxi.lingxibackend.websocket.utils.LambdaUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
* @author 蓝朽
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2024-09-03 10:31:56
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{
    @Override
    public CursorPageBaseResp<Message> getCursorPage(Long roomId, CursorPageBaseReq pageBaseReq, Long lastMsgId) {
        Class<?> type = LambdaUtils.getReturnType(Message.class, "id");
        // 解析Cursor
        Object object = CursorUtils.parseCursor(pageBaseReq.getCursor(), type);
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getRoomId, roomId);
        wrapper.eq(Message::getStatus, MessageStatusEnum.NORMAL.getStatus());
//        wrapper
//                .le(ObjectUtil.isNotNull(lastMsgId),Message::getId,lastMsgId)
//                .or()
//                .lt(ObjectUtil.isNotNull(object),Message::getId, object);
        wrapper.lt(ObjectUtil.isNotNull(object),Message::getId, object);
        //游标方向
        wrapper.orderByDesc(Message::getId);
        Page<Message> page = this.page(pageBaseReq.plusPage(), wrapper);
        //取出游标
        String cursor = Optional.ofNullable(CollectionUtil.getLast(page.getRecords()))
                .map(Message::getId)
                .map(CursorUtils::toCursor)
                .orElse(null);
        //判断是否最后一页
        Boolean isLast = page.getRecords().size() != pageBaseReq.getPageSize();
        return new CursorPageBaseResp<>(cursor, isLast, page.getRecords());
    }

    @Override
    public Integer getUnReadCount(Long roomId, Date readTime) {
        return lambdaQuery()
                .eq(Message::getRoomId, roomId)
                .gt(Objects.nonNull(readTime), Message::getCreateTime, readTime)
                .count().intValue();
    }
}




