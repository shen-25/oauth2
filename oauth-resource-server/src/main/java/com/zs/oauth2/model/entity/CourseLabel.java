package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 课程标签实体类
 * </p>
 *
 * @author zengshen
 * @since 2023-08-15
 */
@Data
@TableName("course_label")
public class CourseLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    /**
     * 课程标签名称
     */
    private String labelName;

    /**
     * 标签备注
     */
    private String note;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh", timezone = "GMT+8")
    private Date createTime;

}
