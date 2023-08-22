package com.zs.oauth2.granter;

import com.zs.oauth2.model.entity.CustomUser;
import com.zs.oauth2.service.UserService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * 用户名和密码授权模式
 */
public class UserNamePasswordTokenGranter extends AbstractCustomTokenGranter {

    protected UserService userService;

    public UserNamePasswordTokenGranter(UserService userService, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, "password");
        this.userService = userService;
    }

    @Override
    protected CustomUser getCustomUser(Map<String, String> parameters) {
        String username = parameters.get("username");
        String password = parameters.get("password");

        // 自定义的获取用户信息
        return userService.loadUserByUsernameAndPassword(username, password);
    }
}
