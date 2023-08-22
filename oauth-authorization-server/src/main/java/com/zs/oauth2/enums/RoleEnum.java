package com.zs.oauth2.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    /**
     * 用户角色
     */
    ADMIN(0, "admin"),
    TEACHER(1, "teacher"),
    STUDENT(2, "student");


    public int type;
    public String desc;

    RoleEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
