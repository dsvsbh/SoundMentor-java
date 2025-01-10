package com.soundmentor.soundmentorpojo.DTO.organization;

import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 查询用户组织列表的返回值
 * @author lzc
 */
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
    /**
     * 组织角色 0 普通成员 1 组织管理员 2 组织创建者
     */
    private Integer organizationRole;
    /**
     * 用户数量
     */
    private Integer userCount;
    /**
     * 文件数量
     */
    private Integer fileCount;
}
