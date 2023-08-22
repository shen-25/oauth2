package com.zs.oauth2.enums;

import lombok.Getter;

@Getter
public enum YesOrNo {

    // 是还是不是
    NO(0, "否"),

    YES(1, "是");

    public int type;
    public String desc;

    YesOrNo(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
