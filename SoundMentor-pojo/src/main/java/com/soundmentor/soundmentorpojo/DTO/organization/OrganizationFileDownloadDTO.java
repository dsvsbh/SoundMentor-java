package com.soundmentor.soundmentorpojo.DTO.organization;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrganizationFileDownloadDTO {
    @NotNull(message = "organizationId不能为空")
    private Integer organizationId;
    @NotNull(message = "fileId不能为空")
    private Integer fileId;
}
