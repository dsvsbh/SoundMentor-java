package com.soundmentor.soundmentorbase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @Author: Make
 * @DATE: 2025/01/05
 **/
@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    /**
     * 正常返回状态码：OK
     *
     * @since 1.0.0
     */
    OK("0", "OK"),
    /**
     * 错误状态码
     **/
    FAIL("1", "FAIL"),
    /**
     * 用于直接显示提示用户的错误，内容由输入内容决定
     */
    SHOW_FAIL("2", ""),
    /**
     * 业务异常
     *
     * @since 1.0.0
     */
    INTERNAL_ERROR("2025001", "业务异常"),

    /**
     * 非法参数
     *
     * @since 1.0.0
     */
    INVALID_PARAM("2025002", "非法参数"),

    /**
     * json转换异常
     *
     * @since 1.0.0
     */
    JSON_CONVERT_ERROR("2025003", "json转换异常"),

    /**
     * 并发事务冲突
     *
     * @since 1.0.0
     */
    CONCURRENT_TRANSACTION_CONFLICT("2025004", "并发事务冲突"),

    /**
     * 唯一字段重复
     *
     * @since 1.0.0
     */
    DUPLICATED("2025005", "唯一字段重复"),

    /**
     * 重复操作
     *
     * @since 1.0.0
     */
    REPEATED_OPERATION("2025006", "重复的操作"),

    /**
     * 状态错误
     *
     * @since 1.0.0
     */
    ILLEGAL_STATUS("2025007", "状态错误"),

    /**
     * 未获取到token，或者token未传递
     *
     * @since 1.0.0
     */
    UNAUTHORIZED("2025009", "认证未通过"),

    /**
     * 错误的客户端请求. 请检查参数格式/请求路径/HTTP方法
     *
     * @since 1.0.0
     */
    HTTP_REQUEST_ERROR("2025011", "错误的客户端请求,请检查参数格式/请求路径/HTTP方法"),
    /**
     * 未知的错误
     *
     * @since 1.0.0
     */
    UNKNOWN_ERROR("2025012", "未知的异常"),

    /**
     * MQ事件推送失败
     *
     * @since 1.0.0
     */
    MQ_PUSH_ERROR("2025100", "MQ事件推送失败"),

    /**
     * 数据找不到
     *
     * @date 2022/12/08
     * @see ResultCodeEnum
     */
    DATA_NOT_FUND("2025204", "数据不存在"),
    /**
     * 无数据权限
     *
     * @since 1.0.0
     */
    DATA_DENIED("2025201", "无数据权限"),
    FILE_ERROR("2025202", "文件异常"),

    ;


    /**
     * 状态码
     *
     * @since 1.0.0
     */
    public final String code;
    /**
     * 响应内容
     *
     * @since 1.0.0
     */
    public final String msg;
    public String code() {
        return code;
    }

    public String message() {
        return msg;
    }

    private static final Map<String, ResultCodeEnum> MAP = new HashMap<>(Stream.of(values()).collect(Collectors.toMap(ResultCodeEnum::getCode, Function.identity())));

    public static ResultCodeEnum valueOfCode(String code) {
        return MAP.get(code);
    }

}
