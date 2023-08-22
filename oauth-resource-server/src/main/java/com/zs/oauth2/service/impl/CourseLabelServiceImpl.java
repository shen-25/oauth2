package com.zs.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.oauth2.mapper.CourseLabelMapper;
import com.zs.oauth2.model.entity.CourseLabel;
import com.zs.oauth2.model.request.AddCourseLabelReq;
import com.zs.oauth2.model.vo.CourseLabelOptVO;
import com.zs.oauth2.service.CourseLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CourseLabelServiceImpl extends ServiceImpl<CourseLabelMapper, CourseLabel>
        implements CourseLabelService {


    @Autowired
    private CourseLabelMapper courseLabelMapper;

    @Override
    public void addCourseLabel(AddCourseLabelReq courseLabelReq) {
        if (this.getByLabelName(courseLabelReq.getLabelName()) != null) {
            throw new RuntimeException("课程标签名称已存在, 添加失败");
        }
        CourseLabel courseLabel = new CourseLabel();
        courseLabel.setId(UUID.randomUUID());
        courseLabel.setLabelName(courseLabelReq.getLabelName());
        courseLabel.setNote(courseLabelReq.getNote());
        courseLabel.setCreateTime(new Date());
        courseLabelMapper.insert(courseLabel);

    }

    @Override
    public List<CourseLabelOptVO> getCourseLabelOptList() {
        QueryWrapper<CourseLabel> query = new QueryWrapper<>();
        query.select("id", "label_name");
        List<CourseLabelOptVO> res = new ArrayList<>();
        List<CourseLabel> courseLabelList = courseLabelMapper.selectList(query);
        for (CourseLabel courseLabel : courseLabelList) {
            CourseLabelOptVO optVO = new CourseLabelOptVO();
            optVO.setId(courseLabel.getId());
            optVO.setLabelName(courseLabel.getLabelName());
            res.add(optVO);
        }
        return res;
    }

    @Override
    public CourseLabel getByLabelName(String labelName) {
        QueryWrapper<CourseLabel> query = new QueryWrapper<>();
        query.eq("label_name", labelName);
        return courseLabelMapper.selectOne(query);
    }

    @Override
    public void deleteById(String id) {
        CourseLabel exit = this.getById(id);
        if (exit == null) {
            throw new RuntimeException("课程标签不存在");
        }
        QueryWrapper<CourseLabel> query = new QueryWrapper<>();
        query.eq("id", id);
        courseLabelMapper.delete(query);
    }

    @Override
    public CourseLabel getById(String id) {
        QueryWrapper<CourseLabel> query = new QueryWrapper<>();
        query.eq("id", UUID.fromString(id));
        return courseLabelMapper.selectOne(query);
    }
}
