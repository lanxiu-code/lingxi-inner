package com.lingxi.lingxibackend.websocket.utils;

import cn.hutool.core.map.WeakConcurrentMap;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import lombok.SneakyThrows;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.io.Serializable;
import java.lang.reflect.Field;

public class LambdaUtils {

    @SneakyThrows
    public static <T> Class<?> getReturnType(Class<T> clazz,String fieldName) {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getType();
    }
}
