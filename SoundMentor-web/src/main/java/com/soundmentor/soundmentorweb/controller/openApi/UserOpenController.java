package com.soundmentor.soundmentorweb.controller.openApi;

import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.user.req.AddUserParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.ForgetPasswordParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.UserLoginParamByPassword;
import com.soundmentor.soundmentorpojo.DTO.user.res.UserDTO;
import com.soundmentor.soundmentorweb.MQ.Producer.MqProducer;
import com.soundmentor.soundmentorweb.biz.UserBiz;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import cn.hutool.core.lang.UUID;
/**
 * 用户相关免登接口
 * @Author: Make
 * @DATE: 2025/01/06
 **/
@RestController
@RequestMapping("/openApi/user")
public class UserOpenController {
    public static final String ADD_USER = "/addUser";
    public static final String LOGIN = "/login";
    public static final String SEND_EMAIL = "/sendEmail";
    public static final String FORGET_PASSWORD = "/forgetPassword";
    @Resource
    private UserBiz userBiz;
    @Resource
    private MqProducer mqProducer;


    /**
     * 发送邮件
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping( SEND_EMAIL)
    public ResponseDTO<Boolean> sendEmail(@RequestParam("email") String email){
        AssertUtil.hasLength(email, "邮箱不能为空");
        return ResponseDTO.OK(userBiz.sendEmail(email));
    }

    /**
     * 用户注册
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(ADD_USER)
    public ResponseDTO<Integer> addUser(@Valid @RequestBody AddUserParam param){
        return ResponseDTO.OK(userBiz.addUser(param));
    }

    /**
     * 用户登录
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(LOGIN)
    public ResponseDTO<UserDTO> login(@Valid @RequestBody UserLoginParamByPassword param){
        return ResponseDTO.OK(userBiz.login(param));
    }

    /**
     * 忘记密码
     * @PARAM: @param param
     * @RETURN: @return
     **/
    @PostMapping(FORGET_PASSWORD)
    public ResponseDTO<Boolean> forgetPassword(@Valid @RequestBody ForgetPasswordParam param){
        return ResponseDTO.OK(userBiz.fogetPassword(param));
    }

    /**
     * MQ测试：发送Direct模式消息
     * @PARAM:
     * @RETURN: @return
     **/
    @GetMapping("/sendDirectMessage")
    public ResponseDTO<String> sendDirectMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        mqProducer.send("TestDirectExchange", "TestDirectRouting", map);
        return ResponseDTO.OK("OK");
    }

    /**
     * MQ测试：发送主题模式消息
     * @PARAM:
     * @RETURN: @return
     **/
    @GetMapping("/sendTopicMessage1")
    public ResponseDTO<String> sendTopicMessage1() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: M A N ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> manMap = new HashMap<>();
        manMap.put("messageId", messageId);
        manMap.put("messageData", messageData);
        manMap.put("createTime", createTime);
        mqProducer.send("topicExchange", "topic.man", manMap);
        return ResponseDTO.OK("OK");
    }

    /**
     * MQ测试：发送广播消息
     * @PARAM:
     * @RETURN: @return
     **/
    @GetMapping("/sendTopicMessage2")
    public ResponseDTO<String> sendTopicMessage2() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: woman is all ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> womanMap = new HashMap<>();
        womanMap.put("messageId", messageId);
        womanMap.put("messageData", messageData);
        womanMap.put("createTime", createTime);
        mqProducer.send("topicExchange", "topic.woman", womanMap);
        return ResponseDTO.OK("OK");
    }
}
