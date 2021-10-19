package com.pj.project4sp.user;


import cn.dev33.satoken.stp.StpUtil;
import com.pj.current.satoken.AuthConst;
import com.pj.utils.sg.AjaxJson;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author stephen
 * @since 2021-08-15
 */
@RestController
@RequestMapping("/user")
public class SpUserController {

    @Resource
    private ISpUserService spUserService;

    @PostMapping("/signUp")
    public AjaxJson signUp(@RequestBody SpUser user) {
        user.setRoleId(11);
        long id = spUserService.add(user);
        return AjaxJson.getSuccessData(id);
    }

    @GetMapping("/checkName")
    public AjaxJson checkName(@RequestParam String name) {
        SpUserUtil.checkName(0, name);
        return AjaxJson.getSuccess("Congratulations! The user name is not yet in use");
    }

    @GetMapping("/checkEmail")
    public AjaxJson checkEmail(@RequestParam String email) {
        SpUserUtil.checkEmail(0, email);
        return AjaxJson.getSuccess("Congratulations! The email address is not yet in use");
    }

    @PostMapping("/changeAvatar")
    public AjaxJson changeAvatar(@RequestBody SpUser user) {
        StpUtil.checkPermission(AuthConst.r11);
        spUserService.changeAvatar(user);
        return AjaxJson.getSuccess();
    }

    @GetMapping("/getUserInfo")
    public AjaxJson getUserInfo() {
        StpUtil.checkLogin();
        return AjaxJson.getSuccessData(spUserService.getUserInfo());
    }

    @PostMapping("/changeInfo")
    public AjaxJson changeUserInfo(@RequestBody SpUserInfoChangeParam changeParam) {
        StpUtil.checkPermission(AuthConst.r11);
        spUserService.changeUserInfo(changeParam);
        return AjaxJson.getSuccess();
    }

    @PostMapping("/add")
    public AjaxJson add(@RequestBody SpUser user){
        StpUtil.checkPermission(AuthConst.p_admin_list);	// 鉴权
        long id = spUserService.add(user);
        return AjaxJson.getSuccessData(id);
    }
}
