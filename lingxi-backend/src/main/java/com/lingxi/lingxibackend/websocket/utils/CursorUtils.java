package com.lingxi.lingxibackend.websocket.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingxi.lingxibackend.websocket.domain.entity.Contact;
import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import com.lingxi.lingxibackend.websocket.domain.enums.MessageStatusEnum;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.CursorPageBaseReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;
import org.apache.poi.ss.formula.functions.T;

import java.util.Date;
import java.util.Optional;

/**
 * Description: 游标分页工具类
 */
public class CursorUtils {

    public static String toCursor(Object o) {
        if (o instanceof Date) {
            return String.valueOf(((Date) o).getTime());
        } else {
            return o.toString();
        }
    }

    public static Object parseCursor(String cursor, Class<?> cursorType) {
        if(StrUtil.isBlank(cursor)) return null;
        if (Date.class.isAssignableFrom(cursorType)) {
            return new Date(Long.parseLong(cursor));
        } else {
            return cursor;
        }
    }
}
