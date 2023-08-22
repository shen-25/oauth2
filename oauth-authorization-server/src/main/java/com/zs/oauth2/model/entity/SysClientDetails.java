package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户端信息.
 * 这里实现了 ClientDetails 接口
 * 个人建议不应该在实体类里面写任何逻辑代码
 * 而为了避免实体类耦合严重不应该去实现这个接口的
 * 但是这里为了演示和 {@link SysUser} 不同的方式，所以就选择实现这个接口了
 * 另一种方式是写一个方法将它转化为默认实现 {@link BaseClientDetails} 比较好一点并且简单很多
 * @author 35536
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_client_details")
public class SysClientDetails implements ClientDetails {


    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    /**
     * 令牌有效期(秒)
     */
    private Integer accessTokenValiditySeconds;

    /**
     * 客户端所拥有的权限值
     */
    private String authorizations;

    /**
     * 是否跳过授权（true 是，false 否）
     */
    private String autoApproveScopes;

    /**
     * 客户端唯一表示id
     */
    private String clientId;

    /**
     * 客户端访问密匙
     */
    private String clientSecret;

    /**
     * 认证模式
     */
    private String grantTypes;

    /**
     * 客户端重定向url
     */
    private String redirectUrl;

    /**
     * 令牌刷新周期(秒）
     */
    private Integer refreshTokenValiditySeconds;

    /**
     * 客户端所能访问的资源id集合,id作为该服务资源的唯一标识,逗号隔开
     * 实体类字段为JSON类型时，必须使用标签进行判定以排除其为null的可能性，否则会报下面的找不到为null时的指定。
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String resourceIds;

    /**
     * 对应的范围
     */
    private String scopes;

    /**
     * 是否安全
     *
     * @return 结果
     */
    @Override
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    /**
     * 是否有 scopes
     *
     * @return 结果
     */
    @Override
    public boolean isScoped() {
        return this.scopes != null && !this.scopes.isEmpty();
    }

    /**
     * scopes
     *
     * @return scopes
     */
    @Override
    public Set<String> getScope() {
        return stringToSet(scopes);
    }

    /**
     * 授权类型
     *
     * @return 结果
     */
    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return stringToSet(grantTypes);
    }

    @Override
    public Set<String> getResourceIds() {
        return stringToSet(resourceIds);
    }


    /**
     * 获取回调地址
     *
     * @return redirectUrl
     */
    @Override
    public Set<String> getRegisteredRedirectUri() {
        return stringToSet(redirectUrl);
    }

    /**
     * 个人觉得这里应该是客户端所有的权限
     * 但是已经有 scope 的存在可以很好的对客户端的权限进行认证了
     * 那么在 oauth2 的四个角色中，这里就有可能是资源服务器的权限
     * 但是一般资源服务器都有自己的权限管理机制，比如拿到用户信息后做 RBAC
     * 所以在 spring security 的默认实现中直接给的是空的一个集合
     * 这里我们也给他一个空的把
     *
     * @return GrantedAuthority
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * 判断是否自动授权
     *
     * @param scope scope
     * @return 结果
     */
    @Override
    public boolean isAutoApprove(String scope) {
        if (autoApproveScopes == null || autoApproveScopes.isEmpty()) {
            return false;
        }
        Set<String> authorizationSet = stringToSet(authorizations);
        for (String auto : authorizationSet) {
            if ("true".equalsIgnoreCase(auto) || scope.matches(auto)) {
                return true;
            }
        }
        return false;
    }

    /**
     * additional information 是 spring security 的保留字段
     * 暂时用不到，直接给个空的即可
     *
     * @return map
     */
    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Collections.emptyMap();
    }



    private Set<String> stringToSet(String s) {
        return Arrays.stream(s.split(",")).collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        SysClientDetails sysClientDetails = new SysClientDetails();
        System.out.println(sysClientDetails.stringToSet("authorization_code,password,refresh_token"));

    }
}
