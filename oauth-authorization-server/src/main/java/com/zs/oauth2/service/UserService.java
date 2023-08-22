package com.zs.oauth2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.oauth2.model.entity.SysRole;
import com.zs.oauth2.model.entity.SysUser;
import com.zs.oauth2.model.vo.UserInfoVO;
import com.zs.oauth2.model.entity.CustomUser;
import com.zs.oauth2.model.req.RegisterReq;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


/**
 * 用户接口
 * @author word
 */
public interface UserService extends IService<SysUser> {


    /**
     * 通过 id 查询用户
     */
    SysUser getById(Long id);


    /**
     * 通过 手机号 查询用户
     */
    SysUser getByMobile(String mobile);

    /**
     * 通过 username 查询用户
     */
    SysUser getByUsername(String username);

    /**
     * 创建用户
     */
    void saveUser(SysUser user);


    /**
     * 更新用户 密码
     */
    void updatePassword(Long id, String password);

    /**
     * 登录
     */
    SysUser login(String username, String password);

    /**
     * 用户注册
     */
    void register(RegisterReq registerBO);

    /**
     * 根据用户id, 获取用户的角色
     */
    List<SysRole> listRoles(Long userId);


    /**
     * oauth自定义授权模式，手机号和密码登录
     */
    CustomUser loadUserByMobileAndPassword(String mobile, String password);

    /**
     * oauth自定义授权模式，手机号和smsCode登录
     */
    CustomUser loadUserByMobileAndSmsCode(String mobile, String smsCode);

    /**
     * oauth自定义授权模式，用户名和密码登录
     */
    CustomUser loadUserByUsernameAndPassword(String username, String password);

}
