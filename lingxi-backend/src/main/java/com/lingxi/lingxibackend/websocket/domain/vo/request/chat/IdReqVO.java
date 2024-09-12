package com.lingxi.lingxibackend.websocket.domain.vo.request.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdReqVO implements Serializable {
    @NotNull
    private long id;
}
