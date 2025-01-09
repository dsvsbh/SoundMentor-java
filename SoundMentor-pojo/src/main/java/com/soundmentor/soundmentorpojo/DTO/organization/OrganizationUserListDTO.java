package com.soundmentor.soundmentorpojo.DTO.organization;

import com.soundmentor.soundmentorbase.enums.OrganizationRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 查询组织成员列表的返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationUserListDTO {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户在组织的角色 0：普通用户 1：管理员 2：组织创建者
     */
    private Integer organizationRole;
    /**
     * 用户名字
     */
    private String name;
    /**
     * 用户头像
     */
    private String headImg;
    /**
     * 用户加入组织的时间
     */
    private LocalDateTime entryTime;
}
