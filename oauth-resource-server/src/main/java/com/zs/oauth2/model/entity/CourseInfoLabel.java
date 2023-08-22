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
 * @since 2023-08-16
 */
@Data
@TableName("course_info_label")
public class CourseInfoLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    /**
     * 课程标签id
     */
    private Object courseLabelId;

    /**
     * 课程id
     */
    private Object courseInfoId;

}
