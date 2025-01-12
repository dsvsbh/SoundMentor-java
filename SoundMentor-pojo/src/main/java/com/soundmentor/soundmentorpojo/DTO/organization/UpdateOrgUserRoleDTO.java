package com.soundmentor.soundmentorpojo.DTO.organization;

import com.soundmentor.soundmentorbase.enums.OrganizationRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrgUserRoleDTO {
    @NotEmpty(message = "organizationId不能为空")
    private Integer organizationId;
    @NotEmpty(message = "userId不能为空")
    private Integer userId;
    /**
     * 这儿只能修改ADMIN和USER角色，不能修改CREATOR角色
     */
    private OrganizationRole role;
}
