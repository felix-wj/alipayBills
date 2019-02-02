package cn.wangjie.alipay.utils;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @program: bonus-point-cloud
 * @description: 发送邮件
 * @author: WangJie
 * @create: 2018-05-02 13:20
 **/
public class SendEmailUtil {
    private static final String authorizationCode = "fzm123456";
    private static final String from = "17826873177@163.com";
    private static final String host = "smtp.163.com";
    public  static void   sendEmail(String toEmail,String emailTitle,String emailContent)
    {


        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.auth", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            @Override
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(from, authorizationCode);
            }
        });
        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));

            // Set Subject: 头部头字段
            message.setSubject(emailTitle);
            Date date = new Date();
            // 设置消息体
            message.setText(emailContent);

            // 发送消息
            Transport.send(message);


        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    public  static void   sendImageEmail(String toEmail,String emailTitle,String emailContent)
    {


        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.auth", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            @Override
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(from, authorizationCode);
            }
        });
        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));

            // Set Subject: 头部头字段
            message.setSubject(emailTitle);
            Date date = new Date();
            // 设置消息体
            message.setContent(emailContent,"text/html;charset=utf-8");

            // 发送消息
            Transport.send(message);


        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
