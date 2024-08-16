package com.lingxi.lingxibackend.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingxi.lingxibackend.model.dto.tag.TagQueryRequest;
import com.lingxi.lingxibackend.model.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingxi.lingxibackend.model.vo.TagVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 蓝朽
* @description 针对表【tag(标签)】的数据库操作Service
* @createDate 2024-08-13 11:23:11
*/
public interface TagService extends IService<Tag> {
    /**
     * 获取标签封装
     *
     * @param tag
     * @param request
     * @return
     */
    TagVO getTagVO(Tag tag, HttpServletRequest request);

    Wrapper<Tag> getQueryWrapper(TagQueryRequest tagQueryRequest);

    Page<TagVO> getTagVOPage(Page<Tag> tagPage, HttpServletRequest request);
}
