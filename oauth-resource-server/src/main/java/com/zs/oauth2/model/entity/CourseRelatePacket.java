package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zengshen
 * @since 2023-08-16
 */
@TableName("course_relate_packet")
public class CourseRelatePacket implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    /**
     * 课程包id
     */
    private Object packetId;

    /**
     * 课程id
     */
    private Object courseId;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getPacketId() {
        return packetId;
    }

    public void setPacketId(Object packetId) {
        this.packetId = packetId;
    }

    public Object getCourseId() {
        return courseId;
    }

    public void setCourseId(Object courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "CourseRelatePacket{" +
            "id = " + id +
            ", packetId = " + packetId +
            ", courseId = " + courseId +
        "}";
    }
}
