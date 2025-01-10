package com.soundmentor.soundmentorpojo.DTO.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResDTO {
    private String originalFileName;
    private String fileUrl;
}
