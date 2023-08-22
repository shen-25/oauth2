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
 * @since 2023-08-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("course_category")
public class CourseCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    /**
     * 课程分类名称
     */
    private String categoryName;

    /**
     * 课程分类备注
     */
    private String note;

    /**
     * 创建人
     */
    private String creater;

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

    /**
     * 状态(0 关闭 1 启动)
     */
    private Integer status;



}
