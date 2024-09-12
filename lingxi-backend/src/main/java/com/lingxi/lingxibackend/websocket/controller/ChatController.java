package com.lingxi.lingxibackend.websocket.controller;

import com.lingxi.lingxibackend.common.BaseResponse;
import com.lingxi.lingxibackend.common.ResultUtils;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.service.UserService;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessageMemberReq;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessagePageReq;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessageReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.ChatMessageResp;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;
import com.lingxi.lingxibackend.websocket.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 聊天相关接口
 */
@RestController
@RequestMapping("/c_api/chat")
@Slf4j
public class ChatController {
    @Resource
    private ChatService chatService;
    @Resource
    private UserService userService;
    /*
    * 消息列表
    * */
    @GetMapping("/public/msg/page")
    public BaseResponse<CursorPageBaseResp<ChatMessageResp>> getMsgPage(@Valid ChatMessagePageReq messagePageReq, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        CursorPageBaseResp<ChatMessageResp> msgPage = chatService.getMsgPage(messagePageReq, loginUser.getId());
        return ResultUtils.success(msgPage);
    }

    /**
     * 发送消息
     * @param request
     * @return
     */
    @PostMapping("/msg")
    public BaseResponse<ChatMessageResp> sendMsg(@Valid @RequestBody ChatMessageReq chatMessageReq, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        Long msgId = chatService.sendMsg(chatMessageReq, userId);
        //返回完整消息格式，方便前端展示
        return ResultUtils.success(chatService.getMsgResp(msgId, userId));
    }
    //消息阅读上报
    @PutMapping("/msg/read")
    public BaseResponse<Void> msgRead(@Valid @RequestBody ChatMessageMemberReq memberReq, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        chatService.msgRead(loginUser.getId(), memberReq);
        return ResultUtils.success(null);
    }
}
