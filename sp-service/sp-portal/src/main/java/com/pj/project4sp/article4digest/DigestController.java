package com.pj.project4sp.article4digest;

import cn.dev33.satoken.stp.StpUtil;
import com.pj.current.satoken.AuthConst;
import com.pj.utils.sg.AjaxJson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RestController
@RequestMapping("/digest")
@RequiredArgsConstructor
public class DigestController {

    private final IDigestService digestService;

    @GetMapping("/manage/collectDigest")
    public AjaxJson collectDigest(@RequestParam String date) {
        StpUtil.checkPermission(AuthConst.r1);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        digestService.collectDigest(localDate);
        return AjaxJson.getSuccess("Digest collection successfully!");
    }

    @GetMapping("/getDigest")
    public AjaxJson getDigest() {
        if (!StpUtil.isLogin()) return AjaxJson.getSuccessData(new ArrayList<>());
        long userId = StpUtil.getLoginIdAsLong();
        return AjaxJson.getSuccessData(digestService.getDigest(userId));
    }

    @GetMapping("/manage/sendEmail")
    public AjaxJson sendEmail(@RequestParam String lastWeekId) {
        StpUtil.checkPermission(AuthConst.r1);
        digestService.sendEmail(lastWeekId);
        return AjaxJson.getSuccess("Send email successfully!");
    }
}
