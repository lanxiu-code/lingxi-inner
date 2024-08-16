package com.lingxi.lingxibackend.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingxi.lingxibackend.common.BaseResponse;
import com.lingxi.lingxibackend.common.DeleteRequest;
import com.lingxi.lingxibackend.common.ErrorCode;
import com.lingxi.lingxibackend.common.ResultUtils;
import com.lingxi.lingxibackend.exception.BusinessException;
import com.lingxi.lingxibackend.exception.ThrowUtils;
import com.lingxi.lingxibackend.model.dto.team.*;
import com.lingxi.lingxibackend.model.entity.Team;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.model.entity.UserTeam;
import com.lingxi.lingxibackend.model.enums.TeamStatusEnum;
import com.lingxi.lingxibackend.model.enums.UserRoleEnum;
import com.lingxi.lingxibackend.model.vo.TeamVO;
import com.lingxi.lingxibackend.model.vo.UserVO;
import com.lingxi.lingxibackend.service.TeamService;
import com.lingxi.lingxibackend.service.UserService;
import com.lingxi.lingxibackend.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签接口
 */
@RestController
@RequestMapping("/team")
@Slf4j
public class TeamController {

    @Resource
    private TeamService teamService;
    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param teamAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        teamService.validTeam(team, true);
        team.setUserId(loginUser.getId());
        // 保存到队伍表
        boolean result = teamService.save(team);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR,"创建队伍失败");
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(loginUser.getId());
        userTeam.setTeamId(team.getId());
        userTeam.setJoinTime(new Date());
        userTeamService.save(userTeam);
        long newTeamId = team.getId();
        return ResultUtils.success(newTeamId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        Team oldTeam = teamService.getById(id);
        ThrowUtils.throwIf(oldTeam == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldTeam.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"无访问权限");
        }
        boolean b = teamService.removeById(id);
        // 移除所有加入队伍的关联信息
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId", oldTeam.getId());
        boolean res = userTeamService.remove(userTeamQueryWrapper);
        return ResultUtils.success(b && res);
    }


    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<TeamVO> getTeamVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(teamService.getTeamVO(team, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param teamQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<TeamVO>> listTeamVOByPage(@RequestBody TeamQueryRequest teamQueryRequest,
            HttpServletRequest request) {
        long current = teamQueryRequest.getCurrent();
        long size = teamQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Team> teamPage = teamService.page(new Page<>(current, size),
                teamService.getQueryWrapper(teamQueryRequest));
        Page<TeamVO> teamVOPage = teamService.getTeamVOPage(teamPage, request);
        // 过滤私密的队伍
        List<TeamVO> teamVOS = teamVOPage.getRecords().stream().filter(teamVO -> {
            Integer status = teamVO.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            if (statusEnum == TeamStatusEnum.PRIVATE) {
                UserVO userVO = teamVO.getUser();
                Integer userRole = userVO.getUserRole();
                UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(userRole);
                return userRoleEnum == UserRoleEnum.ADMIN || ObjectUtil.equals(userVO.getId(), teamVO.getUserId());
            }
            return true;
        }).collect(Collectors.toList());
        teamVOPage.setRecords(teamVOS);
        return ResultUtils.success(teamVOPage);
    }

    /**
     * 分页获取当前用户创建的队伍列表
     *
     * @param teamQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<TeamVO>> listMyTeamVOByPage(@RequestBody TeamQueryRequest teamQueryRequest,
            HttpServletRequest request) {
        if (teamQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        teamQueryRequest.setUserId(loginUser.getId());
        long current = teamQueryRequest.getCurrent();
        long size = teamQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Team> teamPage = teamService.page(new Page<>(current, size),
                teamService.getQueryWrapper(teamQueryRequest));
        return ResultUtils.success(teamService.getTeamVOPage(teamPage, request));
    }
    /*
    * 获取当前用户加入的队伍
    * */
    @PostMapping("/my/join/page/vo")
    public BaseResponse<Page<TeamVO>> listMyJoinTeamVOByPage(@RequestBody TeamQueryRequest teamQueryRequest,
                                                         HttpServletRequest request) {
        if (teamQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        teamQueryRequest.setUserId(loginUser.getId());
        return ResultUtils.success(teamService.listMyJoinTeamVOByPage(teamQueryRequest,request));

    }
    // endregion

    /**
     * 编辑（用户）
     * @param teamEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editTeam(@RequestBody TeamEditRequest teamEditRequest, HttpServletRequest request) {
        if (teamEditRequest == null || teamEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamEditRequest, team);
        User loginUser = userService.getLoginUser(request);
        long id = teamEditRequest.getId();
        // 判断是否存在
        Team oldTeam = teamService.getById(id);
        ThrowUtils.throwIf(oldTeam == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldTeam.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 私密队伍要验证密码
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if(TeamStatusEnum.PRIVATE.equals(statusEnum)){
            String password = team.getPassword();
            ThrowUtils.throwIf(StrUtil.isBlank(password), ErrorCode.PARAMS_ERROR,"加密房间必须要设置密码");
        }
        boolean result = teamService.updateById(team);
        return ResultUtils.success(result);
    }
    /*
    * 加入队伍
    * */
    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(teamJoinRequest == null || teamJoinRequest.getTeamId() <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        boolean res = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(res);
    }
    /*
    * 退出队伍
    * */
    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(teamQuitRequest == null || teamQuitRequest.getTeamId() <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        boolean res = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(res);
    }
}
