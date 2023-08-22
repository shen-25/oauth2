package com.zs.oauth2.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 调用授权接口获取token值的请求（/oauth/token）需要认证client_id和client_secret
 * 让/oauth/token支持client_id以及client_secret作登录认证
 */
public class CustomClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {

    private final AuthorizationServerSecurityConfigurer configurer;
    private AuthenticationEntryPoint authenticationEntryPoint;


    public CustomClientCredentialsTokenEndpointFilter(AuthorizationServerSecurityConfigurer configurer, String path) {
        this.configurer = configurer;
        setFilterProcessesUrl(path);
    }

    @Override
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        // 把父类的干掉
        super.setAuthenticationEntryPoint(null);
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return configurer.and().getSharedObject(AuthenticationManager.class);
    }

    @Override
    public void afterPropertiesSet() {

        setAuthenticationFailureHandler((request, response, exception) ->
                authenticationEntryPoint.commence(request, response, exception));
        setAuthenticationSuccessHandler((request, response, authentication) -> {
            // 无操作-仅允许过滤器链继续到令牌端点
        });
    }
}