package com.lingxi.lingxibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingxi.lingxibackend.model.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户数据库操作
 *
 * 
 * 
 */
public interface UserMapper extends BaseMapper<User> {
    /*
    * 统计关注量
    * */
    @Select("select count(uid) as followCount from user_friend group by uid having uid = #{userId}")
    Integer countFollow(@Param("userId") Long userId);

    /**
     * 统计粉丝量
     */
    @Select("select count(friendUid) as fansCount from user_friend group by friendUid having friendUid = #{userId}")
    Integer countFans(@Param("userId") Long userId);
}




