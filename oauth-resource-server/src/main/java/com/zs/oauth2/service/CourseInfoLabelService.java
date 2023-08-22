package com.zs.oauth2.service;

import com.zs.oauth2.model.entity.CourseInfoLabel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  课程info和课程标签关联表
 * </p>
 *
 * @author zengshen
 * @since 2023-08-16
 */
public interface CourseInfoLabelService extends IService<CourseInfoLabel> {
    /**
     * 添加课程info和课程标签关联
     */
    void  addCourseInfoLabel(String courseInfoId, String courseLabelId);

    void deleteByCourseInfoId(String courseInfoId);

}
