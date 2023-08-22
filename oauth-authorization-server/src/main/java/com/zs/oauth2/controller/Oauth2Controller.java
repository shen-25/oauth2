package com.zs.oauth2.controller;

import com.zs.ApiRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现自定义token返回格式
 * @author 35536
 */
@RestController
@RequestMapping("/oauth")
public class Oauth2Controller {

    /**
     * 令牌请求的端点
     */
    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private CheckTokenEndpoint checkTokenEndpoint;


    @GetMapping("/token")
    public ApiRestResponse<Object> getAccessToken(Principal principal, @RequestParam Map<String, String> parameters)
            throws HttpRequestMethodNotSupportedException {
        return custom(tokenEndpoint.getAccessToken(principal, parameters).getBody());
    }

    @PostMapping("/token")
    public ApiRestResponse postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return custom(tokenEndpoint.postAccessToken(principal, parameters).getBody());
    }


    @PostMapping(value = "/check_token")
    public ApiRestResponse<Object> checkToken(@RequestParam("token") String value)  {
        Map<String, ?> map = checkTokenEndpoint.checkToken(value);
        return ApiRestResponse.success( "解析令牌成功！", map);

    }

    //定制申请返回实体
    private ApiRestResponse custom(OAuth2AccessToken accessToken) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        Map<String, Object> data = new HashMap<>(token.getAdditionalInformation());
        data.put("accessToken", token.getValue());
        if (token.getRefreshToken() != null) {
            data.put("refreshToken", token.getRefreshToken().getValue());
        }
        return ApiRestResponse.success(data);
    }

}
