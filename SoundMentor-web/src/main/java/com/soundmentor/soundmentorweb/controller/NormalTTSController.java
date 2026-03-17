package com.soundmentor.soundmentorweb.controller;


import com.soundmentor.soundmentorpojo.DO.NormalTtsRecord;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.basic.PageResult;
import com.soundmentor.soundmentorweb.service.INormalTtsRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
     * 获取当前用户的预设文本朗读记录
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping
    public ResponseDTO<PageResult<NormalTtsRecord>> getRecords(Integer pageNum, Integer pageSize) {
        return ResponseDTO.OK(normalTtsRecordService.getRecords(pageNum, pageSize));
    }

    /**
     * 删除预设文本朗读记录
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseDTO<Void> deleteRecord(@PathVariable Integer id) {
        normalTtsRecordService.removeById(id);
        return ResponseDTO.OK();
    }
}
