package com.tian.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class AsyncService {
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Async
    public void sendEmailCode(String email,String code) throws MessagingException {
        String mailText = "<h4>尊敬的用户：您好！</h4>\n" +
                "验证码：" + code + "，十五分钟内有效，请输入以完成操作。<br>\n" +
                "<br>\n" +
                "注意：验证码仅用于注册或修改邮箱。如非本人操作，请及时登录并修改密码以保证账户安全 <br>\n" +
                "<br>\n" +
                "------------------------------------------------------------- <br>\n" +
                "<br>\n" +
                "此为系统邮件，请勿回复<br>\n" +
                "请保管好您的邮箱，避免账号被他人盗用";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject("【影视在线】验证码！");
        helper.setText(mailText,true);
        helper.setTo(email);
        helper.setFrom("1097598544@qq.com");

        mailSender.send(message);
    }
}
