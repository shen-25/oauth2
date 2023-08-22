package com.zs.oauth2.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 35536
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUser implements  Serializable {


    private String id;

    //昵称
    private String username;

    //手机号
    private String mobile;


}