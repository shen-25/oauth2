package com.zs.oauth2.controller;

import com.zs.ApiRestResponse;
import com.zs.oauth2.model.entity.SysRole;
import com.zs.oauth2.model.entity.SysUser;
import com.zs.oauth2.model.req.LoginReq;
import com.zs.oauth2.model.req.RegisterReq;
import com.zs.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zengshen
 * @since 2023-08-07
 */
@RestController
@RequestMapping("/oauth/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CheckTokenEndpoint checkTokenEndpoint;

    /**
     * 根据用户名和密码登录
     */
    @PostMapping("/login")
    public ApiRestResponse<SysUser> login(@RequestBody @Valid LoginReq loginReq) {

        SysUser user = userService.login(loginReq.getUsername(), loginReq.getPassword());

        return ApiRestResponse.success(user);
    }

    /**
     * 注册账号
     */
    @PostMapping("/register")
    public ApiRestResponse<Object> register(@RequestBody @Valid RegisterReq registerReq) {
        if (!registerReq.getPassword().equals(registerReq.getConfirmPassword())) {
            return ApiRestResponse.error("密码和确认密码不一致，请重新输入");
        }
        userService.register(registerReq);
        return ApiRestResponse.success(registerReq);

    }

}
