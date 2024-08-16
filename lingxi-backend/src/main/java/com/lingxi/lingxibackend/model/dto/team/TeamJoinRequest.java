package com.lingxi.lingxibackend.model.dto.team;

import lombok.Data;

import java.util.Date;

/**
 * 创建请求
 */
@Data
public class TeamJoinRequest {

    /*
    * teamId
    * */
    private Long teamId;


    /**
     * 密码
     */
    private String password;

}
