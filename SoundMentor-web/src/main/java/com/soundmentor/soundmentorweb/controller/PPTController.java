package com.soundmentor.soundmentorweb.controller;

import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.EditPPTExplanationDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.EditPPTVoiceExplanationDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.GenerateVoiceDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskQueryResultDTO;
import com.soundmentor.soundmentorweb.service.PPTService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * author: lzc
 * desc: 提供有声ppt模块相关接口
 * date: 2026/3/10
 */
@RestController
@RequestMapping("/ppt")
public class PPTController {

    /**
     * 整体流程：
     * 流程：
     * 1.用户上传ppt（上传接口），创建一个有声ppt任务（创建接口），创建1个task、n个task_detail,生成每页预览图上传并落库url，解析每页文字内容落库，页面查询展示ppt的预览图，任务状态为创建。
     * 2.用户执行讲解生成任务，任务状态改为讲解生成中并提交异步任务，异步任务解析每页内容并发请求LLM获取讲解落库，所有页完成后流传状态->讲解已生成，前端轮训获取状态和信息。
     * 3.用户可以进行讲解的微调润色，进行讲解语音生成，任务状态改为语音生成中并提交异步任务，异步任务并发执行每页的讲解语音生成，请求api获取音频并上传获取url落库，所有页完成流转状态->语音已生成。
     * 4.用户可以对各页语音进行编辑，上传自己的语音进行替代，进行最终的ppt生成，状态改为有声ppt生成中并提交异步任务，异步任务将每页的讲解音频嵌入到原ppt并生成终版文件，上传并落库url改状态为有声ppt已生成。
     * 5.前端全程轮训获取实时状态。
     */

    @Resource
    private PPTService pptService;

    /**
     * 创建有声ppt任务
     * @param url ppt地址
     * @param taskName 任务名称
     * @return
     */
    @PostMapping("/createTask")
    public ResponseDTO<Long> createPPTTask(@RequestParam String url, @RequestParam(required = false) String taskName) {
        return ResponseDTO.OK(pptService.createPPTTask(url, taskName));
    }

    /**
     * 讲解生成
     * @param taskId 任务id
     * @return
     */
    @PostMapping("/generateExplanation")
    public ResponseDTO generateExplanation(@RequestParam Long taskId) {
        pptService.generateExplanation(taskId);
        return ResponseDTO.OK();
    }

    /**
     * 讲解单个编辑
     * @param editPPTExplanationDTO 单个编辑讲解DTO
     * @return
     */
    @PostMapping("/editExplanation")
    public ResponseDTO editExplanation(@RequestBody EditPPTExplanationDTO editPPTExplanationDTO) {
        pptService.editExplanation(editPPTExplanationDTO);
        return ResponseDTO.OK();
    }

    /**
     * 讲解语音生成
     * @param taskId 任务id
     * @return
     */
    @PostMapping("/generateExplanationVoice")
    public ResponseDTO generateExplanationVoice(@RequestBody GenerateVoiceDTO generateVoiceDTO) {
        pptService.generateExplanationVoice(generateVoiceDTO);
        return ResponseDTO.OK();
    }

    /**
     * 讲解语音单个编辑
     * @param editPPTVoiceExplanationDTO 单个编辑讲解语音DTO
     * @return
     */
    @PostMapping("/editExplanationVoice")
    public ResponseDTO editExplanationVoice(@RequestBody EditPPTVoiceExplanationDTO editPPTVoiceExplanationDTO) {
        pptService.editExplanationVoice(editPPTVoiceExplanationDTO);
        return ResponseDTO.OK();
    }

    /**
     * 有声ppt生成
     * @param taskId 任务id
     * @return
     */
    @PostMapping("/generateSoundPPT")
    public ResponseDTO generateSoundPPT(@RequestParam Long taskId) {
        pptService.generateSoundPPT(taskId);
        return ResponseDTO.OK();
    }

    /**
     * 任务查询
     * @param taskId 任务id
     * @return
     */
    @PostMapping("/queryTask")
    public ResponseDTO<PptTaskQueryResultDTO> queryTask(@RequestParam Long taskId) {
        return ResponseDTO.OK(pptService.queryTask(taskId));
    }

    /**
     * 列出当前用户的所有ppt任务
     * @return
     */
    @PostMapping("/listTasks")
    public ResponseDTO<List<PptTaskDTO>> listTasks() {
        return ResponseDTO.OK(pptService.listTasks());
    }
    
    /**
     * 批量删除PPT任务
     * @param taskIds 任务ID列表
     * @return
     */
    @PostMapping("/batchDeleteTasks")
    public ResponseDTO batchDeleteTasks(@RequestBody List<Long> taskIds) {
        pptService.batchDeleteTasks(taskIds);
        return ResponseDTO.OK();
    }
}