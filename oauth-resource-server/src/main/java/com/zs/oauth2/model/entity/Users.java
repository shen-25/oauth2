package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zengshen
 * @since 2023-08-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("users")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;


    private String userName;

    private String phoneNumber;

    private String password;

    private String salt;

    /**
     * "0：男;1：女"
     */
    private Boolean sex;


}
