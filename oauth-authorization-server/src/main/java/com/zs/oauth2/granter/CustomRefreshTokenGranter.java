package com.zs.oauth2.granter;

import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 自定义刷新令牌模式 具体逻辑
 * @author 35536
 */
public class CustomRefreshTokenGranter extends RefreshTokenGranter {

    private final TokenStore tokenStore;

    public CustomRefreshTokenGranter(TokenStore tokenStore, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory);
        this.tokenStore=tokenStore;
    }

    @Override
    protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
        String refreshTokenValue = tokenRequest.getRequestParameters().get("refresh_token");
        OAuth2RefreshToken refreshToken = this.tokenStore.readRefreshToken(refreshTokenValue);
        if (refreshToken == null) {
            throw new InvalidTokenException("无效的刷新令牌: " + refreshTokenValue);
        }
        OAuth2Authentication authentication = this.tokenStore.readAuthenticationForRefreshToken(refreshToken);
        String clientId = authentication.getOAuth2Request().getClientId();
        if (clientId != null && clientId.equals(tokenRequest.getClientId())) {
            if (this.isExpired(refreshToken)) {
                tokenStore.removeRefreshToken(refreshToken);
                throw new InvalidTokenException("刷新令牌过期: " + refreshToken);
            } else {
                tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
                tokenStore.removeRefreshToken(refreshToken);
                return getTokenServices().createAccessToken(authentication);
            }
        } else {
            throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
        }
    }

    private boolean isExpired(OAuth2RefreshToken refreshToken) {
        if (!(refreshToken instanceof ExpiringOAuth2RefreshToken)) {
            return false;
        } else {
            ExpiringOAuth2RefreshToken expiringToken = (ExpiringOAuth2RefreshToken)refreshToken;
            return expiringToken.getExpiration() == null || System.currentTimeMillis() > expiringToken.getExpiration().getTime();
        }
    }

}
