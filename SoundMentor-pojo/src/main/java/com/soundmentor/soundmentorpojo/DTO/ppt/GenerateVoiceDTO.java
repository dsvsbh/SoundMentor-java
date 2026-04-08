package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateVoiceDTO {
    private Long taskId;
    private String voice;
    private Float speed;
}