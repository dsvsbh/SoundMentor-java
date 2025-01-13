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
    private Integer id;
    private Integer type;
    private LocalDateTime createTime;
    private T messageBody;
}
