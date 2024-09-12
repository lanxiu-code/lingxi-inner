package com.lingxi.lingxibackend.websocket.domain.vo.response.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSLoginSuccess implements Serializable {
    private Long uid;
    private String avatarUrl;
    private String token;
    private String username;
    //用户权限 0普通用户 1超管
    private Integer userRole;
}
