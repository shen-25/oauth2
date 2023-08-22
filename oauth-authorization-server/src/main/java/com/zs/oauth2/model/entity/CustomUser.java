package com.zs.oauth2.model.entity;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author 35536
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomUser implements AuthenticatedPrincipal, Serializable {


    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;


    /**
     * 头像
     */
    private String avatar;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getName() {
        // 返回经过身份验证的名称（账号唯一标识），可以是用户ID，这里采用手机号
        return this.id;
    }

}