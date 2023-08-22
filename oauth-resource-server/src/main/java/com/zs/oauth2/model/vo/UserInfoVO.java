package com.zs.oauth2.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserInfoVO {
    private UUID id;
    private String userName;
    private Integer sex;
    private String idCard;
    private String avatar;
    private Date createTime;
    private String majorId;
    private String detailAddress;
    private String address;
    private Integer deleted;
    private List<String> roles;
    private Date birthday;
    private String addressProvince;
    private String addressCity;
    private String addressDistrict;
    private String schoolId;

}
