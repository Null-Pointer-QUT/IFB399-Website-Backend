package com.pj.project4sp.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author  Runtian
 * @since 2021-08-15
 */
@Data
@EqualsAndHashCode()
public class SpUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String avatar;

    private String password;

    private String pw;

    private String phone;

    private Integer roleId;

    private Integer status;

    private Long createByAid;

    private Date createTime;

    private Date loginTime;

    private String loginIp;

    private Integer loginCount;

    private String uuid;

    private String source;

    private String about;

    private String organization;

    // 额外字段
//    private String roleName;		// 所属角色名称


    // 防止密码被传递到前台
    public String getPassword(){
        return "********";
    }
    // 获取真实密码
    @JsonIgnore()
    public String getPassword2(){
        return this.password;
    }

}
