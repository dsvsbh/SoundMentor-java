package com.soundmentor.soundmentorweb.service;

import com.soundmentor.soundmentorpojo.DTO.ppt.BatchEditPPTExplanationDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.BatchEditPPTVoiceExplanationDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskQueryResultDTO;

import java.util.List;

public interface PPTService {

    /**
     * 创建PPT任务
     * 创建一个有声ppt任务（创建接口），创建1个task、n个task_detail,
     * 生成每页预览图上传并落库url，页面查询展示ppt的预览图，任务状态为创建。
     * @param url ppt地址
     * @return
     */
    Long createPPTTask(String url);
    /**
     * 生成讲解
     * 用户执行讲解生成任务，任务状态改为讲解生成中并提交异步任务，异步任务解析每页内容并发请求LLM获取讲解落库，
     * 所有页完成后流传状态->讲解已生成，前端轮训获取状态和信息。
     * @param taskId 任务id
     */
    void generateExplanation(Long taskId);

    /**
     * 编辑讲解
     * 用户编辑讲解
     * @param batchEditPPTExplanationDTO 批量编辑讲解DTO
     */
    void batchEditExplanation(BatchEditPPTExplanationDTO batchEditPPTExplanationDTO);

    /**
     * 生成讲解语音
     * @param taskId 任务id
     */
    void generateExplanationVoice(Long taskId);

    /**
     * 批量编辑讲解语音
     * @param batchEditPPTVoiceExplanationDTO 批量编辑讲解语音DTO
     */
    void batchEditExplanationVoice(BatchEditPPTVoiceExplanationDTO batchEditPPTVoiceExplanationDTO);

    /**
     * 生成有声ppt
     * @param taskId 任务id
     */
    void generateSoundPPT(Long taskId);

    /**
     * 根据任务id查询任务
     * @param taskId 任务id
     * @return 任务信息
     */
    PptTaskQueryResultDTO queryTask(Long taskId);

    /**
     * 列出当前用户的所有任务
     * @return 任务列表
     */
    List<PptTaskDTO> listTasks();
}
