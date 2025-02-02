package com.lingxi.lingxibackend.websocket.domain.vo.response.ws;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 游标翻页返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageBaseResp<T> implements Serializable {
    // 游标（下次翻页带上这参数）
    private String cursor;
    // 是否最后一页
    private Boolean isLast = Boolean.FALSE;
    // 数据列表
    private List<T> list;
    public static <T> CursorPageBaseResp<T> init(CursorPageBaseResp cursorPage, List<T> list) {
        CursorPageBaseResp<T> cursorPageBaseResp = new CursorPageBaseResp<T>();
        cursorPageBaseResp.setIsLast(cursorPage.getIsLast());
        cursorPageBaseResp.setList(list);
        cursorPageBaseResp.setCursor(cursorPage.getCursor());
        return cursorPageBaseResp;
    }

    @JsonIgnore
    public Boolean isEmpty() {
        return CollectionUtil.isEmpty(list);
    }

    public static <T> CursorPageBaseResp<T> empty() {
        CursorPageBaseResp<T> cursorPageBaseResp = new CursorPageBaseResp<T>();
        cursorPageBaseResp.setIsLast(true);
        cursorPageBaseResp.setList(new ArrayList<T>());
        return cursorPageBaseResp;
    }

}
