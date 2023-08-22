package com.zs.oauth2.controller;

import com.zs.core.module.Result;
import com.zs.oauth2.constant.Constant;
import com.zs.oauth2.model.entity.CourseCategory;
import com.zs.oauth2.model.request.AddCourseCateReq;
import com.zs.oauth2.model.request.UpdateCourseCateReq;
import com.zs.oauth2.model.vo.CourseCategoryOptVO;
import com.zs.oauth2.service.CourseCategoryService;
import com.zs.oauth2.utils.PageInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  课程分类接口
 * </p>
 *
 * @author zengshen
 * @since 2023-08-08
 */
@RestController
@RequestMapping("/courseCategory")
public class CourseCategoryController {

    @Autowired
    private CourseCategoryService courseCategoryService;

    /**
     * 新建课程分类
     */
    @PostMapping("/add")
    public Result<String> addCourseCategory(@Valid @RequestBody AddCourseCateReq addCourseCateReq) {
        courseCategoryService.addCourseCategory(addCourseCateReq);
        return Result.succeed(null, "添加课程分类成功");

    }

    /**
     * 获取课程分类列表
     */
    @GetMapping("/list")
    public Result<PageInfoResult> getModelTypeList(@RequestParam(value = "pageSize") Integer pageSize,
                                                   @RequestParam(value = "current") Integer current,
                                                   @RequestParam(value = "categoryName", required = false)String categoryName,
                                                   @RequestParam(value = "status", required = false) Integer status) {

        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = Constant.COMMON_PAGE_SIZE;
        }
        PageInfoResult result =  courseCategoryService.getCourseCategoryList(current, pageSize,
                categoryName, status);
        return Result.succeed(result, "获取课程分类列表成功");
    }

    /**
     * 获取课程分类列表选项
     */
    @GetMapping("/list/options")
    public Result getSelectModeTypeList() {
        List<CourseCategoryOptVO> list = courseCategoryService.getCourseCategroyOptList();
        return Result.succeed(list, "获取课程分类列表选项成功");
    }

    /**
     * 更新课程分类
     */
    @PutMapping("/update")
    public  Result<CourseCategory> updateCategory(@Valid @RequestBody UpdateCourseCateReq updateCourseCateReq) {

        CourseCategory category = courseCategoryService.updateCategory(updateCourseCateReq);
        return Result.succeed(category, "更新课程分类成功");

    }


    /**
     * 根据id删除课程分类
     */
    @DeleteMapping("/delete")
    public Result<String> deleteById(@RequestParam String id) {
        courseCategoryService.deleteById(id);
        return Result.succeed(null, "删除课程分类成功");

    }


}
