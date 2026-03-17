package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchEditPPTVoiceExplanationDTO {
    private List<EditPPTVoiceExplanationDTO> editPPTVoiceExplanationDTOList;
    @Data
    public static class EditPPTVoiceExplanationDTO {
        private Long pptTaskDetailId;
        private String newVoicePath;
    }
}