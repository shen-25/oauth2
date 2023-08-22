package com.zs.oauth2.service;

import com.zs.oauth2.model.entity.CourseRelatePacket;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zengshen
 * @since 2023-08-16
 */
public interface CourseRelatePacketService extends IService<CourseRelatePacket> {

    void addCourseRelatePacket(String courseId, String packetId);

    void deleteByPacketId(String packetId);

    void deleteByCourseId(String courseId);

    /**
     *  根据packetId获取列表
     */
    List<CourseRelatePacket> getByPacketId(String packetId);

    List<String> getCourseIdsByPacketId(String packetId);
}
