package com.zs.oauth2.model.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 35536
 */
@Data
public class SysClientDetailsReq {

    /**
     * client id
     */
    @NotBlank(message = "客户端id不能为空")
    private String clientId;

    /**
     * 资源服务器名称
     */
    private String resourceIds;

    /**
     * 授权域
     */
    private String scopes;

    /**
     * 授权类型
     */
    private String grantTypes;

    /**
     * 重定向地址，授权码时必填
     */
    private String redirectUrl;

    /**
     * 授权信息
     */
    private String authorizations;

    /**
     * 授权令牌有效时间
     */
    private Integer accessTokenValiditySeconds;

    /**
     * 刷新令牌有效时间
     */
    private Integer refreshTokenValiditySeconds;

    /**
     * 自动授权请求域
     */
    private String autoApproveScopes;
}
