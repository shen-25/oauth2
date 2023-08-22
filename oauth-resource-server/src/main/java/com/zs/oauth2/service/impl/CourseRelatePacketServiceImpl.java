package com.zs.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zs.oauth2.mapper.CourseRelatePacketMapper;
import com.zs.oauth2.model.entity.CourseRelatePacket;
import com.zs.oauth2.service.CourseRelatePacketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
public class CourseRelatePacketServiceImpl extends ServiceImpl<CourseRelatePacketMapper, CourseRelatePacket> implements CourseRelatePacketService {

    @Autowired
    private CourseRelatePacketMapper courseRelatePacketMapper;

    @Override
    public void addCourseRelatePacket(String courseId, String packetId) {
        CourseRelatePacket courseRelatePacket = new CourseRelatePacket();
        courseRelatePacket.setId(UUID.randomUUID());
        courseRelatePacket.setCourseId(UUID.fromString(courseId));
        courseRelatePacket.setPacketId(UUID.fromString(packetId));
        courseRelatePacketMapper.insert(courseRelatePacket);
    }

    @Override
    public void deleteByPacketId(String packetId) {
        QueryWrapper<CourseRelatePacket> query = new QueryWrapper<>();
        query.eq("packet_id", UUID.fromString(packetId));
        courseRelatePacketMapper.delete(query);
    }

    @Override
    public void deleteByCourseId(String courseId) {
        QueryWrapper<CourseRelatePacket> query = new QueryWrapper<>();
        query.eq("course_id", UUID.fromString(courseId));
        courseRelatePacketMapper.delete(query);
    }

    @Override
    public List<CourseRelatePacket> getByPacketId(String packetId) {
      return   this.courseRelatePacketMapper.selectList(new QueryWrapper<CourseRelatePacket>()
                .eq("packet_id",  UUID.fromString(packetId)));
    }

    @Override
    public List<String> getCourseIdsByPacketId(String packetId) {
        List<CourseRelatePacket> courseRelatePacketList = courseRelatePacketMapper.selectList(new QueryWrapper<CourseRelatePacket>()
                .eq("packet_id", UUID.fromString(packetId))
                .select("course_id"));
        List<String> resList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(courseRelatePacketList)) {
            for (CourseRelatePacket courseRelatePacket : courseRelatePacketList) {
                resList.add(String.valueOf(courseRelatePacket.getCourseId()));
            }
        }
        return resList;
    }
}
