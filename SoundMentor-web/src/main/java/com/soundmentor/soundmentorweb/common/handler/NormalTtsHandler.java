package com.soundmentor.soundmentorweb.common.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.enums.TaskStatusEnum;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorpojo.DO.NormalTtsRecord;
import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.task.CreateNormalTtsParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.common.mq.producer.MqProducer;
import com.soundmentor.soundmentorweb.config.mqConfig.DirectRabbitConfig;
import com.soundmentor.soundmentorweb.mapper.NormalTtsRecordMapper;
import com.soundmentor.soundmentorweb.mapper.TaskMapper;
import com.soundmentor.soundmentorweb.service.IUserSoundRelService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class NormalTtsHandler implements  TaskHandler{
    @Resource
    private IUserSoundRelService userSoundRelService;
    @Resource
    private UserInfoApi userInfoApi;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private MqProducer mqProducer;
    @Resource
    private NormalTtsRecordMapper normalTtsRecordMapper;

    @Override
    public Integer getTaskType() {
        return TaskTypeEnum.NORMAL_TTS.getCode();
    }

    @Override
    public void handleTimeoutTask(TaskDO task) {
        if(task.getStatus().equals(TaskStatusEnum.CREATED.getCode())||task.getStatus().equals(TaskStatusEnum.RUNNING.getCode()))
        {
            log.info("文本转语音任务超时", task.getId());
            task.setStatus(TaskStatusEnum.FAIL.getCode());
            HashMap<String, String> result = new HashMap<>();
            result.put("failReason", "文本转语音任务超时");
            task.setResult(JSON.toJSONString(result));
            taskMapper.updateById(task);
        }
    }

    @Override
    public void taskDone(TaskMessageDTO taskMessage) {
        Integer status = taskMessage.getStatus();
        if (status.equals(TaskStatusEnum.SUCCESS.getCode())) {
            log.info("NormalTtsHandler 任务成功,任务id:{}", taskMessage.getId());
            TaskDO taskDO = taskMapper.selectById(taskMessage.getId());
            taskDO.setStatus(TaskStatusEnum.SUCCESS.getCode());
            taskDO.setResult(JSONObject.toJSONString(taskMessage.getMessageBody()));
            taskMapper.updateById(taskDO);
            Map map = (Map)JSONObject.parse(taskDO.getTaskDetail());
            String content = map.get("content").toString();
            String rate = map.get("rate").toString();
            String voiceName = map.get("voiceName").toString();
            String userId = map.get("userId").toString();
            String mp3Name = content.substring(0,6)+".mp3";
            Map result = (Map)JSONObject.parse(taskDO.getResult());
            String mp3Url = result.get("content").toString();
            NormalTtsRecord normalTtsRecord = new NormalTtsRecord();
            normalTtsRecord.setFileName(mp3Name);
            normalTtsRecord.setFileUrl(mp3Url);
            normalTtsRecord.setRate(Integer.parseInt(rate));
            normalTtsRecord.setVoiceName(voiceName);
            normalTtsRecord.setUserId(Integer.parseInt(userId));
            normalTtsRecord.setCreateTime(LocalDateTime.now());
            normalTtsRecordMapper.insert(normalTtsRecord);
        } else {
            log.info("NormalTtsHandler 任务失败,任务id:{}", taskMessage.getId());
            TaskDO taskDO = taskMapper.selectById(taskMessage.getId());
            taskDO.setStatus(TaskStatusEnum.FAIL.getCode());
            HashMap<String, String> result = new HashMap<>();
            result.put("failReason", "语音合成任务失败");
            taskDO.setResult(JSONObject.toJSONString(result));
            taskMapper.updateById(taskDO);
        }
    }

    @Override
    public Integer createTask(CreateTaskParam param) {
        CreateNormalTtsParam createNormalTtsParam = (CreateNormalTtsParam) param;
        String content = createNormalTtsParam.getContent();
        Integer userSoundId = createNormalTtsParam.getUserSoundId();
        Integer rate = createNormalTtsParam.getRate();
        UserSoundRelDO sound = userSoundRelService.getById(userSoundId);
        if(Objects.isNull(sound)){
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"不存在该声音");
        }
        if(sound.getUserId()!=0)
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"系统暂不支持用户自训练声音");
        }
        if(content.length()<=0||content.length()>2000)
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"内容长度不合法,需满足1-2000字符");
        }
        TaskDO taskDO = new TaskDO();
        HashMap<String, String> taskDetail = new HashMap<>();
        taskDetail.put("content", content);
        taskDetail.put("rate", rate.toString());
        taskDetail.put("voiceName", sound.getApiParam());
        taskDetail.put("userId", userInfoApi.getUser().getId().toString());
        taskDO.setTaskDetail(JSONObject.toJSONString(taskDetail));
        taskDO.setType(TaskTypeEnum.NORMAL_TTS.getCode());
        taskDO.setStatus(TaskStatusEnum.CREATED.getCode());
        taskDO.setCreateTime(LocalDateTime.now());
        taskDO.setUpdateTime(LocalDateTime.now());
        taskMapper.insert(taskDO);
        TaskMessageDTO<Map> message = new TaskMessageDTO<>();
        message.setId(taskDO.getId());
        message.setMessageBody(taskDetail);
        message.setType(TaskTypeEnum.NORMAL_TTS.getCode());
        message.setStatus(TaskStatusEnum.CREATED.getCode());
        message.setCreateTime(LocalDateTime.now());
        mqProducer.send(DirectRabbitConfig.EXCHANGE_NAME_NORMAL_TTS, DirectRabbitConfig.ROUTING_KEY_NORMAL_TTS, message);
        return taskDO.getId();
    }

    @Override
    public Integer timeLimit() {
        return 3*60*1000;
    }
}
