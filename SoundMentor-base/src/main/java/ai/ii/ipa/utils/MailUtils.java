package ai.ii.ipa.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Properties;
import org.springframework.stereotype.Component;
import java.util.Random;
@Component
public class MailUtils {
    public void sendTestMail(String email, String code) throws MessagingException {
        // 创建Properties 类用于记录邮箱的一些属性
        Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", "smtp.qq.com");
        //端口号，QQ邮箱端口587
        props.put("mail.smtp.port", "587");
        // 此处填写，写信人的账号
        props.put("mail.user", "1939729609@qq.com");
        // 此处填写16位STMP口令
        props.put("mail.password", "dmenvocfizyzdbgc");
        // 构建授权信息，用于进行SMTP进行身份验证
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
        message.setSubject("Fzw'S 每日数码新闻速递");
        // 设置邮件的内容体
        message.setContent( code , "text/html;charset=UTF-8");

        Transport.send(message);
    }

    public String achieveCode() {
        Random random = new Random();
        int num = random.nextInt(900000) + 100000;
        String numStr = String.valueOf(num);
        return numStr;
    }
}