package com.zs;

import com.zs.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author word
 */
@Data
public class ApiRestResponse<T> implements Serializable {

    /**
     * 响应状态码
     */
    private  int code;

    /**
     * 响应中的信息
     */
    private  String msg;

    /**
     * 响应的内容
     */
    private  T data;

    private ApiRestResponse() {
        this.code = ResultEnum.SUCCESS.getCode();
        this.msg = ResultEnum.SUCCESS.getMsg();
        this.data = null;
    }

    private ApiRestResponse(T data) {
        this.code = ResultEnum.SUCCESS.getCode();
        this.msg = ResultEnum.SUCCESS.getMsg();
        this.data = data;
    }

    private ApiRestResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    private ApiRestResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiRestResponse<T> success() {
        return new ApiRestResponse<T>();
    }
    public static <T> ApiRestResponse<T> success(T data) {
        return new ApiRestResponse<T>(data);
    }

    public static <T> ApiRestResponse<T> success(String msg, T data) {
        return new ApiRestResponse<T>(ResultEnum.SUCCESS.getCode(), msg, data);
    }

    public static  <T> ApiRestResponse<T> error() {
        return new ApiRestResponse<T>(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMsg());
    }

    public static  <T> ApiRestResponse<T> error(String msg) {
        return new ApiRestResponse<T>(ResultEnum.SYSTEM_ERROR.getCode(), msg);
    }

    public static  <T> ApiRestResponse<T> error(Integer code, String msg) {
        return new ApiRestResponse<T>(code, msg);
    }

    public static <T> ApiRestResponse<T> error(Integer code, String msg, T data) {
        return new ApiRestResponse<T>(code, msg, data);
    }


}
