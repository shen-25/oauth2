package com.zs.oauth2.service;

import com.zs.oauth2.model.entity.CoursePacket;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.oauth2.model.request.AddCoursePacketReq;
import com.zs.oauth2.model.request.UpdateCoursePacketReq;
import com.zs.oauth2.utils.PageInfoResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zengshen
 * @since 2023-08-16
 */
public interface CoursePacketService extends IService<CoursePacket> {

    void addCoursePacket(AddCoursePacketReq addCoursePacketReq);


    CoursePacket updateCoursePacket(UpdateCoursePacketReq coursePacketReq);

    /**
     * 根据课程包名称查找
     * @param coursePacketName
     * @return
     */
    CoursePacket getByName(String coursePacketName);

    PageInfoResult getCourseList(Integer current, Integer pageSize, String name, Integer target);

    CoursePacket getById(String coursePacketId);
}
