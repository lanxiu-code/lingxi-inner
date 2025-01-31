package com.lingxi.lingxibackend.websocket.controller;
import com.lingxi.lingxibackend.common.BaseResponse;
import com.lingxi.lingxibackend.common.ResultUtils;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.service.UserService;
import com.lingxi.lingxibackend.websocket.adapter.ChatAdapter;
import com.lingxi.lingxibackend.websocket.domain.entity.RoomFriend;
import com.lingxi.lingxibackend.websocket.domain.vo.request.room.RoomRequest;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.ChatMemberResp;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;
import com.lingxi.lingxibackend.websocket.service.RoomFriendService;
import com.lingxi.lingxibackend.websocket.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 聊天室相关接口
 * </p>
 */
@RestController
@RequestMapping("/c_api/room")
@Slf4j
public class RoomController {
    @Resource
    private UserService userService;
    @Resource
    private RoomFriendService roomFriendService;
    /*
    * 根据用户id和目标用户id获取房间Id
    * */
    @GetMapping("/get/roomId")
    public BaseResponse<Long> getRoomId(Long targetUserId, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        String roomKey = ChatAdapter
                .generateRoomKey(
                        Arrays.asList(
                                loginUser.getId(),
                                targetUserId
                        ));
        RoomFriend roomFriend = roomFriendService.getByKey(roomKey);
        return ResultUtils.success(roomFriend.getRoomId());
    }
}
