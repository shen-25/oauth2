package com.zs.oauth2.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zs.oauth2.mapper.CourseCategoryMapper;
import com.zs.oauth2.model.entity.CourseCategory;
import com.zs.oauth2.model.request.AddCourseCateReq;
import com.zs.oauth2.model.vo.CourseCategoryOptVO;
import com.zs.oauth2.service.CourseCategoryService;
import com.zs.oauth2.enums.YesOrNo;
import com.zs.oauth2.model.bo.CustomUser;
import com.zs.oauth2.model.request.UpdateCourseCateReq;
import com.zs.oauth2.utils.PageInfoResult;
import com.zs.oauth2.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory>
        implements CourseCategoryService {

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;


    @Override
    public void addCourseCategory(AddCourseCateReq courseCateReq) {
        if (this.getByCategoryName(courseCateReq.getCategoryName()) != null) {
            throw new RuntimeException("课程分类名称已存在, 添加失败");
        }
        // 状态为空或者不是启动，停用对应的状态码，设置默认为启动状态码
        if (!isCategoryValid(courseCateReq.getStatus())) {
            courseCateReq.setStatus(YesOrNo.YES.getType());
        }
        CourseCategory category = new CourseCategory();
        BeanUtils.copyProperties(courseCateReq, category);
        category.setId(UUID.randomUUID());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("请登录");
        }
        Object principal = auth.getPrincipal();
        CustomUser user = JSONUtil.toBean(String.valueOf(principal), CustomUser.class);
        category.setCreater(user.getUsername());
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        courseCategoryMapper.insert(category);
    }

    @Override
    public PageInfoResult getCourseCategoryList(Integer current, Integer pageSize, String categoryName, Integer status) {
        //开启分页功能
        PageHelper.startPage(current, pageSize);
        QueryWrapper<CourseCategory> query = new QueryWrapper<>();
        if (StringUtils.isNotBlank(categoryName)) {
            query.like("category_name", categoryName);
        }
        if (isCategoryValid(status)) {
            query.eq("status", status);
        }
        List<CourseCategory> list = courseCategoryMapper.selectList(query);
        return PageUtil.setPageInfoResult(list, current);
    }

    @Override
    public List<CourseCategoryOptVO> getCourseCategroyOptList() {
        QueryWrapper<CourseCategory> query = new QueryWrapper<>();
        query.select("id", "category_name");
        List<CourseCategoryOptVO> res = new ArrayList<>();
        List<CourseCategory> courseCategoryList = courseCategoryMapper.selectList(query);
        for (CourseCategory category : courseCategoryList) {
            CourseCategoryOptVO optVO = new CourseCategoryOptVO();
            optVO.setId(category.getId());
            optVO.setCategoryName(category.getCategoryName());
            res.add(optVO);
        }
        return res;
    }

    @Override
    public CourseCategory getByCategoryName(String categoryName) {
        QueryWrapper<CourseCategory> query = new QueryWrapper<>();
        query.eq("category_name", categoryName);
        return courseCategoryMapper.selectOne(query);
    }

    @Override
    public void deleteById(String id) {
        CourseCategory exit = this.getById(id);
        if (exit == null) {
            throw new RuntimeException("课程分类不存在");
        }
        QueryWrapper<CourseCategory> query = new QueryWrapper<>();
        query.eq("id", id);
        courseCategoryMapper.delete(query);
    }

    @Override
    public CourseCategory getById(String id) {
        QueryWrapper<CourseCategory> query = new QueryWrapper<>();
        query.eq("id", UUID.fromString(id));
        return courseCategoryMapper.selectOne(query);
    }

    @Override
    public CourseCategory updateCategory(UpdateCourseCateReq categoryReq) {
        CourseCategory exit = this.getById(categoryReq.getId());
        // 1.判断课程分类是否存在
        if (exit == null) {
            throw new RuntimeException("课程分类不存在，更新失败");
        }
        // 1.1 判断课程名称是否需要修改
        if (StringUtils.isNotBlank(categoryReq.getCategoryName())&&
              !StringUtils.equals(categoryReq.getCategoryName(), exit.getCategoryName())) {
            CourseCategory category = this.getByCategoryName(categoryReq.getCategoryName());
            // 1.1.1 判断课程名称是否存在
            if (category != null) {
                throw new RuntimeException("课程分类名称已经存在");
            }
            // 1.1.2 设置课程名称
            exit.setCategoryName(categoryReq.getCategoryName());
        }
        // 1.2 判断课程分类状态是否正确
        if (isCategoryValid(categoryReq.getStatus())) {
            exit.setStatus(categoryReq.getStatus());
        }else{
            throw new RuntimeException("课程分类状态错误");
        }
        // 1.3 备注不为空才更新
        if (categoryReq.getNote() != null) {
            exit.setNote(categoryReq.getNote());
        }
        exit.setUpdateTime(new Date());
        courseCategoryMapper.updateById(exit);
        return exit;
    }

    /**
     * 判断课程分类是否正确
     */
    private boolean isCategoryValid(Integer status) {
        if (status == null) {
            return false;
        }
        return status.equals(0) || status.equals(1);
    }


}
