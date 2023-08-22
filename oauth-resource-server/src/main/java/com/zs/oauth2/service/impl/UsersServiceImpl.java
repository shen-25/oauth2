package com.zs.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zs.oauth2.enums.Sex;
import com.zs.oauth2.mapper.RolesMapper;
import com.zs.oauth2.mapper.UserRoleMapper;
import com.zs.oauth2.mapper.UsersMapper;
import com.zs.oauth2.model.entity.UserRole;
import com.zs.oauth2.model.entity.Users;
import com.zs.oauth2.model.vo.UserInfoVO;
import com.zs.oauth2.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zengshen
 * @since 2023-08-08
 */
@Service
public class UsersServiceImpl  implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RolesMapper rolesMapper;

    @Override
    public UserInfoVO getUserInfo(String userId) {
        QueryWrapper<Users> query = new QueryWrapper<>();
        query.eq("id", UUID.fromString(userId));
        Users user = usersMapper.selectOne(query);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getId());
        List<UserRole> userRoleList = userRoleMapper.selectList(userRoleQueryWrapper);
        List<String> roles = new LinkedList<>();
        for (UserRole userRole : userRoleList) {
            roles.add(String.valueOf(userRole.getRoleId()));
        }
        return UserInfoVO.builder()
                .id((UUID) user.getId())
                .userName(user.getUserName()).address("地址").addressCity("城市")
                .addressProvince("东莞市").addressDistrict("樟木头").birthday(new Date())
                .createTime(new Date())
                .sex(user.getSex() ? Sex.WOMEN.getKey() : Sex.MAN.getKey())
                .deleted(0)
                .idCard("身份证")
                .detailAddress("新药国际")
                .majorId("1")
                .roles(roles)
                .schoolId("1")
                .avatar("图片为空")
                .sex(1).build();
    }

    @Override
    public Users getById(String userId) {
        QueryWrapper<Users> query = new QueryWrapper<>();
        query.eq("id", UUID.fromString(userId));
        return usersMapper.selectOne(query);
    }
}
