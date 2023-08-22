package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zengshen
 * @since 2023-08-15
 */
@TableName("vm_template_course")
public class VmTemplateCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    /**
     * 虚拟机id
     */
    private Object templateId;

    /**
     * 课程id
     */
    private Object courseId;

    /**
     * 集群数量
     */
    private Integer clustersNum;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Object templateId) {
        this.templateId = templateId;
    }

    public Object getCourseId() {
        return courseId;
    }

    public void setCourseId(Object courseId) {
        this.courseId = courseId;
    }

    public Integer getClustersNum() {
        return clustersNum;
    }

    public void setClustersNum(Integer clustersNum) {
        this.clustersNum = clustersNum;
    }

    @Override
    public String toString() {
        return "VmTemplateCourse{" +
            "id = " + id +
            ", templateId = " + templateId +
            ", courseId = " + courseId +
            ", clustersNum = " + clustersNum +
        "}";
    }
}
