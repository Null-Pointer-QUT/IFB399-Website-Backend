package com.pj.project4sp.user4oauth;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pj.project4sp.user.ISpUserService;
import com.pj.project4sp.user.SpUser;
import com.pj.project4sp.user.SpUserMapper;
import com.pj.project4sp.user4login.SpAccUserService;
import com.pj.utils.sg.AjaxJson;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGoogleRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/oauth")
public class RestAuthController {

    @Resource
    private ISpUserService userService;
    @Resource
    private SpUserMapper userMapper;
    @Resource
    private SpAccUserService accUserService;

    @RequestMapping("/render/{source}")
    public void renderAuth(@PathVariable String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @RequestMapping("/callback/{source}")
    public AjaxJson login(@PathVariable String source, AuthCallback callback) {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
        }
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        AuthUser data = response.getData();
        log.info(JSONObject.toJSONString(response));
        return add(source, data);
    }

    public AjaxJson add(String source, AuthUser authUser) {
        SpUser spUser = userMapper.selectOne(Wrappers.<SpUser>query().lambda()
                .eq(SpUser::getSource, source)
                .eq(SpUser::getUuid, authUser.getUuid())
                .or().eq(SpUser::getEmail, authUser.getEmail()));
        if (BeanUtil.isEmpty(spUser)) {
            spUser = new SpUser();
            spUser.setId(0L);
            spUser.setRoleId(11);
            spUser.setAvatar(authUser.getAvatar());
            spUser.setEmail(authUser.getEmail());
            spUser.setName(authUser.getUsername());
            spUser.setSource(source);
            spUser.setUuid(authUser.getUuid());
            userService.add(spUser);
        }
        if (BeanUtil.isEmpty(spUser.getUuid())) {
            userService.update(Wrappers.<SpUser>update().lambda()
                    .eq(SpUser::getId, spUser.getId())
                    .set(SpUser::getUuid, authUser.getUuid())
                    .set(SpUser::getSource, authUser.getSource()));
        }
//        // 4、是否禁用
//        if(user.getStatus() == 2) {
//            return AjaxJson.getError("此账号已被禁用，如有疑问，请联系管理员");
//        }

        return accUserService.afterValidate(spUser);
    }

    private AuthRequest getAuthRequest(String source) {
        Map<String, AuthRequest> authRequestMap = MapUtil.builder(new HashMap<String, AuthRequest>())
                .put("google", new AuthGoogleRequest(AuthConfig.builder()
                        .clientId("658598029163-3glmane0s8ft8s8d0g0ulpl1c9lpgvfj.apps.googleusercontent.com")
                        .clientSecret("N74FZa7fs_Jk7-zz2WMQH0uq")
                        .redirectUri("http://np.halocampus.com:48080/sp-portal/oauth/callback/google")
                        .build()))
                .put("gitee", new AuthGiteeRequest(AuthConfig.builder()
                        .clientId("08868204683e3443423462ffc594e5b2c1a43baeb22d62d94d2351ec9ac058d2")
                        .clientSecret("fe77a277a0d0d75e6f685929c49e3166284c9828a741a1cc77c65c732f19780a")
                        .redirectUri("http://localhost:3000/oauth/gitee")
                        .build())).build();
        return authRequestMap.get(source);
    }
}