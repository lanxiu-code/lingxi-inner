package com.lingxi.lingxibackend.websocket.domain.vo.request.friend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * Description: 申请好友信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendApplyReq implements Serializable {
    // 申请信息
    @NotBlank
    private String msg;

    // 好友uid
    @NotNull
    private Long targetUid;

}
