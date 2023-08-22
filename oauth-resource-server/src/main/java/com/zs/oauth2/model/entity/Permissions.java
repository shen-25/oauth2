package com.zs.oauth2.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zengshen
 * @since 2023-08-11
 */
@Data
public class Permissions implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    /**
     * 权限名称
     */
    private String permisssionName;

    /**
     * 创建时间
     */
    private Data createTime;


}
