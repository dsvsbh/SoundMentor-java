package com.soundmentor.soundmentorbase.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Properties;
import java.util.Random;

public class MailUtil {
    private static final String userName163 = "fzw1938@163.com";
    private static final String password163 = "EWiPDkat4FXdtuh5";
    public static void main(String[] args) throws MessagingException {
        // 可以在这里直接测试方法，填自己的邮箱即可
        sendTestMail("1939729609@qq.com", achieveCode());
    }

    public static void sendTestMail(String email, Integer code) throws MessagingException {
        // 创建Properties 类用于记录邮箱的一些属性
        Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enale", "true");
        // 此处填写SMTP服务器
        props.put("mail.smtp.host", "smtp.163.com");
        // 端口号，SSL 端口为 465
        props.put("mail.smtp.port", "25");
        // 此处填写，发件人的账号
        props.put("mail.user", userName163);
        // 此处填写16位 STMP 授权码
        props.put("mail.password", password163);

        // 构建授权信息，用于进行 SMTP 进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };

        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);

        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);

        // 设置发件人
        InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
        message.setFrom(form);

        // 设置收件人的邮箱
        InternetAddress to = new InternetAddress(email);
        message.setRecipient(RecipientType.TO, to);

        // 设置邮件标题
        message.setSubject("SoundMentor 验证码");

        // 设置邮件的内容体
        message.setContent("尊敬的用户:你好!\n注册验证码为:" + code + "(有效期为5分钟,请勿告知他人)", "text/html;charset=UTF-8");

        // 最后当然就是发送邮件啦
        Transport.send(message);
    }

    public static Integer achieveCode() {
        Random random = new Random();
        int num = random.nextInt(900000) + 100000;
        return num;
    }
}
