package com.zs.oauth2.model.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author word
 */
@Data
public class RegisterReq {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 6)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 22)
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Length(min = 8, max = 22)
    private String confirmPassword;

    private String mobile;

    /**
     * 性别，男-1，女-2， 保密：0
     */
    private Integer sex;
}
