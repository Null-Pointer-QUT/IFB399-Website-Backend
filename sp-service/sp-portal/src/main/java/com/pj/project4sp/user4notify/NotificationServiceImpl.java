package com.pj.project4sp.user4notify;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.mongodb.client.result.UpdateResult;
import com.pj.project4sp.article.ArticleRepository;
import com.pj.project4sp.article4comment.CommentRepository;
import com.pj.project4sp.user.SpUser;
import com.pj.project4sp.user.SpUserMapper;
import com.pj.project4sp.user4notify.entity.MessageType;
import com.pj.project4sp.user4notify.entity.NotificationBaseContent;
import com.pj.project4sp.user4notify.entity.WsMessage;
import com.pj.project4sp.user4notify.vo.NotifyBaseVo;
import com.pj.project4sp.user4notify.vo.NotifyCommentVo;
import com.pj.project4sp.user4notify.vo.NotifyThumbUpVo;
import com.pj.project4sp.user4notify.vo.WsMessageVo;
import com.pj.utils.sg.AjaxError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final WsMessageRepository wsMessageRepo;

    private final ArticleRepository articleRepo;

    private final CommentRepository commentRepo;

    private final SpUserMapper userMapper;

    private final MongoTemplate mongoTemplate;

    @Override
    @Async
    public void wsNotify(Long toUserId, MessageType messageType, NotificationBaseContent content) throws EncodeException, IOException {
        WsMessage wsMessage = new WsMessage(toUserId, messageType, content);
        wsMessageRepo.insert(wsMessage);
        if (NotificationSocket.checkOnline(toUserId)) {
            WsMessageVo msgVo = fillMsg(wsMessage);
            NotificationSocket.sendObjMsg(toUserId, msgVo);
        }
    }

    public WsMessageVo fillMsg(WsMessage wsMessage) {
        NotificationBaseContent content = wsMessage.getContent();
        WsMessageVo msgVo = WsMessageVo.fromEntity(wsMessage);
        NotifyBaseVo notifyBaseVo = null;
        if (MessageType.THUMB_UP.equals(msgVo.getMessageType())) {
            NotifyThumbUpVo thumbUpVo = NotifyThumbUpVo.fromEntity(content);
            articleRepo.findById(Long.valueOf(thumbUpVo.getArticleId())).map(a -> {
                thumbUpVo.setTitle(a.getTitle());
                return a;
            });
            SpUser spUser = userMapper.selectById(content.getUserId());
            thumbUpVo.setUsername(spUser.getName());
            notifyBaseVo = thumbUpVo;
        } else if (MessageType.COMMENT.equals(msgVo.getMessageType())) {
            NotifyCommentVo commentVo = NotifyCommentVo.fromEntity(content);
            articleRepo.findById(Long.valueOf(commentVo.getArticleId())).map(a -> {
                commentVo.setTitle(a.getTitle());
                return a;
            });
            SpUser spUser = userMapper.selectById(commentVo.getUserId());
            commentVo.setUsername(spUser.getName());
            if (!"0".equals(commentVo.getParentId())) {
                commentRepo.findById(Long.valueOf(commentVo.getParentId())).map(c -> {
                    SpUser atUser = userMapper.selectById(c.getUserId());
                    commentVo.setAtUserId(atUser.getId().toString());
                    commentVo.setAtUsername(atUser.getName());
                    return c;
                });
            }
            commentRepo.findById(Long.valueOf(commentVo.getCommentId())).map(c -> {
                commentVo.setComment(c.getComment());
                return c;
            });
            notifyBaseVo = commentVo;
        }
        msgVo.setContent(notifyBaseVo);
        return msgVo;
    }

    @Override
    public Integer getNrOfUnreadMsg() {
        long userId = StpUtil.getLoginIdAsLong();
        return (int) mongoTemplate.count(Query.query(Criteria.where("toUserId").is(userId).and("isRead").is(Boolean.FALSE)), WsMessage.class);
    }

    @Override
    public List<WsMessageVo> getAllMsg() {
        long userId = StpUtil.getLoginIdAsLong();
        List<WsMessage> messages = mongoTemplate.find(Query.query(Criteria.where("toUserId").is(userId)), WsMessage.class);
        return messages.stream()
                .sorted(Comparator.comparing(WsMessage::getMessageId).reversed())
                .map(this::fillMsg)
                .collect(Collectors.toList());
    }

    @Override
    public void read(Long messageId) {
        long userId = StpUtil.getLoginIdAsLong();
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(messageId).and("toUserId").is(userId).and("isRead").is(Boolean.FALSE)),
                Update.update("isRead", Boolean.TRUE), WsMessage.class);
        if (updateResult.getModifiedCount()==0L) throw AjaxError.get("Message not found or has been read!");
    }

    @Override
    public void readList(List<Long> messageIds) {
        long userId = StpUtil.getLoginIdAsLong();
        UpdateResult updateResult = mongoTemplate.updateMulti(Query.query(Criteria.where("_id").in(messageIds).and("toUserId").is(userId).and("isRead").is(Boolean.FALSE)),
                Update.update("isRead", Boolean.TRUE), WsMessage.class);
        if (updateResult.getModifiedCount()==0L) throw AjaxError.get("Message not found or has been read!");
    }
}
