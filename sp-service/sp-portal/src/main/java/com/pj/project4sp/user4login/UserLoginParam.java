package com.pj.project4sp.user4login;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

    private String password;
}
