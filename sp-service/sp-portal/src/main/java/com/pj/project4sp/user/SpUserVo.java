package com.pj.project4sp.user;

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
public class SpUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String email;

    private String avatar;

    private Integer status;

    private Date loginTime;

}
