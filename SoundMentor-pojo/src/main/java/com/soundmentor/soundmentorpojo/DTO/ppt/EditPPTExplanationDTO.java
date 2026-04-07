package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPPTExplanationDTO {
    private Long pptTaskDetailId;
    private String newExplanation;
}