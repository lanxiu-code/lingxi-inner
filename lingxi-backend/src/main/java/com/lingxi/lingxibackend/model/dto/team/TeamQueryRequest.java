package com.lingxi.lingxibackend.model.dto.team;

import com.lingxi.lingxibackend.common.PageRequest;
import lombok.Data;

import java.util.Date;

/**
 * 创建请求
 */
@Data
public class TeamQueryRequest extends PageRequest {
    /**
     * id
     */
    private Long id;
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 用户id（队长 id）
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;


}
