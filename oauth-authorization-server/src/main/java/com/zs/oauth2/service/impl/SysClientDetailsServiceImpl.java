package com.zs.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.oauth2.mapper.SysClientDetailsMapper;
import com.zs.oauth2.model.entity.SysClientDetails;
import com.zs.oauth2.service.SysClientDetailsService;
import com.zs.oauth2.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

/**
 * 客户端相关操作
 * @author word
 */
@Service
public class SysClientDetailsServiceImpl extends ServiceImpl<SysClientDetailsMapper, SysClientDetails> implements SysClientDetailsService {

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SysClientDetailsMapper sysClientDetailsMapper;


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        SysClientDetails sysClientDetails = this.getByClientId(clientId);
        if (sysClientDetails == null) {
            throw  new ClientRegistrationException("Loading client exception.");
        }
        return sysClientDetails;
    }

    @Override
    public SysClientDetails getByClientId(String clientId) {
        QueryWrapper<SysClientDetails> query = new QueryWrapper<>();
        query.eq("client_id", clientId);

        return sysClientDetailsMapper.selectOne(query);
    }

    @Override
    public void addClientDetails(SysClientDetails clientDetails) throws ClientAlreadyExistsException {
        SysClientDetails pendingClientDetails = this.getByClientId(clientDetails.getClientId());
        if (pendingClientDetails != null) {
            throw new ClientAlreadyExistsException(String.format("Client id %s already exist.", clientDetails.getClientId()));
        }
        clientDetails.setId(idGenerator.getId());
        clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));

        sysClientDetailsMapper.insert(clientDetails);
    }

    @Override
    public SysClientDetails updateClientDetails(SysClientDetails clientDetails) throws NoSuchClientException {
        SysClientDetails pendingClientDetails = this.getByClientId(clientDetails.getClientId());
        if (pendingClientDetails == null) {
            throw  new NoSuchClientException("No such client!");
        }
        clientDetails.setClientSecret(pendingClientDetails.getClientSecret());
        sysClientDetailsMapper.updateById(clientDetails);
        return clientDetails;
    }

    @Override
    public void updateByClientSecret(String clientId, String clientSecret) throws NoSuchClientException {
        SysClientDetails exist = this.getByClientId(clientId);

        if (exist == null) {
            throw new NoSuchClientException("No such client!");
        }
        exist.setClientSecret(passwordEncoder.encode(clientSecret));
        sysClientDetailsMapper.insert(exist);
    }

    @Override
    public void deleteClientDetails(String clientId) throws NoSuchClientException {
        QueryWrapper<SysClientDetails> query = new QueryWrapper<>();
        query.eq("client_id", clientId);
        sysClientDetailsMapper.delete(query);
    }

}
