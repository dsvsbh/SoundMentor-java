package com.soundmentor.soundmentorpojo.DTO.organization;

import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrganizationFileListReqDTO {
    @NotNull(message = "organizationId不能为空")
    private Integer organizationId;
    private List<FileTypeEnum> fileTypes;
    private String fileName;
    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
}
