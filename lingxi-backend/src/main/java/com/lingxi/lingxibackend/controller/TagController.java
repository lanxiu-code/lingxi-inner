package com.lingxi.lingxibackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingxi.lingxibackend.common.BaseResponse;
import com.lingxi.lingxibackend.common.DeleteRequest;
import com.lingxi.lingxibackend.common.ErrorCode;
import com.lingxi.lingxibackend.common.ResultUtils;
import com.lingxi.lingxibackend.exception.BusinessException;
import com.lingxi.lingxibackend.exception.ThrowUtils;
import com.lingxi.lingxibackend.model.dto.tag.TagAddRequest;
import com.lingxi.lingxibackend.model.dto.tag.TagEditRequest;
import com.lingxi.lingxibackend.model.dto.tag.TagQueryRequest;
import com.lingxi.lingxibackend.model.entity.Tag;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.model.vo.TagVO;
import com.lingxi.lingxibackend.service.TagService;
import com.lingxi.lingxibackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 标签接口
 */
@RestController
@RequestMapping("/tag")
@Slf4j
public class TagController {

    @Resource
    private TagService tagService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param tagAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTag(@RequestBody TagAddRequest tagAddRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (tagAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagAddRequest, tag);
        tag.setUserId(loginUser.getId());
        boolean result = tagService.save(tag);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newTagId = tag.getId();
        return ResultUtils.success(newTagId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTag(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        Tag oldTag = tagService.getById(id);
        ThrowUtils.throwIf(oldTag == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldTag.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = tagService.removeById(id);
        return ResultUtils.success(b);
    }


    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<TagVO> getTagVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tag tag = tagService.getById(id);
        if (tag == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(tagService.getTagVO(tag, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param tagQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<TagVO>> listTagVOByPage(@RequestBody TagQueryRequest tagQueryRequest,
            HttpServletRequest request) {
        long current = tagQueryRequest.getCurrent();
        long size = tagQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Tag> tagPage = tagService.page(new Page<>(current, size),
                tagService.getQueryWrapper(tagQueryRequest));
        return ResultUtils.success(tagService.getTagVOPage(tagPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param tagQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<TagVO>> listMyTagVOByPage(@RequestBody TagQueryRequest tagQueryRequest,
            HttpServletRequest request) {
        if (tagQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        tagQueryRequest.setUserId(loginUser.getId());
        long current = tagQueryRequest.getCurrent();
        long size = tagQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Tag> tagPage = tagService.page(new Page<>(current, size),
                tagService.getQueryWrapper(tagQueryRequest));
        return ResultUtils.success(tagService.getTagVOPage(tagPage, request));
    }

    // endregion

    /**
     * 编辑（用户）
     * @param tagEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editTag(@RequestBody TagEditRequest tagEditRequest, HttpServletRequest request) {
        if (tagEditRequest == null || tagEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagEditRequest, tag);
        User loginUser = userService.getLoginUser(request);
        long id = tagEditRequest.getId();
        // 判断是否存在
        Tag oldTag = tagService.getById(id);
        ThrowUtils.throwIf(oldTag == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldTag.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = tagService.updateById(tag);
        return ResultUtils.success(result);
    }

}
