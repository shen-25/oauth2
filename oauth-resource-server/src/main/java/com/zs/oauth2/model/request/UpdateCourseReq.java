package com.zs.oauth2.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UpdateCourseReq {

    @NotBlank(message = "课程id不能为空")
    private String id;
    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    /**
     * 课程类型
     */
    @NotBlank(message = "课程类型不能为空")
    private String courseCategory;

    /**
     * 课程介绍
     */
    private String courseDescribe;

    /**
     * 前置课程
     */
    private String preRequisiteId;

    /**
     * 是否为推荐课程 1是 0 否
     */
    @Max(value = 1, message = "是否为推荐课程 1是 0 否")
    @Min(value = 0, message = "是否为推荐课程 1是 0 否")
    private Integer isRecommend;

    /**
     * 0:配套云桌面 ，1配套Jupyte，2配套codeserver 3 不选择
     */
    private Integer environment;


    /**
     * 封面图片文件
     */
    private MultipartFile file;
    /**
     * 课程大纲文件
     */
    private MultipartFile outLine;

    /**
     * 课程标签id列表
     */
    private List<String> lableList;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 引用对象(0 中职 1 高职 2 本科)
     */
    @Min(value = 0, message = "面向对象错误,(0 中职 1 高职 2 本科)")
    @Max(value = 2, message = "面向对象错误,(0 中职 1 高职 2 本科)")
    private Integer target;

    /**
     * 模板集群数量
     */
    private Integer clustersNum;

    /**
     * 版本
     */
    @NotBlank(message = "课程版本不能为空")
    private String version;


}
