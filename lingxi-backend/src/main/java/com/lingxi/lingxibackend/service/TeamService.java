package com.lingxi.lingxibackend.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingxi.lingxibackend.model.dto.team.TeamJoinRequest;
import com.lingxi.lingxibackend.model.dto.team.TeamQueryRequest;
import com.lingxi.lingxibackend.model.dto.team.TeamQuitRequest;
import com.lingxi.lingxibackend.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.model.vo.TeamVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 蓝朽
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-08-14 16:02:26
*/
public interface TeamService extends IService<Team> {

    TeamVO getTeamVO(Team team, HttpServletRequest request);

    Wrapper<Team> getQueryWrapper(TeamQueryRequest teamQueryRequest);

    Page<TeamVO> getTeamVOPage(Page<Team> teamPage, HttpServletRequest request);
    /**
     * 校验数据
     *
     * @param team
     * @param add 对创建的数据进行校验
     */
    void validTeam(Team team, boolean add);
    /*
    * 加入队伍
    * */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);
    /*
    * 退出队伍
    * */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    Page<TeamVO> listMyJoinTeamVOByPage(TeamQueryRequest teamQueryRequest,HttpServletRequest request);
}
