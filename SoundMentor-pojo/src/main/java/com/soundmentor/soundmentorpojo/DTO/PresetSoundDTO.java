package com.soundmentor.soundmentorpojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresetSoundDTO {
    private Integer id;
    private String soundUrl;
    private String soundName;
    private String apiParam;
    private String description;
}