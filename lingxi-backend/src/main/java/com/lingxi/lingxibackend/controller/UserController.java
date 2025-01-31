package com.lingxi.lingxibackend.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingxi.lingxibackend.annotation.AuthCheck;
import com.lingxi.lingxibackend.common.BaseResponse;
import com.lingxi.lingxibackend.common.DeleteRequest;
import com.lingxi.lingxibackend.common.ErrorCode;
import com.lingxi.lingxibackend.common.ResultUtils;
import com.lingxi.lingxibackend.config.WxOpenConfig;
import com.lingxi.lingxibackend.constant.UserConstant;
import com.lingxi.lingxibackend.exception.BusinessException;
import com.lingxi.lingxibackend.exception.ThrowUtils;
import com.lingxi.lingxibackend.mapper.UserMapper;
import com.lingxi.lingxibackend.model.dto.user.UserAddRequest;
import com.lingxi.lingxibackend.model.dto.user.UserLoginRequest;
import com.lingxi.lingxibackend.model.dto.user.UserQueryRequest;
import com.lingxi.lingxibackend.model.dto.user.UserRegisterRequest;
import com.lingxi.lingxibackend.model.dto.user.UserUpdateMyRequest;
import com.lingxi.lingxibackend.model.dto.user.UserUpdateRequest;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.model.vo.LoginUserVO;
import com.lingxi.lingxibackend.model.vo.UserVO;
import com.lingxi.lingxibackend.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.lingxi.lingxibackend.utils.RedisUtil;
import com.lingxi.lingxibackend.websocket.domain.entity.UserFriend;
import com.lingxi.lingxibackend.websocket.service.UserFriendService;
import com.lingxi.lingxibackend.websocket.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserFriendService userFriendService;
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisUtil redisUtil;
    private static final String SALT = "lingxi";

    @Resource
    private JwtUtils jwtUtils;
    /*
     * 微信登录
     * */
    @GetMapping("/login/wx")
    public BaseResponse<String> userLoginByWx(@RequestParam("code") String code, HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        map.put("appid","wx27dd115ce4467d45");
        map.put("secret","907ea7c7dd91bae9aa0f7fc20efc53c4");
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String string = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session", map);
        log.info(string);
        return null;
    }
    /*
    * 获取token
    * */
    @GetMapping("/token")
    public BaseResponse<String> getToken(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String token = jwtUtils.createToken(loginUser.getId());
        return ResultUtils.success(token);
    }
    // region 登录相关
    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        UserVO userVO = userService.getUserVO(user);
        List<Long> friendUidList = userFriendService.getFriendList(loginUser.getId())
                .stream().map(UserFriend::getFriendUid).collect(Collectors.toList());
        userVO.setIsFriend(friendUidList.contains(user.getId()));
        Integer countFans = userMapper.countFans(userVO.getId());
        Integer countFollow = userMapper.countFollow(userVO.getId());
        userVO.setFansCount(countFans);
        userVO.setFollowCount(countFollow);
        return ResultUtils.success(userVO);
    }
    /**
     * 根据 id数组 获取包装类
     *
     * @param ids
     * @param request
     * @return
     */
    @PostMapping("/vo/batch")
    public BaseResponse<List<UserVO>> getUserVOBatch(@RequestBody List<Long> ids, HttpServletRequest request) {
        userService.getLoginUser(request);
        List<UserVO> userVOList = userService.listByIds(ids)
                .stream().map(UserVO::objToVo).collect(Collectors.toList());
        return ResultUtils.success(userVOList);
    }
    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }
    /*
    * 根据id获取粉丝列表
    * */
    @GetMapping("/list/fans")
    public BaseResponse<List<UserVO>> getUserFans(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<Long> firendUidList = userFriendService.getFriendList(loginUser.getId())
                .stream().map(UserFriend::getFriendUid)
                .collect(Collectors.toList());
        List<UserVO> userVOS = userService.listByIds(firendUidList)
                .stream().map(UserVO::objToVo)
                .collect(Collectors.toList());
        return ResultUtils.success(userVOS);
    }
    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
//        String key = String.format("lingxi:user:recommend:%s",loginUser.getId());
//        // 读取缓存
//        Page<UserVO> userPageCache = (Page<UserVO>) redisUtil.get(key);
//        if(ObjectUtil.isNotNull(userPageCache)){
//            return ResultUtils.success(userPageCache);
//        }
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 没有缓存查询数据库
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<Long> friendUidList = userFriendService.getFriendList(loginUser.getId())
                .stream()
                .map(UserFriend::getFriendUid)
                .collect(Collectors.toList());
        List<UserVO> userVOS = userService.getUserVO(userPage.getRecords())
                .stream()
                .peek(userVO -> {
                    userVO.setIsFriend(friendUidList.contains(userVO.getId()));
                    Integer countFans = userMapper.countFans(userVO.getId());
                    Integer countFollow = userMapper.countFollow(userVO.getId());
                    userVO.setFansCount(countFans);
                    userVO.setFollowCount(countFollow);
                })
                .collect(Collectors.toList());
        userVOPage.setRecords(userVOS);
        //写缓存,10s过期
//        try{
//            redisUtil.set(key,userVOPage,60);
//        }catch (Exception e){
//            log.error("redis set key error",e);
//        }
        return ResultUtils.success(userVOPage);
    }

    // endregion
    /**
     * 更新个人信息
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
            HttpServletRequest request) {
        if (ObjectUtil.hasEmpty(userUpdateMyRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        String userPassword = user.getUserPassword();
        if(StrUtil.isNotBlank(userPassword)){
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            user.setUserPassword(encryptPassword);
        }
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
    /*
    * 根据标签搜索用户
    * */
    @GetMapping("/search/tags")
    public BaseResponse<List<UserVO>> searchUsers(@RequestParam(required = false) List<String> tags,HttpServletRequest request){
        // 是否登录
        userService.getLoginUser(request);
        ThrowUtils.throwIf(tags.isEmpty(),ErrorCode.PARAMS_ERROR);
        List<UserVO> userVOList = userService.searchUsersByTags(tags);
        return ResultUtils.success(userVOList);
    }
    /*
    * 获取最匹配的用户
    * */
    @GetMapping("/match")
    public BaseResponse<List<UserVO>> matchUsers(@RequestParam long num,HttpServletRequest request){
      ThrowUtils.throwIf(num<1 || num>20,ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.matchUsers(num,loginUser));
    }
}
