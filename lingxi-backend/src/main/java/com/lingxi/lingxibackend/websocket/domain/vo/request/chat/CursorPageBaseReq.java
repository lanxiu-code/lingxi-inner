package com.lingxi.lingxibackend.websocket.domain.vo.request.chat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 游标翻页请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageBaseReq implements Serializable {
    // 页面大小
    @Min(0)
    @Max(100)
    private Integer pageSize = 10;
    // 游标（初始为null，后续请求附带上次翻页的游标）
    private String cursor;

    public Page plusPage() {
        return new Page(1, this.pageSize, false);
    }

    @JsonIgnore
    public Boolean isFirstPage() {
        return StringUtils.isEmpty(cursor);
    }
}
