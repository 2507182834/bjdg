package com.dagu.service.impl;

import com.dagu.service.MailService;
import com.dagu.pojo.MailModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MimeMessage mimeMessage;
    private static Logger logger = Logger.getLogger(MailServiceImpl.class);

    /**
     * 发送html格式的，带附件的邮件
     */
    @Override
    public void sendAttachMail(MailModel mail) {

        try {
            MimeMessageHelper mailMessage = new MimeMessageHelper(
                    this.mimeMessage, true, "UTF-8");
            mailMessage.setFrom(mail.getFromAddress());// 设置邮件消息的发送者

            mailMessage.setSubject(mail.getSubject());// 设置邮件消息的主题
            mailMessage.setSentDate(new Date());// 设置邮件消息发送的时间
            mailMessage.setText(mail.getContent(), true); // 设置邮件正文，true表示以html的格式发送

            String[] toAddresses = mail.getToAddresses().split(";");// 得到要发送的地址数组
            for (int i = 0; i < toAddresses.length; i++) {
                mailMessage.setTo(toAddresses[i]);
//                for (String fileName : mail.getAttachFileNames()) {
//                    mailMessage.addAttachment(fileName, new File(fileName));
//                }
                // 发送邮件
                this.mailSender.send(this.mimeMessage);
                logger.info("send mail ok=" + toAddresses[i]);
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }

    }
}