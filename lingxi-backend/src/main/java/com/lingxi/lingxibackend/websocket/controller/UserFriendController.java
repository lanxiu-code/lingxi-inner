package com.lingxi.lingxibackend.websocket.controller;

import com.lingxi.lingxibackend.common.BaseResponse;
import com.lingxi.lingxibackend.common.ResultUtils;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.service.UserService;
import com.lingxi.lingxibackend.websocket.domain.vo.request.friend.FriendApplyReq;
import com.lingxi.lingxibackend.websocket.service.UserFriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 好友相关接口
 */
@RestController
@RequestMapping("/c_api/user/friend")
@Slf4j
public class UserFriendController {
    @Resource
    private UserService userService;
    @Resource
    private UserFriendService userFriendService;
    // 关注好友
    @PostMapping("/apply")
    public BaseResponse<Void> apply(@Valid @RequestBody FriendApplyReq friendApplyReq, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        userFriendService.apply(loginUser.getId(), friendApplyReq);
        return ResultUtils.success(null);
    }

}
