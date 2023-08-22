package com.zs.oauth2.config;

import com.zs.oauth2.handler.ClientLoginFailureHandler;
import com.zs.oauth2.handler.ClientLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置
 * @author word
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientLogoutSuccessHandler clientLogoutSuccessHandler;

    @Autowired
    private ClientLoginFailureHandler clientLoginFailureHandler;

    /**
     * 第一种方式是不走 Spring Security 过滤器链，
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**",
                "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/verifyCode");
    }

    /**
     * 而第二种方式走 Spring Security 过滤器链，在过滤器链中，给请求放行
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .formLogin().loginPage("/oauth/login")
                .failureHandler(clientLoginFailureHandler)
                .loginProcessingUrl("/authorization/form")
                .and()
                .logout()
                .logoutUrl("/oauth/logout")
                .logoutSuccessHandler(clientLogoutSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**")
                .permitAll()
                .anyRequest().authenticated()
                .and().cors() // 需要添加此配置项
                .and().csrf().disable();

    }


    /**
     * 授权管理.
     *
     * @return 认证管理对象
     * @throws Exception 认证异常信息
     */
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



}
