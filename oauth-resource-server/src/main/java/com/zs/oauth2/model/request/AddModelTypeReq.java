package com.zs.oauth2.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddModelTypeReq {

    @NotBlank(message = "模块类型名称不能为空")
    private String typeName;

    private String note;
    private Integer status;

}
