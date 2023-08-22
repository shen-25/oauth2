package com.zs.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.oauth2.mapper.SysRoleMapper;
import com.zs.oauth2.mapper.SysUserMapper;
import com.zs.oauth2.mapper.SysUserRoleMapper;
import com.zs.oauth2.model.entity.CustomUser;
import com.zs.oauth2.model.entity.SysRole;
import com.zs.oauth2.model.entity.SysUser;
import com.zs.oauth2.model.entity.SysUserRole;
import com.zs.oauth2.model.req.RegisterReq;
import com.zs.oauth2.service.UserService;
import com.zs.oauth2.util.IdGenerator;
import com.zs.oauth2.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 用户相关操作.
 */
@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IdGenerator idGenerator;

    /**
     * 暂时不用
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("username", username);

        SysUser user = sysUserMapper.selectOne(query);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        QueryWrapper<SysUserRole> query2 = new QueryWrapper<>();
        query2.eq("user_id", user.getId());
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(query2);
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for (SysUserRole userRole : sysUserRoleList) {
            QueryWrapper<SysRole> rolesQueryWrapper = new QueryWrapper<>();
            rolesQueryWrapper.eq("id", userRole.getRoleId());
            SysRole role = sysRoleMapper.selectOne(rolesQueryWrapper);
            SimpleGrantedAuthority simpleGrantedAuthority =
                    new SimpleGrantedAuthority(role.getName());
            list.add(simpleGrantedAuthority);
        }
        // 在这里手动构建 UserDetails 的默认实现
        return new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), list);
    }

    @Override
    public SysUser getById(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("找不到用户");
        }
        return user;
    }

    @Override
    public void saveUser(SysUser sysUser) {
        // TODO 判断用户名是否存在，手机号之类的
        sysUser.setId(idGenerator.getId());
        String salt = UUID.randomUUID().toString();
        String md5Password = MD5Utils.getMd5Password(sysUser.getPassword(), salt);
        sysUser.setSalt(salt);
        sysUser.setPassword(md5Password);
        sysUserMapper.insert(sysUser);
    }

    @Override
    public void updatePassword(Long id, String password) {
        SysUser exist = getById(id);
        String md5Password = MD5Utils.getMd5Password(password, exist.getSalt());
        exist.setPassword(md5Password);
        sysUserMapper.updateById(exist);

    }


    @Override
    public SysUser getByUsername(String username) {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("username", username);
        return sysUserMapper.selectOne(query);
    }


    @Override
    public SysUser login(String username, String password) {
        SysUser user = this.getByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!MD5Utils.comparePasswordsForEquality(user.getSalt(), user.getPassword(), password)) {
            throw new RuntimeException("用户名或者密码不正确");
        }
        return user;
    }

    @Override
    public void register(RegisterReq registerBO) {

        SysUser exit = this.getByUsername(registerBO.getUsername());
        if (exit != null) {
            throw new RuntimeException("用户名已存在");
        }
        SysUser user = new SysUser();
        // TODO 判断是否重复之类的
        user.setId(idGenerator.getId());
        user.setUsername(registerBO.getUsername());
        // 加盐值
        String salt = UUID.randomUUID().toString();
        // 获取md5后的密码
        String md5Password = MD5Utils.getMd5Password(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setMobile(registerBO.getMobile());
        user.setSex(registerBO.getSex());
        sysUserMapper.insert(user);
    }



    @Override
    public SysUser getByMobile(String mobile) {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("mobile", mobile);
        return sysUserMapper.selectOne(query);
    }

    @Override
    public List<SysRole> listRoles(Long userId) {
        QueryWrapper<SysUserRole> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(query);
        List<SysRole> roleList = new LinkedList<>();
        for (SysUserRole userRole : userRoleList) {
            roleList.add(sysRoleMapper.selectById(userRole.getRoleId()));
        }
        return roleList;
    }

    @Override
    public CustomUser loadUserByMobileAndPassword(String mobile, String password) {
        SysUser user = this.getByMobile(mobile);
        if (user == null) {
            throw  new UsernameNotFoundException("用户不存在");
        }
        if (!MD5Utils.comparePasswordsForEquality(user.getSalt(), user.getPassword(), password)) {
            throw new UsernameNotFoundException("手机号或者密码不正确");
        }
        // 构建用户授权的角色名
        List<SimpleGrantedAuthority> authorityList = this.getAuthorityList(user.getId());

        // 在这里手动构建 UserDetails 的默认实现
        return CustomUser.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .mobile(user.getMobile())
                .avatar(user.getAvatar())
                .authorities(authorityList).build();

    }

    @Override
    public CustomUser loadUserByMobileAndSmsCode(String mobile, String smsCode) {
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(smsCode)) {
            throw new InvalidGrantException("您输入的手机号或短信验证码不正确");
        }
        //TODO 验证码从别的地方拿过来，这里先写死
        if (!StringUtils.equals(smsCode, "smsCode")) {
            throw new InvalidGrantException("短信验证码不正确");
        }
        SysUser user = this.getByMobile(mobile);
        if (user == null) {
            throw new UsernameNotFoundException("手机号不存在");
        }
        List<SimpleGrantedAuthority> authorityList = this.getAuthorityList(user.getId());
        // 在这里手动构建 UserDetails 的默认实现
        return CustomUser.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .mobile(user.getMobile())
                .avatar(user.getAvatar())
                .authorities(authorityList).build();
    }

    @Override
    public CustomUser loadUserByUsernameAndPassword(String username, String password)
            throws UsernameNotFoundException {
        SysUser user = this.getByUsername(username);
        if (user == null) {
            throw  new UsernameNotFoundException("User not found!");
        }
        if (!MD5Utils.comparePasswordsForEquality(user.getSalt(), user.getPassword(), password)) {
            throw new UsernameNotFoundException("用户名或者密码不正确");
        }
        List<SimpleGrantedAuthority> authorityList = this.getAuthorityList(user.getId());
        // 在这里手动构建 UserDetails 的默认实现
        return CustomUser.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .mobile(user.getMobile())
                .avatar(user.getAvatar())
                .authorities(authorityList).build();

    }

    private  List<SimpleGrantedAuthority>  getAuthorityList(Long userId) {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        List<SysRole> rolesList = this.listRoles(userId);
        for (SysRole role : rolesList) {
            SimpleGrantedAuthority simpleGrantedAuthority =
                    new SimpleGrantedAuthority(role.getName());
            list.add(simpleGrantedAuthority);
        }
        return list;
    }

}
