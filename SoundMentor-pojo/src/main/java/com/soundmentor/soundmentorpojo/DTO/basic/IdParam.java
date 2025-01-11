package com.soundmentor.soundmentorpojo.DTO.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 公共的id查询参数
 *
 * @Author: Make
 * @DATE: 2025/01/05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdParam {
    /**
     * id
     *
     * @see String
     */
    @NotNull(message = "ID不能为空")
    protected Integer id;
}