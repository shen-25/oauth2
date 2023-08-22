package com.zs.oauth2.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateModelTypeReq {

    @NotBlank(message = "模块类型id不能为空")
    private String id;

    private String typeName;

    private String note;

    private Integer status;

}
