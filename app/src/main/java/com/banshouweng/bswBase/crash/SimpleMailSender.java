package com.banshouweng.bswBase.crash;
import android.util.Log;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SimpleMailSender  
{    

	public boolean sendTextMail(MailSenderInfo mailInfo) 
	{

		MyAuthenticator authenticator = null;    
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) 
		{    
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}   
		Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
		try 
		{    
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mailInfo.getFromAddress());
			mailMessage.setFrom(from);
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO,to);
			mailMessage.setSubject(mailInfo.getSubject());
			mailMessage.setSentDate(new Date());
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);    
			Transport.send(mailMessage);
			return true;    
		} 
		catch (MessagingException ex)
		{    
			ex.printStackTrace();    
		}    
		return false;    
	}    


	public static boolean sendHtmlMail(MailSenderInfo mailInfo)
	{    

		MyAuthenticator authenticator = null;   
		Properties pro = mailInfo.getProperties();

		if (mailInfo.isValidate()) 
		{    
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());   
		}    

		Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
		try {    
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mailInfo.getFromAddress());
			mailMessage.setFrom(from);
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO,to);
			mailMessage.setSubject(mailInfo.getSubject());
			mailMessage.setSentDate(new Date());
			Multipart mainPart = new MimeMultipart();
			BodyPart html = new MimeBodyPart();
			Log.d("==error==",mailInfo.getContent());
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");    
			mainPart.addBodyPart(html);    
			mailMessage.setContent(mainPart);
			Transport.send(mailMessage);
			return true;    
		} catch (Exception ex) {
			ex.printStackTrace();    
		}    
		return false;    
	}    

}
