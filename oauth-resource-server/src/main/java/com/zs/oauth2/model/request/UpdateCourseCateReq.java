package com.zs.oauth2.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateCourseCateReq {

    @NotBlank(message = "课程分类id不能为空")
    private String id;

    private String categoryName;

    private String note;

    private Integer status;

}
