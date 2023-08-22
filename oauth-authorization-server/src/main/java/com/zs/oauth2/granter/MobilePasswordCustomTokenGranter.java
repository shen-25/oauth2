package com.zs.oauth2.granter;

import com.zs.oauth2.model.entity.CustomUser;
import com.zs.oauth2.service.UserService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import java.util.Map;


/**
 * 手机号 + 密码 授权模式 具体逻辑
 * @author 35536
 */
public class MobilePasswordCustomTokenGranter extends AbstractCustomTokenGranter {

    protected UserService userService;

    public MobilePasswordCustomTokenGranter(UserService userDetailsService, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, "mobile");
        this.userService = userDetailsService;
    }

    @Override
    protected CustomUser getCustomUser(Map<String, String> parameters) {
        String mobile = parameters.get("mobile");
        String password = parameters.get("password");
        return userService.loadUserByMobileAndPassword(mobile, password);
    }

}
