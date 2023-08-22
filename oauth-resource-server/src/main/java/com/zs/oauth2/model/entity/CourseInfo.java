package com.zs.oauth2.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author zengshen
 * @since 2023-08-16
 */
@Data
@TableName("course_info")
public class CourseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object id;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程类型
     */
    private Object courseCategory;

    /**
     * 课程介绍
     */
    private String courseDescribe;

    /**
     * 课程封面地址
     */
    private String coverPicUrl;

    /**
     * 课程大纲路径
     */
    private String courseOutlineUrl;

    /**
     * 前置课程
     */
    private Object preRequisiteId;

    /**
     * 是否为推荐课程 1是 0 否
     */
    private Integer isRecommend;

    /**
     * 0:配套云桌面 ，1配套Jupyte，2配套codeserver 3 不选择
     */
    private Integer environment;

    /**
     * 课程版本
     */
    private String version;

    /**
     * 章节数量
     */
    private Integer chapterNum;

    /**
     * 课时数
     */
    private Integer classHourNum;

    /**
     * 引用对象(0 中职 1 高职 2 本科)
     */
    private Integer target;

    /**
     * 引用次数
     */
    private Integer citeNum;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 状态 （0 下架 1 上架)
     */
    private Integer status;

    /**
     * 模板Id
     */
    private Object templateId;

    /**
     * 模板集群数量
     */
    private Integer clustersNum;

    /**
     * 是否是最新版本
     */
    private Boolean isLatestVersion;


}
