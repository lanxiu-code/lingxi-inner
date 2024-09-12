package com.lingxi.lingxibackend.websocket.domain.vo.response.ws;

import com.lingxi.lingxibackend.websocket.domain.enums.ChatActiveStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSMemberChange implements Serializable {
    public static final Integer CHANGE_TYPE_ADD = 1;
    public static final Integer CHANGE_TYPE_REMOVE = 2;
    private Long roomId;
    private Long uid;
    private Integer changeType;
    /**
     * @see ChatActiveStatusEnum
     */
    private Integer activeStatus;
    private Date lastOptTime;
}
