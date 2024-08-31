package com.lingxi.lingxibackend.mapper;

import com.lingxi.lingxibackend.model.entity.UserTeam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
* @author 蓝朽
* @description 针对表【user_team(用户队伍关系)】的数据库操作Mapper
* @createDate 2024-08-14 16:02:34
* @Entity com.lingxi.lingxibackend.model.entity.UserTeam
*/
public interface UserTeamMapper extends BaseMapper<UserTeam> {
    // 获取当前队伍人数
    @Select("select count(*) from user_team where teamId = ${teamId} and isDelete = 0")
    int getTeamUserCount(Long teamId);
}




