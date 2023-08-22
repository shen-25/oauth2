package com.zs.oauth2.config;


import cn.hutool.core.lang.UUID;
import com.zs.oauth2.enums.TokenStoreType;
import com.zs.oauth2.filter.CustomClientCredentialsTokenEndpointFilter;
import com.zs.oauth2.granter.CustomRefreshTokenGranter;
import com.zs.oauth2.granter.MobilePasswordCustomTokenGranter;
import com.zs.oauth2.granter.MobileSmsCustomTokenGranter;
import com.zs.oauth2.granter.UserNamePasswordTokenGranter;
import com.zs.oauth2.model.entity.CustomUser;
import com.zs.oauth2.service.SysClientDetailsService;
import com.zs.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.util.*;


/**
 * 授权服务器相关的配置，主要设置授权服务器如何读取客户端、用户信息和一些端点配置
 * @author 35536
 */
@Configuration
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter  {




    @Autowired
    private UserService userService;

    @Autowired
    private SysClientDetailsService sysClientDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthorizationServerEndpointsConfiguration configuration;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;



    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        String path = "/oauth/token";
        // 获取自定义映射路径，比如 ((AuthorizationServerEndpointsConfigurer) endpoints).pathMapping("/oauth/token", "/my/token");
        path = configuration.oauth2EndpointHandlerMapping().getServletPath(path);
        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(security, path);
        endpointFilter.afterPropertiesSet();
        endpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
        security.authenticationEntryPoint(authenticationEntryPoint);
        security.addTokenEndpointAuthenticationFilter(endpointFilter);

        security

                // 获取 token key 需要进行 basic 认证客户端信息
                .tokenKeyAccess("permitAll()")
                // 获取 token 信息同样需要 basic 认证客户端信息
                .checkTokenAccess("permitAll()")
                // 允许客户端使用表单方式发送请求token的认证（因为表单一般是POST请求，所以使用POST方式发送获取token，但必须携带clientId，clientSecret，否则随便一请求过来验token是不验的）
                .allowFormAuthenticationForClients();

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 从数据库读取自定义的客户端信息
        clients.withClientDetails(sysClientDetailsService);
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore);
        List<TokenGranter> tokenGranters = getTokenGranters(endpoints.getAuthorizationCodeServices(), endpoints.getTokenStore(), endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
        endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters));
        endpoints.tokenEnhancer(new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
                DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) oAuth2AccessToken;
                CustomUser user = (CustomUser) oAuth2Authentication.getPrincipal();
                Map<String, Object> map = new HashMap<>();
                map.put("userId", user.getId());
                map.put("username", user.getUsername());
                token.setAdditionalInformation(map);
                if (Oauth2AuthorizationTokenConfig.TOKEN_STORE_TYPE == TokenStoreType.IN_MEMORY) {
                    token = (DefaultOAuth2AccessToken) jwtAccessTokenConverter.enhance(oAuth2AccessToken, oAuth2Authentication);
                } else {
                    token.setValue(buildTokenValue());
                    if (token.getRefreshToken() != null) {
                        if (token.getRefreshToken() instanceof DefaultExpiringOAuth2RefreshToken) {
                            DefaultExpiringOAuth2RefreshToken refreshToken = (DefaultExpiringOAuth2RefreshToken) token.getRefreshToken();
                            token.setRefreshToken(new DefaultExpiringOAuth2RefreshToken(buildTokenValue(), refreshToken.getExpiration()));
                        } else {
                            token.setRefreshToken(new DefaultOAuth2RefreshToken(buildTokenValue()));
                        }
                    }
                }
                return token;
            }
            public String buildTokenValue() {
                return UUID.fastUUID().toString(true) + UUID.fastUUID().toString(true);
            }

        });

    }

    /**
     * 注册授权模式
     */
    private List<TokenGranter> getTokenGranters(AuthorizationCodeServices authorizationCodeServices,
                                                TokenStore tokenStore, AuthorizationServerTokenServices tokenServices,
                                                ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        return new ArrayList<>(Arrays.asList(
                new CustomRefreshTokenGranter(tokenStore, tokenServices, clientDetailsService, requestFactory),
                new UserNamePasswordTokenGranter(userService, tokenServices, clientDetailsService, requestFactory),
                new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory),
                new MobilePasswordCustomTokenGranter(userService, tokenServices, clientDetailsService, requestFactory),
                new MobileSmsCustomTokenGranter(userService, tokenServices, clientDetailsService, requestFactory)
        ));
    }


}
