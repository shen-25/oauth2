package com.zs.oauth2.granter;

import com.zs.oauth2.model.entity.CustomUser;
import com.zs.oauth2.service.UserService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import java.util.Map;

/**
 * 手机号 + 验证码 授权模式
 * @author 35536
 */
public class MobileSmsCustomTokenGranter extends AbstractCustomTokenGranter {

    protected UserService userService;

    public MobileSmsCustomTokenGranter(UserService userDetailsService, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, "smsCode");
        this.userService = userDetailsService;
    }

    @Override
    protected CustomUser getCustomUser(Map<String, String> parameters) {
        String phoneNumber = parameters.get("mobile");
        String smsCode = parameters.get("code");
        return userService.loadUserByMobileAndSmsCode(phoneNumber, smsCode);
    }

}
