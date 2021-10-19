package com.pj.project4sp.user;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stephen
 * @since 2021-08-15
 */
public interface ISpUserService extends IService<SpUser> {

    /**
     * 管理员添加用户
     * @param user
     * @return
     */
    Long add(SpUser user);


    void changeAvatar(SpUser user);

    void changeUserInfo(SpUserInfoChangeParam changeParam);

    SpUser getUserInfo();
}
