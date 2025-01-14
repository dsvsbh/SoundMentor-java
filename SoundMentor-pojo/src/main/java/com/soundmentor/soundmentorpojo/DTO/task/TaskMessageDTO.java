package com.soundmentor.soundmentorpojo.DTO.task;

import cn.hutool.core.date.DateTime;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * mq消息对象
 * @param <T>
 * @author  lzc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskMessageDTO<T> implements Serializable {
    /**
     * 任务id
     **/
    private Integer id;
    /**
     * 任务类型：
     * 1.语音训练
     * 2.ppt总结生成
     * 3.PPT讲解生成有声ppt
     **/
    private Integer type;
    /**
     * 任务状态：
     * 0：创建
     * 1：执行中
     * 2：执行完成
     * 3：执行失败
     **/
    private Integer status;
    /**
     * 创建时间
     **/
    private LocalDateTime createTime;
    /**
     * 任务消息
     **/
    private T messageBody;
}
