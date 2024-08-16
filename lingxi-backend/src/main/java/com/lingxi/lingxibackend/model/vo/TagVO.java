package com.lingxi.lingxibackend.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lingxi.lingxibackend.model.entity.Tag;
import com.lingxi.lingxibackend.model.entity.Tag;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 标签
 * @TableName tag
 */
@TableName(value ="tag")
@Data
public class TagVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 父标签 id
     */
    private Long parentId;

    /**
     * 0 - 不是, 1 - 父标签
     */
    private Integer isParent;

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
     * @param tagVO
     * @return
     */
    public static Tag voToObj(TagVO tagVO) {
        if (tagVO == null) {
            return null;
        }
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagVO, tag);
        return tag;
    }

    /**
     * 对象转包装类
     *
     * @param tag
     * @return
     */
    public static TagVO objToVo(Tag tag) {
        if (tag == null) {
            return null;
        }
        TagVO tagVO = new TagVO();
        BeanUtils.copyProperties(tag, tagVO);
        return tagVO;
    }
}