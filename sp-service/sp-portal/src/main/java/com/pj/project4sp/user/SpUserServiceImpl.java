package com.pj.project4sp.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pj.project4sp.SP;
import com.pj.project4sp.user4password.SpUserPasswordService;
import com.pj.utils.sg.AjaxError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Runtian
 * @since 2021-08-15
 */
@Service
public class SpUserServiceImpl extends ServiceImpl<SpUserMapper, SpUser> implements ISpUserService {

    @Resource
    private SpUserMapper spUserMapper;
    @Resource
    private SpUserPasswordService spUserPasswordService;

    private Map<String, BiConsumer<Long, String>> infoChangeMap = MapUtil.builder(new HashMap<String, BiConsumer<Long, String>>())
            .put("name", (userId, value) -> {
                SpUserUtil.checkName(0, value);
                spUserMapper.update(null, Wrappers.<SpUser>update().lambda()
                        .eq(SpUser::getId, userId)
                        .set(SpUser::getName, value));
            }).put("password", (userId, value) -> {
                if (value.length() < 4) {
                    throw new AjaxError("The password must be at least four characters long");
                }
                spUserPasswordService.updatePassword(userId, value);
            }).put("about", (userId, value) -> {
                if (StrUtil.isBlank(value)) {
                    throw new AjaxError("About cannot be empty");
                }
                spUserMapper.update(null, Wrappers.<SpUser>update().lambda()
                        .eq(SpUser::getId, userId)
                        .set(SpUser::getAbout, value));
            }).put("avatar", (userId, value) -> {
                if (StrUtil.isBlank(value)) {
                    throw new AjaxError("Avatar cannot be empty");
                }
                spUserMapper.update(null, Wrappers.<SpUser>update().lambda()
                        .eq(SpUser::getId, userId)
                        .set(SpUser::getAvatar, value));
            }).put("organization", (userId, value) -> {
                if (StrUtil.isBlank(value)) {
                    throw new AjaxError("Organization cannot be empty");
                }
                spUserMapper.update(null, Wrappers.<SpUser>update().lambda()
                        .eq(SpUser::getId, userId)
                        .set(SpUser::getOrganization, value));
            }).build();


    // 添加一个用户
    @Override
    @Transactional(rollbackFor = Exception.class, propagation= Propagation.REQUIRED)	// REQUIRED=如果调用方有事务  就继续使用调用方的事务
    public Long add(SpUser user) {
        // 检查姓名是否合法
        SpUserUtil.checkUser(user);

        // 开始添加
        if (StpUtil.isLogin()) {
            user.setCreateByAid(StpUtil.getLoginIdAsLong());    // 创建人，为当前账号
        }
        spUserMapper.insert(user);	// 添加
        long id = SP.publicMapper.getPrimarykey();	// 获取主键
        Optional.ofNullable(user.getPassword2())
                .ifPresent(password2 -> spUserPasswordService.updatePassword(id, password2));

        // 返回主键
        return id;
        // return AjaxJson.getSuccessData(id);
    }

    @Override
    public void changeAvatar(SpUser user) {
        long userId = StpUtil.getLoginIdAsLong();
        Optional.ofNullable(user).orElseThrow(() -> AjaxError.get("The avatar cannot be empty"));
        if (StrUtil.isBlank(user.getAvatar())) throw AjaxError.get("The avatar cannot be empty");
        spUserMapper.update(null, Wrappers.<SpUser>update().lambda()
                .eq(SpUser::getId, userId)
                .set(SpUser::getAvatar, user.getAvatar()));
    }

    @Override
    public void changeUserInfo(SpUserInfoChangeParam changeParam) {
        long userId = StpUtil.getLoginIdAsLong();
        infoChangeMap.getOrDefault(changeParam.getKey(), (aLong, s) -> {
            throw AjaxError.get("Key should be name, password, about, avatar or organization");
        }).accept(userId, changeParam.getValue());
    }

    @Override
    public SpUser getUserInfo() {
        long userId = StpUtil.getLoginIdAsLong();
        return spUserMapper.selectById(userId);
    }
}
