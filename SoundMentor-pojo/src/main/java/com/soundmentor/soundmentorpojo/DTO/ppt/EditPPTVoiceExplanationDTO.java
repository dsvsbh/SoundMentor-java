package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPPTVoiceExplanationDTO {
    private Long pptTaskDetailId;
    private String newVoicePath;
}