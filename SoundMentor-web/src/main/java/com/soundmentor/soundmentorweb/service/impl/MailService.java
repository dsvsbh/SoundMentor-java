package com.soundmentor.soundmentorweb.service.impl;

import cn.hutool.core.util.StrUtil;
import com.soundmentor.soundmentorbase.constants.SoundMentorConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

@Service
public class MailService {
    @Resource
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private  String FROM_EMAIL;

    public void sendTestMail(String email, Integer code) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(FROM_EMAIL);
            helper.setTo(email);
            helper.setSubject("Sound - Mentot 验证码");
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            // 定义日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);
            String content = StrUtil.format(SoundMentorConstant.MAIL_CONTENT, code, currentDate.format(formatter));
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public  Integer achieveCode() {
        Random random = new Random();
        int num = random.nextInt(900000) + 100000;
        return num;
    }
}
