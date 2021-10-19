package com.pj.project4sp.user4notify;

import com.pj.project4sp.user4notify.entity.MessageType;
import com.pj.project4sp.user4notify.entity.NotificationBaseContent;
import com.pj.project4sp.user4notify.vo.WsMessageVo;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.List;

public interface INotificationService {

    void wsNotify(Long toUserId, MessageType messageType, NotificationBaseContent content) throws EncodeException, IOException;

    Integer getNrOfUnreadMsg();

    List<WsMessageVo> getAllMsg();

    void read(Long messageId);

    void readList(List<Long> messageIds);
}
