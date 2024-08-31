package com.lingxi.lingxibackend.model.dto.tag;

import com.lingxi.lingxibackend.common.PageRequest;
import lombok.Data;

@Data
public class TagQueryRequest extends PageRequest {
    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 0 - 不是, 1 - 父标签
     */
    private Integer isParent;
    /*
    * 用户id
    * */
    private Long userId;
}
