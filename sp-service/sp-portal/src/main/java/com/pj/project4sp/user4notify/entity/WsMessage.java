package com.pj.project4sp.user4notify.entity;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.pj.project4sp.user4notify.entity.MessageType;
import com.pj.project4sp.user4notify.entity.NotificationBaseContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("wsMessage")
public class WsMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long messageId;

    private Long toUserId;

    private LocalDateTime createTime;

    private Boolean isRead;

    private MessageType messageType;

    private NotificationBaseContent content;

    public WsMessage(Long toUserId, MessageType messageType, NotificationBaseContent content) {
        Snowflake snowflake = IdUtil.getSnowflake(6);
        messageId = snowflake.nextId();
        this.toUserId = toUserId;
        createTime = LocalDateTime.now(ZoneOffset.ofHours(8));
        isRead = false;
        this.messageType = messageType;
        this.content = content;
    }
}
