package com.zs.oauth2.granter;

import com.zs.oauth2.model.entity.CustomUser;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Map;


/**
 * 自定义token授予抽象实现
 * @author 35536
 */
public abstract class AbstractCustomTokenGranter extends AbstractTokenGranter {

    private final OAuth2RequestFactory requestFactory;

    protected AbstractCustomTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.requestFactory = requestFactory;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = tokenRequest.getRequestParameters();
        CustomUser customUser = getCustomUser(parameters);
        if (customUser == null) {
            throw new InvalidGrantException("无法获取用户信息");
        }
        OAuth2Request storedOauth2Request = this.requestFactory.createOAuth2Request(client, tokenRequest);
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(customUser, null, customUser.getAuthorities());
        authentication.setDetails(customUser);
        return new OAuth2Authentication(storedOauth2Request, authentication);
    }

    /**
     * 自定义获取用户信息
     */
    protected abstract CustomUser getCustomUser(Map<String, String> parameters);

}
