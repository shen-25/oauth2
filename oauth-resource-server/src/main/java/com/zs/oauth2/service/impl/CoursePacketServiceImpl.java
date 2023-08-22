package com.zs.oauth2.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zs.oauth2.mapper.CoursePacketMapper;
import com.zs.oauth2.model.entity.CourseInfo;
import com.zs.oauth2.model.entity.CoursePacket;
import com.zs.oauth2.model.vo.CoursePacketPageVO;
import com.zs.oauth2.service.CourseInfoService;
import com.zs.oauth2.service.CoursePacketService;
import com.zs.oauth2.model.bo.CustomUser;
import com.zs.oauth2.model.entity.Users;
import com.zs.oauth2.model.request.AddCoursePacketReq;
import com.zs.oauth2.model.request.UpdateCoursePacketReq;
import com.zs.oauth2.model.vo.CourseRelateCategoryVO;
import com.zs.oauth2.service.CourseRelatePacketService;
import com.zs.oauth2.service.UsersService;
import com.zs.oauth2.utils.PageInfoResult;
import com.zs.oauth2.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class CoursePacketServiceImpl extends ServiceImpl<CoursePacketMapper, CoursePacket>
        implements CoursePacketService {

    @Autowired
    private CoursePacketMapper coursePacketMapper;

    @Autowired
    private CourseRelatePacketService courseRelatePacketService;

    @Autowired
    private CourseInfoService courseInfoService;

    @Autowired
    private UsersService usersService;

    @Override
    @Transactional
    public void addCoursePacket(AddCoursePacketReq addCoursePacketReq) {
        CoursePacket exit = this.getByName(addCoursePacketReq.getName());
        if (exit != null) {
            throw new RuntimeException("课程包名称已存在,新增课程包失败");
        }
        CoursePacket coursePacket = new CoursePacket();
        UUID uuid = UUID.randomUUID();
        coursePacket.setId(uuid);
        coursePacket.setName(addCoursePacketReq.getName());
        coursePacket.setTarget(addCoursePacketReq.getTarget());
        coursePacket.setNote(addCoursePacketReq.getNote());
        coursePacket.setCourseAmount(0);
        coursePacket.setQuoteNum(0);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String principalStr = String.valueOf(auth.getPrincipal());
        if ("anonymousUser".equals(principalStr)) {
            throw new RuntimeException("请登录");
        }
        CustomUser user = JSONUtil.toBean(principalStr, CustomUser.class);
        coursePacket.setCreater(UUID.fromString(user.getId()));
        coursePacket.setCreateTime(new Date());
        coursePacket.setUpdateTime(new Date());
        coursePacketMapper.insert(coursePacket);
        List<String> courseIdList = addCoursePacketReq.getCourseIdList();
        if (!CollectionUtils.isEmpty(courseIdList)) {
            for (String courId : courseIdList) {
                CourseInfo exitCourseInfo = courseInfoService.getById(courId);
                if (exitCourseInfo == null) {
                    throw new RuntimeException("课程不存在,新增课程包失败");
                }
                courseRelatePacketService.addCourseRelatePacket(courId, uuid.toString());
            }
        }
    }

    @Override
    public PageInfoResult getCourseList(Integer current, Integer pageSize, String name, Integer target) {
        //开启分页功能
        PageHelper.startPage(current, pageSize);
        QueryWrapper<CoursePacket> query = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            query.like("name", name);
        }
        if (target != null) {
            query.eq("target", target);
        }
        List<CoursePacket> coursePacketList = coursePacketMapper.selectList(query);
        PageInfoResult pageInfoResult = PageUtil.setPageInfoResult(coursePacketList, current);
        List<CoursePacketPageVO> coursePacketPageVOList = new ArrayList<>();
        // 返回所有课程的数据(id, 课程名称，课程类别)
        if (!CollectionUtils.isEmpty(coursePacketList)) {
            List<CourseRelateCategoryVO> courseRelateCategoryVOList = courseInfoService.getCourseRelateCategoryVOList();
            for (CoursePacket coursePacket : coursePacketList) {
                CoursePacketPageVO coursePacketPageVO = new CoursePacketPageVO();
                BeanUtils.copyProperties(coursePacket, coursePacketPageVO);
                List<String> courseIdList = courseRelatePacketService.getCourseIdsByPacketId(String.valueOf(coursePacket.getId()));
                String join = StringUtils.join(courseIdList, ",");
                coursePacketPageVO.setCourseIdList(join);
                coursePacketPageVO.setCourseList(courseRelateCategoryVOList);
                // 获取创建人的用户名
                Users user = usersService.getById(String.valueOf(coursePacket.getCreater()));
                coursePacketPageVO.setCreateName(user.getUserName());
                coursePacketPageVOList.add(coursePacketPageVO);
            }
        }
        pageInfoResult.setRecords(coursePacketPageVOList);
        return pageInfoResult;

    }

    @Override
    @Transactional
    public CoursePacket updateCoursePacket(UpdateCoursePacketReq coursePacketReq) {
        CoursePacket exit = this.getById(coursePacketReq.getId());
        if (exit == null) {
            throw new RuntimeException("课程包不存在,更新失败");
        }
        CoursePacket byName = this.getByName(coursePacketReq.getName());
        if (byName != null && !coursePacketReq.getId().equals(byName.getId())) {
            throw new RuntimeException("课程包名称已存在,更新课程包失败");
        }
        // 判断是否需要更新
        if (StringUtils.isNotBlank(coursePacketReq.getName())) {
            exit.setName(coursePacketReq.getName());
        }else{
            throw new RuntimeException("课程名称不能为空字符串");
        }
        exit.setTarget(coursePacketReq.getTarget());
        if (coursePacketReq.getNote() != null) {
            exit.setNote(coursePacketReq.getNote());
        }
        exit.setUpdateTime(new Date());
        coursePacketMapper.updateById(exit);
        // 删除之前的课程包和课程信息关联关系
        courseRelatePacketService.deleteByPacketId(coursePacketReq.getId());
        List<String> courseIdList = coursePacketReq.getCourseIdList();
        if (!CollectionUtils.isEmpty(courseIdList)) {
            for (String courId : courseIdList) {
                CourseInfo exitCourseInfo = courseInfoService.getById(courId);
                if (exitCourseInfo == null) {
                    throw new RuntimeException("课程不存在,新增课程包失败");
                }
                courseRelatePacketService.addCourseRelatePacket(courId, coursePacketReq.getId());
            }
        }
        return exit;
    }

    @Override
    public CoursePacket getByName(String coursePacketName) {
        QueryWrapper<CoursePacket> query = new QueryWrapper<>();
        query.eq("name", coursePacketName);
        return coursePacketMapper.selectOne(query);
    }

    @Override
    public CoursePacket getById(String coursePacketId) {
        QueryWrapper<CoursePacket> query = new QueryWrapper<>();
        query.eq("id", UUID.fromString(coursePacketId));
        return coursePacketMapper.selectOne(query);
    }
}
