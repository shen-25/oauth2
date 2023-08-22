package com.zs.oauth2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.oauth2.model.entity.CourseLabel;
import com.zs.oauth2.model.request.AddCourseLabelReq;
import com.zs.oauth2.model.vo.CourseLabelOptVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zengshen
 * @since 2023-08-11
 */
public interface CourseLabelService extends IService<CourseLabel> {
    /**
     * 添加课程标签
     */
    void addCourseLabel(AddCourseLabelReq courseLabelReq);

    /**
     * 获取课程标签下拉列表
     */
    public List<CourseLabelOptVO> getCourseLabelOptList();

    /**
     * 根据名称获取课程标签
     */
    CourseLabel getByLabelName(String labelName);

    /**
     * 根据课程标签id删除课程标签
     */
    void deleteById(String id);

    /**
     * 根据课程标签id获取课程标签，有空值返回
     */
    CourseLabel getById(String id);
}
