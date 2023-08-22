package com.zs.oauth2.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 获取模块类型列表的试图
 * @author 35536
 */
@Data
public class ModelTypeVO {

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
     * 模块类型描述
     */
    private String note;


    /**
     * 模块类型数量
     */
    private Integer amount;

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
