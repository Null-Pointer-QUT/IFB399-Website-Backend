package com.pj.project4sp.user4notify.vo;

import cn.hutool.extra.cglib.CglibUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pj.project4sp.user4notify.entity.MessageType;
import com.pj.project4sp.user4notify.entity.WsMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsMessageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messageId;

    private String toUserId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    private Boolean isRead;

    private MessageType messageType;

    private NotifyBaseVo content;

    public static WsMessageVo fromEntity(WsMessage wsMessage) {
        WsMessageVo messageVo = CglibUtil.copy(wsMessage, WsMessageVo.class);
        messageVo.setMessageId(wsMessage.getMessageId().toString());
        messageVo.setToUserId(wsMessage.getToUserId().toString());
        return messageVo;
    }

}
