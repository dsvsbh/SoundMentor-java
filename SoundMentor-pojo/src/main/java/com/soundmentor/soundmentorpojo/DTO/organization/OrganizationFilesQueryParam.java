package com.soundmentor.soundmentorpojo.DTO.organization;

import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class OrganizationFilesQueryParam {
    private Integer organizationId;
    private List<Integer> fileTypes;
    private String fileName;
}
