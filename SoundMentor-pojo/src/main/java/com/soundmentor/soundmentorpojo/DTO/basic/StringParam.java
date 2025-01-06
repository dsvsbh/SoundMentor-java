package com.soundmentor.soundmentorpojo.DTO.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 公共的String参数
 *
 * @Author: Make
 * @DATE: 2025/01/05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringParam {
    /**
     * 用户密码等相关参数
     *
     * @see String
     */
    @NotNull(message = "参数不能为空")
    protected  String param;
}