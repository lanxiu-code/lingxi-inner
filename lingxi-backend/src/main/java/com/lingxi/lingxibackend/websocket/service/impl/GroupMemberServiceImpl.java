package com.lingxi.lingxibackend.websocket.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingxi.lingxibackend.websocket.domain.entity.GroupMember;
import com.lingxi.lingxibackend.websocket.domain.entity.RoomGroup;
import com.lingxi.lingxibackend.websocket.service.GroupMemberService;
import com.lingxi.lingxibackend.websocket.mapper.GroupMemberMapper;
import com.lingxi.lingxibackend.websocket.service.RoomGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author 蓝朽
* @description 针对表【group_member(群成员表)】的数据库操作Service实现
* @createDate 2024-09-03 10:31:56
*/
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember>
    implements GroupMemberService{
    @Resource
    private RoomGroupService roomGroupService;
    @Override
    public List<Long> getMemberUidList(Long roomId) {
        RoomGroup roomGroup = roomGroupService.getOne(Wrappers.lambdaQuery(RoomGroup.class)
                .eq(RoomGroup::getRoomId, roomId));
        if (Objects.isNull(roomGroup)) {
            return null;
        }
        List<GroupMember> list = lambdaQuery()
                .eq(GroupMember::getGroupId, roomGroup.getId())
                .select(GroupMember::getUid)
                .list();
        return list.stream().map(GroupMember::getUid).collect(Collectors.toList());

    }
}




