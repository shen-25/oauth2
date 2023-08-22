package com.zs.oauth2.service;

import com.zs.oauth2.model.entity.SysClientDetails;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;


/**
 * 声明自己的实现.

 */
public interface SysClientDetailsService extends ClientDetailsService {

    /**
     * 通过客户端 id 查询
     */
    SysClientDetails getByClientId(String clientId);

    /**
     * 添加客户端信息.
     *
     * @param clientDetails 客户端信息
     * @throws ClientAlreadyExistsException 客户端已存在
     */
    void addClientDetails(SysClientDetails clientDetails) throws ClientAlreadyExistsException;

    /**
     * 更新客户端信息，不包括 clientSecret.
     *
     * @param clientDetails 客户端信息
     * @throws NoSuchClientException 找不到客户端异常
     */
    SysClientDetails updateClientDetails(SysClientDetails clientDetails) throws NoSuchClientException;

    /**
     * 更新客户端密钥.
     *
     * @param clientId     客户端 id
     * @param clientSecret 客户端密钥
     * @throws NoSuchClientException 找不到客户端异常
     */
    void updateByClientSecret(String clientId, String clientSecret) throws NoSuchClientException;

    /**
     * 删除客户端信息.
     *
     * @param clientId 客户端 id
     * @throws NoSuchClientException 找不到客户端异常
     */
    void deleteClientDetails(String clientId) throws NoSuchClientException;

}
