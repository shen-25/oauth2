package com.zs.oauth2.controller;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 自定义确认授权页面.
 * 需要注意的是： 不能在代码中 setComplete，因为整个授权流程并没有结束
 * 我们只是在中途修改了它确认的一些信息而已。
 *
 * @author 35536
 */
@Controller
@SessionAttributes("authorizationRequest")
public class AuthorizationController {

    /**
     * 自定义确认授权页面
     * @return ModelAndView
     */
    @GetMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model) {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("authorization");
        view.addObject("clientId", authorizationRequest.getClientId());
        // 传递 scope 过去,Set 集合
        view.addObject("scopes", authorizationRequest.getScope());
        return view;
    }

}
