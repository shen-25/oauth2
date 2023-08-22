package com.zs.oauth2.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CoursePacketPageVO {

    private Object id;

    /**
     * 课程包名称
     */
    private String name;

    /**
     * 课程数量
     */
    private Integer courseAmount;

    /**
     * 面向对象 0 中职 1高职 2 本科
     */
    private Integer target;

    /**
     * 引用次数
     */
    private Integer quoteNum;

    /**
     * 备注
     */
    private String note;

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
     * 创建人id
     */
    private Object createName;

    private String courseIdList;

    private List<CourseRelateCategoryVO> courseList;
}
