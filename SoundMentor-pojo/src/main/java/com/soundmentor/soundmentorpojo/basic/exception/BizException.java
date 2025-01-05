package com.soundmentor.soundmentorpojo.basic.exception;

import com.soundmentor.soundmentorpojo.basic.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 业务异常类
 * @Author: Make
 * @DATE: 2025/01/05
 **/
@Getter
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -1696290974375583053L;

    private String code;

    private String desc;

    private Object data;

    public BizException() {
        super();
    }

    public BizException(String msg) {
        super(msg);
        this.desc = msg;
        this.code = ResultCodeEnum.INTERNAL_ERROR.getCode();
    }

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
        this.desc = msg;
    }


    public BizException(String code, String msg, String desc) {
        super(msg);
        this.code = code;
        this.desc = desc;
    }

    public BizException(String code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.desc = msg;
        this.data = data;
    }

    public BizException(String code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
        this.desc = msg;
    }
}
