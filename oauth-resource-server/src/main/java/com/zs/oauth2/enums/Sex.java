package com.zs.oauth2.enums;

public enum Sex {

    MAN(0),
    WOMEN(1);
    private final Integer key;

     Sex(Integer key) {
        this.key = key;
    }

    public Integer getKey() {
        return key;
    }
}
