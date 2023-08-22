package com.zs.oauth2.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class CheckTokenVO {

    private String userName;
    private String userId;
    private List<String > authorities;
}
