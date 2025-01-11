package com.soundmentor.soundmentorpojo.DTO.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 通用IDList请求参数
 * @Author: Make
 * @DATE: 2025/01/11
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdListParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID集合
     **/
    @NotNull(message = "ID集合不能为空")
    private List<Integer> IdList;
}
