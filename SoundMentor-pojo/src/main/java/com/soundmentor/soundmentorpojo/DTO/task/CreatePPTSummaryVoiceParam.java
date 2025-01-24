package com.soundmentor.soundmentorpojo.DTO.task;

import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import lombok.Data;

@Data
public class CreatePPTSummaryVoiceParam implements CreateTaskParam{
    private TaskTypeEnum type;
    private TaskTypeEnum taskType;
    /**
     * 用户pptid（执行讲解任务返回的）
     */
    private Integer userPptId;
    /**
     * 用户声音id
     */
    private String userSoundId;
    /**
     * 语速 正常语速的倍数
     */
    private Integer rate;
}
