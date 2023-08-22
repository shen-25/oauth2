package com.zs.oauth2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.oauth2.model.entity.CourseCategory;
import com.zs.oauth2.model.request.AddCourseCateReq;
import com.zs.oauth2.model.vo.CourseCategoryOptVO;
import com.zs.oauth2.model.request.UpdateCourseCateReq;
import com.zs.oauth2.utils.PageInfoResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zengshen
 * @since 2023-08-14
 */
public interface CourseCategoryService extends IService<CourseCategory> {

    /**
     * 添加课程分类
     */
    void addCourseCategory(AddCourseCateReq addCourseCateReq);

    /**
     * 分页查询课程分类列表
     */
    PageInfoResult getCourseCategoryList(Integer current, Integer pageSize,
                                         String categoryName, Integer status);

    /**
     * 获取课程分类下拉列表
     */
    public List<CourseCategoryOptVO> getCourseCategroyOptList();

    /**
     * 根据名称获取课程分类
     */
    CourseCategory getByCategoryName(String categoryName);

    /**
     * 更新课程分类id
     */
    CourseCategory updateCategory(UpdateCourseCateReq updateCourseCateReq);

    /**
     * 根据课程分类id删除课程分类
     */
    void deleteById(String id);

    /**
     * 根据课程分类id获取课程分类，有空值返回
     */
    CourseCategory getById(String id);
}
