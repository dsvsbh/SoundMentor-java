package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PPTTaskResultDTO {
    private Integer userPptId;
    private Integer pptPage;
    private String summary;
    private String soundUrl;
}
