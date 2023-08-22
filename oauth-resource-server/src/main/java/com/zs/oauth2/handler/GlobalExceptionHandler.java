package com.zs.oauth2.handler;

import com.zs.core.module.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author 35536
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 自定义验证异常
     * MethodArgumentNotValidException 方法参数无效异常
     */
    @ExceptionHandler(BindException.class)
    public Result paramExceptionHandler(BindException  e) {
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!CollectionUtils.isEmpty(errors)) {
               // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                FieldError fieldError = (FieldError) errors.get(0);
                return Result.failed(fieldError.getDefaultMessage());
            }
        }
        return Result.failed("请求参数错误");
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        return Result.failed(e.getMessage());

    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return Result.failed(e.getMessage());

    }
    
}
