package com.lingxi.lingxibackend.mapper;

import com.lingxi.lingxibackend.model.entity.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingxi.lingxibackend.model.vo.TeamVO;

import java.util.List;

/**
* @author 蓝朽
* @description 针对表【team(队伍)】的数据库操作Mapper
* @createDate 2024-08-14 16:02:26
* @Entity com.lingxi.lingxibackend.model.entity.Team
*/
public interface TeamMapper extends BaseMapper<Team> {
    // 获取当前用户加入的队伍
    List<TeamVO> getMyJoinTeamVOList(long userId);
}




