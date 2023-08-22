package com.zs.oauth2.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddCourseCateReq {

    @NotBlank(message = "课程分类名称不能为空")
    private String categoryName;

    private String note;

    private Integer status;


}
