package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PPTPageSummaryTaskDTO {
    private Integer userPptId;
    private Integer page;
    private String content;
}
