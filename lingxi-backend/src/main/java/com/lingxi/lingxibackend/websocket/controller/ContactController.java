package com.lingxi.lingxibackend.websocket.controller;

import com.lingxi.lingxibackend.common.BaseResponse;
import com.lingxi.lingxibackend.common.ResultUtils;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.service.UserService;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.CursorPageBaseReq;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.IdReqVO;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.ChatRoomResp;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;
import com.lingxi.lingxibackend.websocket.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 聊天室相关接口
 */
@RestController
@RequestMapping("/c_api/chat")
@Slf4j
public class ContactController {
    @Resource
    private UserService userService;
    @Resource
    private RoomService roomService;
    // 会话列表
    @GetMapping("/public/contact/page")
    public BaseResponse<CursorPageBaseResp<ChatRoomResp>> getRoomPage(@Valid CursorPageBaseReq pageBaseReq, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(roomService.getContactPage(pageBaseReq, loginUser.getId()));
    }
    // 会话详情
    @GetMapping("/public/contact/detail")
    public BaseResponse<ChatRoomResp> getContactDetail(@Valid IdReqVO idReqVO,HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(roomService.getContactDetail(loginUser.getId(), idReqVO.getId()));
    }
}
