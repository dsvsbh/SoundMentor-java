package com.soundmentor.soundmentorweb.service.impl;

import com.soundmentor.soundmentorpojo.DTO.ppt.BatchEditPPTExplanationDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.BatchEditPPTVoiceExplanationDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskQueryResultDTO;
import com.soundmentor.soundmentorweb.service.FileService;
import com.soundmentor.soundmentorweb.service.PPTService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * PPTServiceImpl 有声ppt服务实现类 todo 待实现方法
 */
@Service
@Slf4j
public class PPTServiceImpl implements PPTService {
    @Resource
    private  UserInfoApi userInfoApi;
    @Resource(name = "task-thread-pool-executor")
    private  ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private FileService fileService;

    @Override
    public Long createPPTTask(String url) {
        // todo 创建一个有声ppt任务（创建接口），创建1个task、n个task_detail,生成每页预览图上传并落库url，页面查询展示ppt的预览图，任务状态为创建。
        return 0L;
    }

    @Override
    public void generateExplanation(Long taskId) {

    }

    @Override
    public void batchEditExplanation(BatchEditPPTExplanationDTO batchEditPPTExplanationDTO) {

    }

    @Override
    public void generateExplanationVoice(Long taskId) {

    }

    @Override
    public void batchEditExplanationVoice(BatchEditPPTVoiceExplanationDTO batchEditPPTVoiceExplanationDTO) {

    }

    @Override
    public void generateSoundPPT(Long taskId) {

    }

    @Override
    public PptTaskQueryResultDTO queryTask(Long taskId) {
        return null;
    }

    @Override
    public List<PptTaskDTO> listTasks() {
        return Collections.emptyList();
    }
}
