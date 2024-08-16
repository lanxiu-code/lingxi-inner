package com.lingxi.lingxibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingxi.lingxibackend.model.entity.UserTeam;
import com.lingxi.lingxibackend.service.UserTeamService;
import com.lingxi.lingxibackend.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 蓝朽
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-08-14 16:02:34
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




