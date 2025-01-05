package com.soundmentor.soundmentorweb.config;

import com.google.common.collect.ImmutableMultimap;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局异常处理
 * @Author: Make
 * @DATE: 2025/01/05
 **/
@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice {

    /**
     * 用于从唯一字段重复的数据库异常中提取关键字.
     * 此处为 PG 数据的重复报错格式, 仅作为示例.
     */
    private final Pattern DUP_KEY_ERR_PTN = Pattern.compile("详细: Key \\((.+)\\)=\\((.+)\\) already exists\\.$");
    /**
     * 数据库表字段名(或唯一索引名) -> 展示给用户看的中文提示
     * eg: unique_index_username -> 用户名
     */
    private final ImmutableMultimap<String, String> COLUMN_CN;

    public CommonControllerAdvice() {
        COLUMN_CN = ImmutableMultimap.<String, String>builder().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseDTO<String> IllegalStateExceptionHandler(IllegalStateException e) {
        log.info("[{}] - {}", ResultCodeEnum.INTERNAL_ERROR.getCode(), e.getMessage());
        return ResponseDTO.fail(ResultCodeEnum.INTERNAL_ERROR, e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public ResponseDTO<String> baseExceptionHandler(BizException cuzEx) {
        log.info("[{}] - {}", cuzEx.getCode(), cuzEx.getMessage());
        return ResponseDTO.fail(cuzEx.getCode(), cuzEx.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseDTO<String> methodArgumentNotValidExceptionHandler(BindException cuzEx) {
        String message = String.format("请求参数校验失败 -> %s : %s.%s = %s",
                Objects.requireNonNull(cuzEx.getFieldError()).getDefaultMessage(),
                cuzEx.getObjectName(),
                cuzEx.getFieldError().getField(),
                cuzEx.getFieldError().getRejectedValue());
        log.info("[{}] - {}", ResultCodeEnum.INVALID_PARAM.getCode(), message);
        return ResponseDTO.fail(ResultCodeEnum.INVALID_PARAM.getCode(), message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException verificationEx) {
        FieldError fe = verificationEx.getBindingResult().getFieldError();
        String message = String.format("请求参数校验失败 -> %s : %s.%s = %s",
                fe.getDefaultMessage(), fe.getObjectName(), fe.getField(), fe.getRejectedValue());
        log.info("[{}] - {}", ResultCodeEnum.INVALID_PARAM.getCode(), message);

        return ResponseDTO.fail(ResultCodeEnum.INVALID_PARAM.getCode(), message);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseDTO<String> duplicateKeyException(DuplicateKeyException dkEx) {
        if (StringUtils.isNotBlank(dkEx.getMessage())) {
            Matcher m = DUP_KEY_ERR_PTN.matcher(dkEx.getMessage());
            if (m.find() && Objects.nonNull(COLUMN_CN.get(m.group(1)))) {
                String errMsg = String.format("%s重复: %s", COLUMN_CN.get(m.group(1)), m.group(2));
                log.error("[{}] - {}", ResultCodeEnum.DUPLICATED.getCode(), errMsg);
                return ResponseDTO.fail(ResultCodeEnum.DUPLICATED.getCode(), errMsg);
            }
        }
        log.error("[{}] - {}", ResultCodeEnum.DUPLICATED.getCode(), dkEx.getMessage());
        return ResponseDTO.fail(ResultCodeEnum.DUPLICATED.getCode(), "唯一字段重复");
    }

    @ExceptionHandler({ServletRequestBindingException.class, HttpMessageNotReadableException.class, TypeMismatchException.class})
    public ResponseDTO<String> servletRequestBindingException(Exception srbEx) {
        log.info("[{}] - {}", ResultCodeEnum.HTTP_REQUEST_ERROR.getCode(), srbEx.getMessage());
        return ResponseDTO.fail(ResultCodeEnum.HTTP_REQUEST_ERROR.getCode(), "错误的客户端请求. 请检查参数格式/请求路径/HTTP方法");
    }

    @ExceptionHandler(Exception.class)
    public ResponseDTO<String> exceptionHandle(Exception e) {
        log.error("[{}] - ", ResultCodeEnum.INTERNAL_ERROR.getCode(), e);
        return ResponseDTO.fail(e);
    }
}
