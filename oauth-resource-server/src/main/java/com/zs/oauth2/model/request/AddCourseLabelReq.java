package com.zs.oauth2.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 添加课程标签请求数据
 * @author 35536
 */
@Data
public class AddCourseLabelReq {

    @NotBlank(message = "课程标签名称不能为空")
    private String labelName;
    private String note;

}
