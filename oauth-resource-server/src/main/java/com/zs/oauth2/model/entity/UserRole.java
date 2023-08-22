package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zengshen
 * @since 2023-08-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Object userId;

    private Object roleId;


    private Date createTime;

}
