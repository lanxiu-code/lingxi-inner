package com.lingxi.lingxibackend.websocket.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingxi.lingxibackend.websocket.domain.entity.GroupMember;
import com.lingxi.lingxibackend.websocket.domain.entity.RoomGroup;
import com.lingxi.lingxibackend.websocket.service.GroupMemberService;
import com.lingxi.lingxibackend.websocket.service.RoomGroupService;
import com.lingxi.lingxibackend.websocket.mapper.RoomGroupMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author 蓝朽
* @description 针对表【room_group(群聊房间表)】的数据库操作Service实现
* @createDate 2024-09-03 10:32:05
*/
@Service
public class RoomGroupServiceImpl extends ServiceImpl<RoomGroupMapper, RoomGroup>
    implements RoomGroupService{

}




