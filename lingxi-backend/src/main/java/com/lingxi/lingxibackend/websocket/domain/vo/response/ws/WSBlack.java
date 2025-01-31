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
public class WSBlack implements Serializable {
    private Long uid;
}
