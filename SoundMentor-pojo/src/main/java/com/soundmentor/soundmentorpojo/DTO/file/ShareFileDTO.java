package com.soundmentor.soundmentorpojo.DTO.file;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShareFileDTO {
    @NotNull
    private Integer fileId;
    @NotNull
    private Integer organizationId;
}
