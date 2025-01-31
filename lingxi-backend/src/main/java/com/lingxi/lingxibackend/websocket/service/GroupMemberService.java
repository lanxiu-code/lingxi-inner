package com.lingxi.lingxibackend.websocket.service;

import com.lingxi.lingxibackend.websocket.domain.entity.GroupMember;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 蓝朽
* @description 针对表【group_member(群成员表)】的数据库操作Service
* @createDate 2024-09-03 10:31:56
*/
public interface GroupMemberService extends IService<GroupMember> {
    List<Long> getMemberUidList(Long id);

}
