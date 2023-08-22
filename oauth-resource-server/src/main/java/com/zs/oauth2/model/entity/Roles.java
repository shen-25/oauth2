package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author zengshen
 * @since 2023-08-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("roles")
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 角色描述
     */
    private String note;

}
