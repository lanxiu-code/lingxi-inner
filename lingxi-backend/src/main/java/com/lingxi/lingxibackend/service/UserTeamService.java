package com.lingxi.lingxibackend.service;

import com.lingxi.lingxibackend.model.entity.UserTeam;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;

/**
* @author 蓝朽
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service
* @createDate 2024-08-14 16:02:34
*/
public interface UserTeamService extends IService<UserTeam> {
    // 获取当前队伍人数
    int getTeamUserCount(Long teamId);
}
