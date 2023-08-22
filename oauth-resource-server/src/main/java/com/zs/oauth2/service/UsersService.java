package com.zs.oauth2.service;

import com.zs.oauth2.model.entity.Users;
import com.zs.oauth2.model.vo.UserInfoVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zengshen
 * @since 2023-08-08
 */
public interface UsersService {

    UserInfoVO getUserInfo(String userId);

    Users getById(String userId);

}
