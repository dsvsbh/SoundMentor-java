package com.soundmentor.soundmentorpojo.DTO;

import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * 返回结果统一封装
 * @Author: Make
 * @DATE: 2025/01/05
 **/
public class ResponseDTO<T> implements Serializable {

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


    public static <T> ResponseDTO<T> ok(T data) {
        ResponseDTO<T> responseDTO = new ResponseDTO<>();
        responseDTO.setData(data);
        responseDTO.setMessage(ResultCodeEnum.OK.message());
        responseDTO.setCode(ResultCodeEnum.OK.code());
        responseDTO.setDatetime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return responseDTO;
    }

    public static <T> ResponseDTO<T> ok() {
        ResponseDTO<T> responseDTO = new ResponseDTO<>();
        responseDTO.setCode(ResultCodeEnum.OK.code());
        responseDTO.setMessage(ResultCodeEnum.OK.message());
        return responseDTO;
    }

    public static <T> ResponseDTO<T> fail() {
        ResponseDTO<T> responseDTO = new ResponseDTO<>();
        responseDTO.setMessage(ResultCodeEnum.FAIL.message());
        responseDTO.setCode(ResultCodeEnum.FAIL.code());
        return responseDTO;
    }

    public static <T> ResponseDTO<T> fail(ResultCodeEnum ResultCodeEnum) {
        ResponseDTO<T> responseDTO = new ResponseDTO<>();
        responseDTO.setMessage(ResultCodeEnum.message());
        responseDTO.setCode(ResultCodeEnum.code());
        return responseDTO;
    }

    public static <T> ResponseDTO<T> fail(ResultCodeEnum ResultCodeEnum, T data) {
        ResponseDTO<T> responseDTO = new ResponseDTO<>();
        responseDTO.setMessage(ResultCodeEnum.message());
        responseDTO.setCode(ResultCodeEnum.code());
        responseDTO.setData(data);
        return responseDTO;
    }

    public static <T> ResponseDTO<T> failBusinessException(String code, String msg) {
        ResponseDTO<T> responseDTO = new ResponseDTO<>();
        responseDTO.setMessage(msg);
        responseDTO.setCode(code);
        return responseDTO;
    }


    /**
     * 前端显示失败消息
     * @param msg 失败消息
     * @return
     */
    public static <T> ResponseDTO<T> showFailMsg(String msg) {
        ResponseDTO<T> serverResponseDTO = new ResponseDTO<>();
        serverResponseDTO.setMessage(msg);
        serverResponseDTO.setCode(ResultCodeEnum.SHOW_FAIL.code());
        return serverResponseDTO;
    }
}
