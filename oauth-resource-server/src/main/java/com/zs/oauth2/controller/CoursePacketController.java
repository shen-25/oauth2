package com.zs.oauth2.controller;

import com.zs.oauth2.service.CoursePacketService;
import com.zs.core.module.Result;
import com.zs.oauth2.constant.Constant;
import com.zs.oauth2.model.request.AddCoursePacketReq;
import com.zs.oauth2.model.request.UpdateCoursePacketReq;
import com.zs.oauth2.service.CourseRelatePacketService;
import com.zs.oauth2.utils.PageInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zengshen
 * @since 2023-08-08
 */
@RestController
public class CoursePacketController {


    @Autowired
    private CoursePacketService coursePacketService;

    @Autowired
    private CourseRelatePacketService courseRelatePacketService;


    /**
     * 新建课程包
     */
    @PostMapping("/coursePacket")
    public Result<String> addCoursePacket(@Valid @RequestBody AddCoursePacketReq addCoursePacketReq) {
        coursePacketService.addCoursePacket(addCoursePacketReq);
        return Result.succeed(null, "添加课程包成功");

    }

    /**
     * 分页查询: 课程包名称，面向对象。获取课程包列表
     */
    @GetMapping("/coursePacket/getCoursePacketList")
    public Result<PageInfoResult> getModelTypeList(@RequestParam(value = "pageSize") Integer pageSize,
                                                   @RequestParam(value = "current") Integer current,
                                                   @RequestParam(value = "name", required = false)String name,
                                                   @RequestParam(value = "target", required = false) Integer target) {

        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = Constant.COMMON_PAGE_SIZE;
        }
        PageInfoResult result =  coursePacketService.getCourseList(current, pageSize,
                name, target);
        return Result.succeed(result, "获取课程包分页列表成功");
    }


    /**
     * 修改课程包
     */
    @PutMapping("/coursePacket")
    public Result<String> updateCoursePacket(@Valid @RequestBody UpdateCoursePacketReq updateCoursePacketReq) {
        coursePacketService.updateCoursePacket(updateCoursePacketReq);
        return Result.succeed(null, "修改课程包成功");

    }


//
//    /**
//     * 更新课程分类
//     */
//    @PutMapping("/update")
//    public  Result<CourseCategory> updateCategory(@Valid @RequestBody UpdateCourseCateReq updateCourseCateReq) {
//
//        CourseCategory category = courseCategoryService.updateCategory(updateCourseCateReq);
//        return Result.succeed(category, "更新课程分类成功");
//
//    }
//
//
//    /**
//     * 根据id删除课程分类
//     */
//    @DeleteMapping("/delete")
//    public Result<String> deleteById(@RequestParam String id) {
//        courseCategoryService.deleteById(id);
//        return Result.succeed(null, "删除课程分类成功");
//
//    }

}
