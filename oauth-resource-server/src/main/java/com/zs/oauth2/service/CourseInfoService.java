package com.zs.oauth2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.oauth2.model.entity.CourseInfo;
import com.zs.oauth2.model.request.AddCourseReq;
import com.zs.oauth2.model.vo.CourseInfoOptVO;
import com.zs.oauth2.model.request.UpdateCourseReq;
import com.zs.oauth2.model.vo.CourseRelateCategoryVO;
import com.zs.oauth2.utils.PageInfoResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zengshen
 * @since 2023-08-11
 */
@Service
public interface CourseInfoService extends IService<CourseInfo> {

    /**
     * 添加课程
     * @param courseInfo
     */
    void addCourseInfo(AddCourseReq courseInfo);

    /**
     *  分页根据课程信息列表
     */
    PageInfoResult getCourseInfoList(Integer current, Integer pageSize, String  courseCategory,
                                     Integer target, String courseName, Integer status);

    /**
     *  根据课程下拉列表
     */
    List<CourseInfoOptVO> getCourseInfoOptList();

    /**
     * 该课程的设置为最新版本，同面向对象下的同课程名字但是不同版本的课程设置为旧版本
     */
     void updateLatestVersion(String courseId);

    CourseInfo getById(String courseInfoId);

    /**
     *  根据课程id删除课程
     */
    void deleteById(String courseId);

    CourseInfo updateCourseInfo(UpdateCourseReq updateCourseReq);

    /**
     * 返回所有课程的数据(id, 课程名称，课程类别)
     * @return
     */
    List<CourseRelateCategoryVO> getCourseRelateCategoryVOList();


    PageInfoResult getLatestVerCourseList(Integer current, Integer pageSize, String courseType, Integer target, Integer status);
}
