package com.pj.project4sp.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
public class SpUserInfoChangeParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

    private String value;
}
