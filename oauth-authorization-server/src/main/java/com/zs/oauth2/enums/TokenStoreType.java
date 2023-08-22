package com.zs.oauth2.enums;

import lombok.Getter;

@Getter
public enum TokenStoreType {

    /**
     * oauth信息存储
     */
    IN_MEMORY("IN_MEMORY", "内存存储"),

    REDIS("REDIS", "redis存储");


    final String value;
    final String desc;

    TokenStoreType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
