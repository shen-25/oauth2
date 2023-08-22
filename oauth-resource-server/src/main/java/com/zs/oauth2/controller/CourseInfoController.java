package com.zs.oauth2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zs.oauth2.constant.MessageConstant;
import com.zs.oauth2.model.entity.CourseInfo;
import com.zs.oauth2.model.request.AddCourseReq;
import com.zs.oauth2.model.request.UpdateCourseReq;
import com.zs.oauth2.model.vo.CourseInfoOptVO;
import com.zs.oauth2.service.CourseInfoService;
import com.zs.core.module.Result;
import com.zs.oauth2.constant.Constant;
import com.zs.oauth2.utils.PageInfoResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 课程controller
 *
 * @author 35536
 */
@RestController
@Slf4j
public class CourseInfoController {


    @Autowired
    private CourseInfoService courseInfoService;


    /**
     * 添加课程
     */
    @PostMapping("/course/addCourse")
    public Result add(@Valid AddCourseReq requestCourse) {
        CourseInfo one = courseInfoService.getOne(new QueryWrapper<CourseInfo>()
                .eq("target", requestCourse.getTarget())
                .eq("version", requestCourse.getVersion())
                .eq("course_name", requestCourse.getCourseName()));
        if (one != null) {
            return Result.failed("课程名称、版本号、面向对象相同，不可以上传!");
        }

        courseInfoService.addCourseInfo(requestCourse);
        return Result.succeed(null, "上传课程成功");
    }

    /**
     * 按照课程名字、课程类型、引用对象、状态进行课程搜索
     *
     * @return
     */
    @GetMapping("/course/list")
    public Result<?> listCourse(@RequestParam(value = "pageSize") Integer pageSize,
                                @RequestParam(value = "current") Integer current,
                                @RequestParam(value = "courseCategory", required = false) String courseCategory,
                                @RequestParam(value = "objQuote", required = false) Integer objQuote,
                                @RequestParam(value = "courseName", required = false) String courseName,
                                @RequestParam(value = "status", required = false) Integer status) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = Constant.COMMON_PAGE_SIZE;
        }
        PageInfoResult result = courseInfoService.getCourseInfoList(current, pageSize,
                courseCategory, objQuote, courseName, status);
        return Result.succeed(result, "获取课程仓库列表成功");
    }

    /**
     * 按照课程名字、课程类型、状态进行课程搜索
     *
     * @return
     */
    @GetMapping("/course/list/options")
    public Result<?> listCourseOptions() {

        List<CourseInfoOptVO> res = courseInfoService.getCourseInfoOptList();
        return Result.succeed(res, "获取课程下拉列表成功");
    }

    /**
     * 根据课程id更新课程
     */
    @PutMapping("/updateCourseInfo")
    public Result<CourseInfo> updateCourseInfo(@Valid UpdateCourseReq updateCourseReq) {
        CourseInfo courseInfo = courseInfoService.updateCourseInfo(updateCourseReq);
        return Result.succeed(courseInfo, MessageConstant.MES_COURSE_UPDATE_INFO_SUCCESS);

    }


    /**
     * 根据课程id更新课程为最新课程,同面向对象且同课程名称但是不同版本的课程更新为旧版本
     */
    @PutMapping("/update/{courseId}latestVersion")
    public Result<?> updateByIsLatestVersion(@PathVariable String courseId) {
        courseInfoService.updateLatestVersion(courseId);
        return Result.succeed(null, "课程已设置为最新版本");
    }

    /**
     * 根据课程id删除课程
     */
    @DeleteMapping("/deletecourse/{id}")
    public Result<?> deleteById(@PathVariable String id) {
        courseInfoService.deleteById(id);
        return Result.succeed(null, "删除课程成功");
    }

    @GetMapping("/courseLatest/getCourseLatestList")
    public Result<?> getLatestVerCourseList(@RequestParam(value = "pageSize") Integer pageSize,
                                @RequestParam(value = "current") Integer current,
                                @RequestParam(value = "courseType", required = false) String courseType,
                                @RequestParam(value = "target", required = false) Integer target,
                                @RequestParam(value = "status", required = false) Integer status) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = Constant.COMMON_PAGE_SIZE;
        }
        PageInfoResult result = courseInfoService.getLatestVerCourseList(current, pageSize,
                courseType, target,  status);
        return Result.succeed(result, "获取课程包列表成功");
    }
}
