package com.zs.oauth2.model.req;

import cn.hutool.json.JSONUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author word
 */
@Data
public class ClientReq {


    /**
     * client id
     */
    @NotBlank(message = "客户端id不能为空")
    private String clientId;

    /**
     * client 密钥
     */
    private String clientSecret;

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

    public static void main(String[] args) {
        ClientReq clientReq = new ClientReq();
        clientReq.setClientId("oauth2");
        clientReq.setClientSecret("oauth2");
        clientReq.setGrantTypes("authorization_code,password");
        clientReq.setAccessTokenValiditySeconds(36000);
        clientReq.setRefreshTokenValiditySeconds(6000);
        clientReq.setScopes("READ,WRITE");
        clientReq.setRedirectUrl("http://localhost:8081");
        clientReq.setResourceIds("oauth2");
        String jsonStr = JSONUtil.toJsonStr(clientReq);
        System.out.println(jsonStr);

    }

}
