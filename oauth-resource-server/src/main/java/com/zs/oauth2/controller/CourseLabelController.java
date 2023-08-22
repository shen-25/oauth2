package com.zs.oauth2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zs.oauth2.model.entity.CourseInfoLabel;
import com.zs.oauth2.model.vo.CourseLabelOptVO;
import com.zs.oauth2.service.CourseInfoLabelService;
import com.zs.oauth2.service.CourseLabelService;
import com.zs.core.module.Result;
import com.zs.oauth2.model.request.AddCourseLabelReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * 课程标签接口
 * @author 35536
 */
@RestController
@RequestMapping("/courseLabel")
public class CourseLabelController {

    @Autowired
    private CourseLabelService courseLabelService;

    @Autowired
    private CourseInfoLabelService courseInfoLabelService;

    /**
     * 新建课程标签
     */
    @PostMapping("/add")
    public Result<String> addCourseLabel(@Valid @RequestBody AddCourseLabelReq addCourseLabelReq) {
        courseLabelService.addCourseLabel(addCourseLabelReq);
        return Result.succeed(null, "添加课程标签成功");

    }

    /**
     * 根据id删除课程标签
     */
    @DeleteMapping("/delete")
    public Result<String> deleteById(@RequestParam String id) {
        List<CourseInfoLabel> labelId = courseInfoLabelService.list(new QueryWrapper<CourseInfoLabel>()
                .like("course_label_id", UUID.fromString(id)));
        if (!CollectionUtils.isEmpty(labelId)) {
            return Result.failed("删除失败,有课程关联标签!");
        }
        courseLabelService.deleteById(id);
        return Result.succeed(null, "删除课程标签成功");

    }

    /**
     * 获取课程标签列表选项
     */
    @GetMapping("/list/options")
    public Result getCourseLabelOptList() {
        List<CourseLabelOptVO> list = courseLabelService.getCourseLabelOptList();
        return Result.succeed(list, "获取课程标签列表选项成功");
    }




}
