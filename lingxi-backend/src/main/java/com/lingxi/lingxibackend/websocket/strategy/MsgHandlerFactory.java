package com.lingxi.lingxibackend.websocket.strategy;


import cn.hutool.core.lang.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 */
public class MsgHandlerFactory {
    private static final Map<Integer, AbstractMsgHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer code, AbstractMsgHandler strategy) {
        STRATEGY_MAP.put(code, strategy);
    }

    public static AbstractMsgHandler getStrategyNoNull(Integer code) {
        return STRATEGY_MAP.get(code);
    }
}
