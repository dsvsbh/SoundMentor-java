package com.soundmentor.soundmentorpojo.DTO.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveOrganizationUserDTO {
    @NotEmpty(message = "organizationId不能为空")
    private Integer organizationId;
    @NotEmpty(message = "userId不能为空")
    private Integer userId;
}
