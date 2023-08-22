package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zengshen
 * @since 2023-08-11
 */
@TableName("role_permission")
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Object permissionId;

    private Object roleId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Object permissionId) {
        this.permissionId = permissionId;
    }

    public Object getRoleId() {
        return roleId;
    }

    public void setRoleId(Object roleId) {
        this.roleId = roleId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RolePermission{" +
            "id = " + id +
            ", permissionId = " + permissionId +
            ", roleId = " + roleId +
            ", createTime = " + createTime +
        "}";
    }
}
