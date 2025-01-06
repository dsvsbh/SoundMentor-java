package com.soundmentor.soundmentorpojo.DTO.organization;

import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationListDTO{
    /**
     * 组织id
     */
    private Integer id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     *  组织的容量
     */
    private Integer capacity;
}
