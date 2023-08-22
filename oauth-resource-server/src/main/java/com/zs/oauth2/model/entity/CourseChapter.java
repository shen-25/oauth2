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
@TableName("course_chapter")
public class CourseChapter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    private Object courseId;

    private String chapterName;

    private String chapterDescribe;

}
