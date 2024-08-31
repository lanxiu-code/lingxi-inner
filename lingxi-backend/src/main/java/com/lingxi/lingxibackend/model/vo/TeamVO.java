package com.lingxi.lingxibackend.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lingxi.lingxibackend.model.entity.Team;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 标签
 * @TableName team
 */
@TableName(value ="team")
@Data
public class TeamVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 队伍名称
     */
    private String name;
    /**
     * 队伍图标
     */
    private String icon;
    /**
     * 描述
     */
    private String description;
    /*
    * 当前队伍人数
    * */
    private Integer currentNum;
    /**
     * 最大人数
     */
    private Integer maxNum;
    /*
     * 是否加入
     * */
    private Boolean hasJoin;
    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id（队长 id）
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 创建人信息
     */
    private UserVO user;
    /**
     * 包装类转对象
     *
     * @param teamVO
     * @return
     */
    public static Team voToObj(TeamVO teamVO) {
        if (teamVO == null) {
            return null;
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamVO, team);
        return team;
    }

    /**
     * 对象转包装类
     *
     * @param team
     * @return
     */
    public static TeamVO objToVo(Team team) {
        if (team == null) {
            return null;
        }
        TeamVO teamVO = new TeamVO();
        BeanUtils.copyProperties(team, teamVO);
        return teamVO;
    }
}