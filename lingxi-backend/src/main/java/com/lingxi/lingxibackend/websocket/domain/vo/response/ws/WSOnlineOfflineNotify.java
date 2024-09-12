package com.lingxi.lingxibackend.websocket.domain.vo.response.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:用户上下线变动的推送类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSOnlineOfflineNotify implements Serializable {
    private List<ChatMemberResp> changeList = new ArrayList<>();//新的上下线用户
    private Long onlineNum;//在线人数
}
