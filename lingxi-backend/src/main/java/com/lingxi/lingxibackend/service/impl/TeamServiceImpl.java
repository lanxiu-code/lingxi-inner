package com.lingxi.lingxibackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingxi.lingxibackend.annotation.RedisLock;
import com.lingxi.lingxibackend.common.ErrorCode;
import com.lingxi.lingxibackend.common.ResultUtils;
import com.lingxi.lingxibackend.constant.CommonConstant;
import com.lingxi.lingxibackend.exception.BusinessException;
import com.lingxi.lingxibackend.exception.ThrowUtils;
import com.lingxi.lingxibackend.mapper.TeamMapper;
import com.lingxi.lingxibackend.model.dto.team.TeamJoinRequest;
import com.lingxi.lingxibackend.model.dto.team.TeamQueryRequest;
import com.lingxi.lingxibackend.model.dto.team.TeamQuitRequest;
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
import com.lingxi.lingxibackend.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author 蓝朽
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-08-14 16:02:26
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{
    @Resource
    private UserService userService;
    @Resource
    private UserTeamService userTeamService;
    @Resource
    private RedissonClient redissonClient;
    /*
    * 锁名
    * */
    private static final String LOCK_TEAM_KEY = "lingxi:join:team:key";
    @Override
    public TeamVO getTeamVO(Team team, HttpServletRequest request) {
        TeamVO teamVO = TeamVO.objToVo(team);
        // 1. 关联查询用户信息
        Long userId = team.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        teamVO.setUser(userVO);
        return teamVO;
    }

    @Override
    public Wrapper<Team> getQueryWrapper(TeamQueryRequest teamQueryRequest) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (teamQueryRequest == null) {
            return queryWrapper;
        }
        String teamName = teamQueryRequest.getName();
        Long userId = teamQueryRequest.getUserId();
        Integer maxNum = teamQueryRequest.getMaxNum();
        Integer status = teamQueryRequest.getStatus();
        String sortField = teamQueryRequest.getSortField();
        String sortOrder = teamQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(teamName), "name", teamName)
                .or().like(StringUtils.isNotBlank(teamName), "description", teamName);
        queryWrapper.eq(ObjectUtil.isNotNull(userId), "userId", userId);
        queryWrapper.eq(ObjectUtil.isNotNull(maxNum) && maxNum > 0, "maxNum", maxNum);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if(statusEnum==null){
            statusEnum = TeamStatusEnum.PUBLIC;
        }
        queryWrapper.eq("status", statusEnum.getValue());
        // 不展示已过期的队伍
        queryWrapper.and(qw->qw.gt("expireTime", new Date()))
                        .or().isNull("expireTime");
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<TeamVO> getTeamVOPage(Page<Team> teamPage, HttpServletRequest request) {
        List<Team> teamList = teamPage.getRecords();
        ThrowUtils.throwIf(CollUtil.isEmpty(teamList), ErrorCode.NOT_FOUND_ERROR);
        Page<TeamVO> teamVOPage = new Page<>(teamPage.getCurrent(), teamPage.getSize(), teamPage.getTotal());
        // 1. 关联查询用户信息
        Set<Long> userIdSet = teamList.stream().map(Team::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<TeamVO> teamVOList = teamList.stream().map(team -> {
            TeamVO teamVO = TeamVO.objToVo(team);
            Long userId = team.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            teamVO.setUser(userService.getUserVO(user));
            return teamVO;
        }).collect(Collectors.toList());
        teamVOPage.setRecords(teamVOList);
        return teamVOPage;
    }

    @Override
    public void validTeam(Team team, boolean add) {
        ThrowUtils.throwIf(team == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String name = team.getName();
        String description = team.getDescription();
        Integer maxNum = team.getMaxNum();
        Date expireTime = team.getExpireTime();
        Long userId = team.getUserId();
        Integer status = team.getStatus();
        String password = team.getPassword();
        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(maxNum < 1 || maxNum > 20, ErrorCode.PARAMS_ERROR, "队伍人数不符合要求");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 20, ErrorCode.PARAMS_ERROR, "队伍名称要小于 20");
        ThrowUtils.throwIf(StringUtils.isNotBlank(description) && description.length() > 256, ErrorCode.PARAMS_ERROR, "队伍描述过长");
        TeamStatusEnum enumByValue = TeamStatusEnum.getEnumByValue(status);
        ThrowUtils.throwIf(ObjectUtil.isNull(enumByValue), ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        if(TeamStatusEnum.SECRET.equals(enumByValue)){
            ThrowUtils.throwIf(StringUtils.isBlank(password) || password.length() > 32, ErrorCode.PARAMS_ERROR, "密码设置不正确");
        }
        ThrowUtils.throwIf(new Date().after(expireTime), ErrorCode.PARAMS_ERROR, "超时时间 > 当前时间");
        // 最多五个队伍
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        long count = this.count(wrapper);
        ThrowUtils.throwIf(count >= 5, ErrorCode.PARAMS_ERROR, "超时时间 > 当前时间");
    }
    /*
    * 加入队伍
    * */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
    @RedisLock(lockName = "lingxi:team:joinTeam:key")
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        Long teamId = teamJoinRequest.getTeamId();
        Team team = this.getById(teamId);
        if(team == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"队伍不存在");
        }
        Date expireTime = team.getExpireTime();
        if(expireTime != null && expireTime.before(new Date())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已过期");
        }
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if(TeamStatusEnum.PRIVATE.equals(statusEnum)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"禁止加入私有队伍");
        }
        String password = team.getPassword();
        if(TeamStatusEnum.SECRET.equals(statusEnum)){
            if(!teamJoinRequest.getPassword().equals(password) || StringUtils.isBlank(teamJoinRequest.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
            }
        }
        // 该用户已加入的队伍数量
        Long userId = loginUser.getId();
        QueryWrapper<UserTeam> wrapper = new QueryWrapper<>();
        wrapper.eq("userId",userId);
        long count = userTeamService.count(wrapper);
        if(count >= 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"最多创建和加入 5 个队伍");
        }
        //不能重复加入已加入的队伍
        wrapper = new QueryWrapper<>();
        wrapper.eq("teamId",teamId);
        wrapper.eq("userId",userId);
        if(userTeamService.count(wrapper) > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户已加入该队伍");
        }
        //已加入队伍的人数
        wrapper = new QueryWrapper<>();
        wrapper.eq("teamId",teamId);
        if(userTeamService.count(wrapper) >= team.getMaxNum()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已满");
        }
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }
    /*
    * 退出队伍
    * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
        Long teamId = teamQuitRequest.getTeamId();
        Team team = this.getById(teamId);
        ThrowUtils.throwIf(ObjectUtil.isNull(team),ErrorCode.NOT_FOUND_ERROR,"队伍不存在");
        Long userId = loginUser.getId();
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamId(teamId);
        userTeam.setUserId(userId);
        QueryWrapper<UserTeam> wrapper = new QueryWrapper<>(userTeam);
        long count = userTeamService.count(wrapper);
        ThrowUtils.throwIf(count==0,ErrorCode.PARAMS_ERROR,"未加入队伍");
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId", teamId);
        long teamNum = userTeamService.count(userTeamQueryWrapper);
        //队伍只剩一人，解散
        if(teamNum == 1){
            this.removeById(teamId);
        }else{
            // 队伍还剩至少两人
            // 是队长
            if(team.getUserId() == userId){
                // 把队伍转移给最早加入的用户
                // 1. 查询已加入队伍的所有用户和加入时间
                QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("teamId",teamId);
                queryWrapper.last("order by id asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextTeamLeaderId = nextUserTeam.getUserId();
                // 更新当前队伍的队长
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextTeamLeaderId);
                boolean res = this.updateById(updateTeam);
                ThrowUtils.throwIf(!res,ErrorCode.SYSTEM_ERROR,"更新队长失败");
            }
        }
        // 移除关系
        return userTeamService.remove(wrapper);
    }

    @Override
    public Page<TeamVO> listMyJoinTeamVOByPage(TeamQueryRequest teamQueryRequest,HttpServletRequest request) {
        long current = teamQueryRequest.getCurrent();
        long size = teamQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<UserTeam> userTeamPage = userTeamService.page(new Page<>(current, size), Wrappers.lambdaQuery(UserTeam.class)
                .eq(UserTeam::getUserId, teamQueryRequest.getUserId()));

        List<Long> teamIdList = userTeamPage.getRecords().stream().map(UserTeam::getTeamId).collect(Collectors.toList());
        List<Team> teamList = this.listByIds(teamIdList);
        Page<Team> teamPage = new Page<>(current,size,userTeamPage.getTotal());
        teamPage.setRecords(teamList);
        return this.getTeamVOPage(teamPage, request);
    }

}




