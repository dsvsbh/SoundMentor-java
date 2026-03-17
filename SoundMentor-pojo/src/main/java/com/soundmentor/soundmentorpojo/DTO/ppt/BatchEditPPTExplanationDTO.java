package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchEditPPTExplanationDTO {
    private List<EditPPTExplanationDTO> editPPTExplanationDTOList;
    @Data
    public static class EditPPTExplanationDTO {
        private Long pptTaskDetailId;
        private String newExplanation;
    }
}
