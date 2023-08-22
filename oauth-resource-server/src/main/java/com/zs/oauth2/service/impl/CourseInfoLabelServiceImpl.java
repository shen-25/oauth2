package com.zs.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zs.oauth2.mapper.CourseInfoLabelMapper;
import com.zs.oauth2.model.entity.CourseInfoLabel;
import com.zs.oauth2.service.CourseInfoLabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zengshen
 * @since 2023-08-16
 */
@Service
public class CourseInfoLabelServiceImpl extends ServiceImpl<CourseInfoLabelMapper, CourseInfoLabel> implements CourseInfoLabelService {

    @Autowired
    private CourseInfoLabelMapper courseInfoLabelMapper;

    @Override
    public void addCourseInfoLabel(String courseInfoId, String courseLabelId) {
        CourseInfoLabel courseInfoLabel = new CourseInfoLabel();
        courseInfoLabel.setId(UUID.randomUUID());
        courseInfoLabel.setCourseInfoId(UUID.fromString(courseInfoId));
        courseInfoLabel.setCourseLabelId(UUID.fromString(courseLabelId));
        courseInfoLabelMapper.insert(courseInfoLabel);
    }

    @Override
    public void deleteByCourseInfoId(String courseInfoId) {
        QueryWrapper<CourseInfoLabel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_info_id", UUID.fromString(courseInfoId));
        courseInfoLabelMapper.delete(queryWrapper);
    }


}
