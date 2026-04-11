package com.soundmentor.soundmentorweb.controller;


import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.PresetSoundDTO;
import com.soundmentor.soundmentorpojo.DTO.basic.PageResult;
import com.soundmentor.soundmentorpojo.DTO.tts.NormalTtsGenerateReqDTO;
import com.soundmentor.soundmentorpojo.DTO.tts.NormalTtsRecordResDTO;
import com.soundmentor.soundmentorweb.service.INormalTtsRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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

    /**
     * 生成预设文本朗读音频
     * @param reqDTO 生成请求DTO
     * @return 生成的记录
     */
    @PostMapping("/generate")
    public ResponseDTO<NormalTtsRecordResDTO> generateAudio(@Valid @RequestBody NormalTtsGenerateReqDTO reqDTO) {
        return ResponseDTO.OK(normalTtsRecordService.generateAudio(reqDTO));
    }

    /**
     * 分页查询当前用户的预设文本朗读历史记录
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    @GetMapping("/records")
    public ResponseDTO<PageResult<NormalTtsRecordResDTO>> getRecords(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseDTO.OK(normalTtsRecordService.getRecordsByPage(pageNum, pageSize));
    }

    /**
     * 批量删除预设文本朗读记录
     * @param ids 记录ID列表
     * @return
     */
    @PostMapping("/batchDelete")
    public ResponseDTO<Void> batchDelete(@RequestBody List<Integer> ids) {
        normalTtsRecordService.batchDelete(ids);
        return ResponseDTO.OK();
    }
}