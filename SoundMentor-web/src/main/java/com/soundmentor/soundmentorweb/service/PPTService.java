package com.soundmentor.soundmentorweb.service;

import com.soundmentor.soundmentorpojo.DTO.task.CreatePPTSummaryTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreatePPTSummaryVoiceParam;

public interface PPTService {
    Integer createPPTSummary(CreatePPTSummaryTaskParam param);

    Integer createPPTSummaryVoice(CreatePPTSummaryVoiceParam param);
}
