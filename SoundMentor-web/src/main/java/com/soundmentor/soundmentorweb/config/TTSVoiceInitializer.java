package com.soundmentor.soundmentorweb.config;

import com.alibaba.dashscope.aigc.multimodalconversation.AudioParameters;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soundmentor.soundmentorpojo.DO.PresetSoundDO;
import com.soundmentor.soundmentorweb.mapper.PresetSoundMapper;
import com.soundmentor.soundmentorweb.service.TTSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TTSVoiceInitializer implements CommandLineRunner {

    private final PresetSoundMapper presetSoundMapper;
    private final TTSService ttsService;

    private static final String SAMPLE_TEXT = "你好，这是一段试听音频";
    private static final float SAMPLE_SPEED = 1.0f;

    @Override
    public void run(String... args) {
        log.info("开始初始化预设声音列表...");
        
        try {
            // 获取所有的AudioParameters.Voice枚举值
            AudioParameters.Voice[] voices = AudioParameters.Voice.values();
            
            List<PresetSoundDO> newVoices = new ArrayList<>();
            
            for (AudioParameters.Voice voice : voices) {
                String apiParam = voice.name();
                
                // 检查是否已经存在该声音的记录
                LambdaQueryWrapper<PresetSoundDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(PresetSoundDO::getApiParam, apiParam);
                PresetSoundDO existingSound = presetSoundMapper.selectOne(wrapper);
                
                if (existingSound == null) {
                    log.info("开始为声音 [{}] 生成试听音频...", apiParam);
                    
                    try {
                        // 生成试听音频
                        String soundUrl = ttsService.textToSpeech(voice, SAMPLE_TEXT, SAMPLE_SPEED);
                        
                        // 创建新的预设声音记录
                        PresetSoundDO presetSound = new PresetSoundDO();
                        presetSound.setSoundUrl(soundUrl);
                        presetSound.setSoundName(apiParam);
                        presetSound.setApiParam(apiParam);
                        presetSound.setDescription("系统自动生成的"+apiParam+"试听音频");
                        
                        newVoices.add(presetSound);
                        log.info("声音 [{}] 试听音频生成成功，URL: {}", apiParam, soundUrl);
                        
                    } catch (Exception e) {
                        log.error("声音 [{}] 试听音频生成失败: {}", apiParam, e.getMessage(), e);
                    }
                } else {
                    log.info("声音 [{}] 已存在，跳过", apiParam);
                }
            }
            
            // 批量插入新记录
            if (!newVoices.isEmpty()) {
                for (PresetSoundDO presetSound : newVoices) {
                    presetSoundMapper.insert(presetSound);
                }
                log.info("成功插入 {} 条预设声音记录", newVoices.size());
            }
            
            log.info("预设声音列表初始化完成");
            
        } catch (Exception e) {
            log.error("预设声音列表初始化失败: {}", e.getMessage(), e);
        }
    }
}