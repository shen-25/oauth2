package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zengshen
 * @since 2023-08-11
 */
@Data
@TableName("school_info")
public class SchoolInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    private String schoolName;

    /**
     * 省
     */
    private String schoolProvince;

    /**
     * 详细地址
     */
    private String schoolAddr;

    /**
     * 市
     */
    private String schoolCity;

    /**
     * 区县
     */
    private String schoolDistrict;

    /**
     * 联系人ID
     */
    private Integer contactId;


}
