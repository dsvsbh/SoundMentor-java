package com.soundmentor.soundmentorweb.controller;


import com.soundmentor.soundmentorpojo.DTO.PresetSoundDTO;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorweb.service.INormalTtsRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预设文本朗读相关接口
 *
 * @author liuzhicheng
 * @since 2025-04-13
 */
@RestController
@RequestMapping("/tts")
public class NormalTTSController {
    @Resource
    private INormalTtsRecordService normalTtsRecordService;

    /**
     * 查询声音库
     * @return 预设声音列表
     */
    @GetMapping("/listVoices")
    public ResponseDTO<List<PresetSoundDTO>> listVoices() {
        return ResponseDTO.OK(normalTtsRecordService.listVoices());
    }
}