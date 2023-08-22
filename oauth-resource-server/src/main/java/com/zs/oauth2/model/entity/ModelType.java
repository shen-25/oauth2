package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
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
 * @since 2023-08-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("model_type")
public class ModelType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    /**
     * 模块类型名称
     */
    private String name;

    /**
     * 模块类型状态
     */
    private Integer status;

    /**
     * 模块描述
     */
    private String note;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh", timezone = "GMT+8")
    private Date updateTime;


}
