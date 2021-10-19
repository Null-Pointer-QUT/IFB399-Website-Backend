package com.pj.project4sp.user4notify;

import cn.dev33.satoken.stp.StpUtil;
import com.pj.current.satoken.AuthConst;
import com.pj.utils.sg.AjaxJson;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notify")
public class NotificationController {

//    @Resource
//    private NotificationSocket notificationSocket;

    @Resource
    private  INotificationService notificationService;

//    @GetMapping("/sendMsg")
//    public AjaxJson sendMsg(@RequestParam Long userId, @RequestParam String msg) throws IOException {
//        NotificationSocket.sendMsg(userId, msg);
//        return AjaxJson.getSuccess("Successfully");
//    }

    @GetMapping("/getNrOfUnreadMsg")
    public AjaxJson getNrOfUnreadMsg() {
        StpUtil.checkPermission(AuthConst.r11);
        return AjaxJson.getSuccessData(notificationService.getNrOfUnreadMsg());
    }

    @GetMapping("/getAllMsg")
    public AjaxJson getAllMsg() {
        StpUtil.checkPermission(AuthConst.r11);
        return AjaxJson.getSuccessData(notificationService.getAllMsg());
    }

    @GetMapping("/read")
    public AjaxJson read(@RequestParam Long messageId) {
        StpUtil.checkPermission(AuthConst.r11);
        notificationService.read(messageId);
        return AjaxJson.getSuccess("Read message successfully");
    }

    @PostMapping("/readList")
    public AjaxJson readList(@RequestBody List<Long> messageIds) {
        StpUtil.checkPermission(AuthConst.r11);
        notificationService.readList(messageIds);
        return AjaxJson.getSuccess("Read message successfully");
    }

}
