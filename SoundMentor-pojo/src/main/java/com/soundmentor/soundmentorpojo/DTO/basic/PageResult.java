package com.soundmentor.soundmentorpojo.DTO.basic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResult<T> {
    private Long pageNum;
    private Long pageSize;
    private Long total;
    private Long pages;
    private List<T> records;
}
