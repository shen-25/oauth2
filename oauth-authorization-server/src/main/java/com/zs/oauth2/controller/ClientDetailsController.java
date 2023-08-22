package com.zs.oauth2.controller;

import com.zs.ApiRestResponse;
import com.zs.oauth2.model.entity.SysClientDetails;
import com.zs.oauth2.model.req.ClientIdAndSecretReq;
import com.zs.oauth2.model.req.ClientReq;
import com.zs.oauth2.model.req.SysClientDetailsReq;
import com.zs.oauth2.service.SysClientDetailsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 客户端操作
 * @author 35536
 */
@RestController
@RequestMapping("/client")
public class ClientDetailsController {

    @Autowired
    private SysClientDetailsService sysClientDetailsService;

    /**
     * 根据clientId 查找客户端
     */
    @GetMapping("/get")
    public ApiRestResponse<SysClientDetails> getByClientId(@RequestParam(value = "clientId") String clientId) {
        SysClientDetails byClientId = sysClientDetailsService.getByClientId(clientId);
        return ApiRestResponse.success(byClientId);
    }

    /**
     * 添加客户端
     */
    @PostMapping("/add")
    public ApiRestResponse<Object> addClient(@RequestBody ClientReq clientReq) {
        SysClientDetails sysClientDetails = new SysClientDetails();
        BeanUtils.copyProperties(clientReq,sysClientDetails);
        sysClientDetailsService.addClientDetails(sysClientDetails);
        return ApiRestResponse.success("添加客户端成功", null);

    }

    /**
     * 删除客户端
     */
    @DeleteMapping("/delete")
    public ApiRestResponse<String> deleteClient(@RequestParam(value = "clientId") String clientId) {
        sysClientDetailsService.deleteClientDetails(clientId);
        return ApiRestResponse.success("删除客户端成功", null);
    }

    /**
     * 更新客户端，不包含密码
     */
    @PutMapping("/update")
    public ApiRestResponse<SysClientDetails> updateClient(@RequestBody SysClientDetailsReq sysClientDetailsReq) {
        SysClientDetails sysClientDetails = new SysClientDetails();
        BeanUtils.copyProperties(sysClientDetailsReq, sysClientDetails);
        SysClientDetails res = sysClientDetailsService.updateClientDetails(sysClientDetails);
        return ApiRestResponse.success(null, res);

    }
    /**
     * 更新客户端密钥
     */
    @PutMapping("/update/secret")
    public ApiRestResponse<String> updateBy( @RequestBody @Valid ClientIdAndSecretReq clientIdAndSecretReq) {

        sysClientDetailsService.updateByClientSecret(clientIdAndSecretReq.getClientId(),
                                               clientIdAndSecretReq.getClientSecret());

        return ApiRestResponse.success( "更新客户端密钥成功", null);

    }


}
