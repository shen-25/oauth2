package com.zs.oauth2.model.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author word
 */
@Data
public class ClientIdAndSecretReq {

    /**
     * client id
     */
    @NotBlank(message = "客户端id不能为空")
    private String clientId;

    /**
     * client 密钥
     */
    @NotBlank(message = "客户端密钥不能为空")
    private String clientSecret;
}
