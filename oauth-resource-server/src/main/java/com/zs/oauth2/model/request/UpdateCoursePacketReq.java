package com.zs.oauth2.model.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UpdateCoursePacketReq {

    @NotBlank(message = "课程包id不能为空")
    private String id;

    private String name;

    /**
     * 引用对象(0 中职 1 高职 2 本科)
     */
    @Min(value = 0, message = "面向对象错误,(0 中职 1 高职 2 本科)")
    @Max(value = 2, message = "面向对象错误,(0 中职 1 高职 2 本科)")
    private Integer target;

    private String note;

    private List<String> courseIdList;
}
