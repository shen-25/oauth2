package com.zs.oauth2.config;

import com.zs.oauth2.enums.TokenStoreType;
import com.zs.oauth2.model.entity.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class Oauth2AuthorizationTokenConfig {

    // 该对象用来将令牌信息存储到Redis中
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    // 默认redis存储
    public static final TokenStoreType TOKEN_STORE_TYPE = TokenStoreType.REDIS;

    //从文件中获取对称密钥的值。
    @Value("${jwt.key}")
    private String jwtKey;

    /**
     * jwt 令牌 配置
     * @return 转换器
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(jwtKey);
        ((DefaultAccessTokenConverter) accessTokenConverter.getAccessTokenConverter()).setUserTokenConverter(new DefaultUserAuthenticationConverter() {
            @Override
            public Authentication extractAuthentication(Map<String, ?> map) {
                CustomUser customUser = new CustomUser();
                BeanMap.create(customUser).putAll(map);
                Object authorities = map.get("authorities");
                if (authorities instanceof String) {
                    customUser.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities));
                } else if (authorities instanceof Collection) {
                    customUser.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString((Collection) authorities)));
                }
                return new PreAuthenticatedAuthenticationToken(customUser, null, customUser.getAuthorities());
            }
        });
        return accessTokenConverter;
    }

    /**
     * 声明 内存 TokenStore 实现，用来存储 token 相关.
     * 默认实现有 mysql、redis
     *
     * @return InMemoryTokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        switch (TOKEN_STORE_TYPE) {
            case REDIS:
                return new RedisTokenStore(redisConnectionFactory);
        }
        return new InMemoryTokenStore();
    }


    /**
     * 加密方式，使用 BCrypt.
     * 参数越大加密次数越多，时间越久.
     * 默认为 10.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
