package com.soundmentor.soundmentorpojo.basic.res;

import com.soundmentor.soundmentorpojo.basic.enums.ResultCodeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 返回结果统一封装
 * @Author: Make
 * @DATE: 2025/01/05
 **/
public class ResponseEntity<T> implements Serializable {

    private String code;
    private String message;
    private String datetime;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static <T> ResponseEntity<T> ok(T data) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setData(data);
        responseEntity.setMessage(ResultCodeEnum.OK.message());
        responseEntity.setCode(ResultCodeEnum.OK.code());
        responseEntity.setDatetime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return responseEntity;
    }

    public static <T> ResponseEntity<T> ok() {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setCode(ResultCodeEnum.OK.code());
        responseEntity.setMessage(ResultCodeEnum.OK.message());
        return responseEntity;
    }

    public static <T> ResponseEntity<T> fail() {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setMessage(ResultCodeEnum.FAIL.message());
        responseEntity.setCode(ResultCodeEnum.FAIL.code());
        return responseEntity;
    }

    public static <T> ResponseEntity<T> fail(ResultCodeEnum ResultCodeEnum) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setMessage(ResultCodeEnum.message());
        responseEntity.setCode(ResultCodeEnum.code());
        return responseEntity;
    }

    public static <T> ResponseEntity<T> fail(ResultCodeEnum ResultCodeEnum, T data) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setMessage(ResultCodeEnum.message());
        responseEntity.setCode(ResultCodeEnum.code());
        responseEntity.setData(data);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> failBusinessException(String code, String msg) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setMessage(msg);
        responseEntity.setCode(code);
        return responseEntity;
    }


    /**
     * 前端显示失败消息
     * @param msg 失败消息
     * @return
     */
    public static <T> ResponseEntity<T> showFailMsg(String msg) {
        ResponseEntity<T> serverResponseEntity = new ResponseEntity<>();
        serverResponseEntity.setMessage(msg);
        serverResponseEntity.setCode(ResultCodeEnum.SHOW_FAIL.code());
        return serverResponseEntity;
    }
}
