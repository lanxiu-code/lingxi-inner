package com.lingxi.lingxibackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingxi.lingxibackend.constant.CommonConstant;
import com.lingxi.lingxibackend.model.dto.tag.TagQueryRequest;
import com.lingxi.lingxibackend.model.entity.*;
import com.lingxi.lingxibackend.model.entity.Tag;
import com.lingxi.lingxibackend.model.vo.TagVO;
import com.lingxi.lingxibackend.model.vo.UserVO;
import com.lingxi.lingxibackend.service.TagService;
import com.lingxi.lingxibackend.mapper.TagMapper;
import com.lingxi.lingxibackend.service.UserService;
import com.lingxi.lingxibackend.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 蓝朽
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-08-13 11:23:11
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{
    @Resource
    private UserService userService;
    @Override
    public TagVO getTagVO(Tag tag, HttpServletRequest request) {
        TagVO tagVO = TagVO.objToVo(tag);
        // 1. 关联查询用户信息
        Long userId = tag.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        tagVO.setUser(userVO);
        return tagVO;
    }

    @Override
    public Wrapper<Tag> getQueryWrapper(TagQueryRequest tagQueryRequest) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        if (tagQueryRequest == null) {
            return queryWrapper;
        }
        String tagName = tagQueryRequest.getTagName();
        Long userId = tagQueryRequest.getUserId();
        Integer isParent = tagQueryRequest.getIsParent();
        String sortField = tagQueryRequest.getSortField();
        String sortOrder = tagQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(tagName), "tagName", tagName);
        queryWrapper.eq(ObjectUtil.isNotNull(userId), "userId", userId);
        queryWrapper.eq(ObjectUtil.isNotNull(isParent), "isParent", isParent);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<TagVO> getTagVOPage(Page<Tag> tagPage, HttpServletRequest request) {
        List<Tag> tagList = tagPage.getRecords();
        Page<TagVO> tagVOPage = new Page<>(tagPage.getCurrent(), tagPage.getSize(), tagPage.getTotal());
        if (CollUtil.isEmpty(tagList)) {
            return tagVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = tagList.stream().map(Tag::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<TagVO> tagVOList = tagList.stream().map(tag -> {
            TagVO tagVO = TagVO.objToVo(tag);
            Long userId = tag.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            tagVO.setUser(userService.getUserVO(user));
            return tagVO;
        }).collect(Collectors.toList());
        tagVOPage.setRecords(tagVOList);
        return tagVOPage;
    }
}




