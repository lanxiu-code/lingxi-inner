package com.lingxi.lingxibackend.websocket.mapper;

import com.lingxi.lingxibackend.websocket.domain.entity.Contact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.Date;
import java.util.List;

/**
* @author 蓝朽
* @description 针对表【contact(会话列表)】的数据库操作Mapper
* @createDate 2024-09-03 10:31:41
* @Entity com.lingxi.lingxibackend.websocket.domain.entity.Contact
*/
public interface ContactMapper extends BaseMapper<Contact> {

    void refreshOrCreateActiveTime(@Param("roomId") Long roomId, @Param("memberUidList") List<Long> memberUidList,@Param("msgId") Long msgId,@Param("activeTime") Date activeTime);
}




