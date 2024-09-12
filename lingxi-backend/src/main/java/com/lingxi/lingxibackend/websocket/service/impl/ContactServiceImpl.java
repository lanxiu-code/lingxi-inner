package com.lingxi.lingxibackend.websocket.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingxi.lingxibackend.websocket.domain.entity.Contact;
import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import com.lingxi.lingxibackend.websocket.domain.enums.MessageStatusEnum;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.CursorPageBaseReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;
import com.lingxi.lingxibackend.websocket.service.ContactService;
import com.lingxi.lingxibackend.websocket.mapper.ContactMapper;
import com.lingxi.lingxibackend.websocket.utils.CursorUtils;
import com.lingxi.lingxibackend.websocket.utils.LambdaUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author 蓝朽
* @description 针对表【contact(会话列表)】的数据库操作Service实现
* @createDate 2024-09-03 10:31:41
*/
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact>
    implements ContactService{
    @Resource
    private ContactMapper contactMapper;

    @Override
    public void refreshOrCreateActiveTime(Long roomId, List<Long> memberUidList, Long messageId, Date createTime) {
        baseMapper.refreshOrCreateActiveTime(roomId, memberUidList, messageId, createTime);
    }

    @Override
    public CursorPageBaseResp<Contact> getContactPage(Long uid, CursorPageBaseReq pageBaseReq) {
        Class<?> type = LambdaUtils.getReturnType(Contact.class, "activeTime");
        // 解析Cursor,Object: string或者date类型
        Object object = CursorUtils.parseCursor(pageBaseReq.getCursor(), type);
        LambdaQueryWrapper<Contact> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Contact::getUid, uid);
        //游标条件
        wrapper.lt(ObjectUtil.isNotNull(object),Contact::getActiveTime,object);
        //排序
        wrapper.orderByDesc(Contact::getActiveTime);
        Page<Contact> page = this.page(pageBaseReq.plusPage(), wrapper);
        //取出游标
        String cursor = Optional.ofNullable(CollectionUtil.getLast(page.getRecords()))
                .map(Contact::getActiveTime)
                .map(CursorUtils::toCursor)
                .orElse(null);
        //判断是否最后一页
        Boolean isLast = page.getRecords().size() != pageBaseReq.getPageSize();
        return new CursorPageBaseResp<>(cursor, isLast, page.getRecords());
    }
}




