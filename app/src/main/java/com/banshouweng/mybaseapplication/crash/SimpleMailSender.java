package com.banshouweng.mybaseapplication.crash;
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

/**
 * ���ʼ��������������ʼ���������   
 */    
public class SimpleMailSender  
{    
	/**   
	 * ���ı���ʽ�����ʼ�   
	 * @param mailInfo �����͵��ʼ�����Ϣ   
	 */    
	public boolean sendTextMail(MailSenderInfo mailInfo) 
	{
		// �ж��Ƿ���Ҫ�����֤    
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

	/**   
	 * ��HTML��ʽ�����ʼ�   
	 * @param mailInfo �����͵��ʼ���Ϣ   
	 */    
	public static boolean sendHtmlMail(MailSenderInfo mailInfo)
	{    
		// �ж��Ƿ���Ҫ�����֤    
		MyAuthenticator authenticator = null;   
		Properties pro = mailInfo.getProperties();
		//�����Ҫ�����֤���򴴽�һ��������֤��     
		if (mailInfo.isValidate()) 
		{    
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());   
		}    
		// �����ʼ��Ự���Ժ�������֤������һ�������ʼ���session    
		Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
		try {    
			// ����session����һ���ʼ���Ϣ    
			Message mailMessage = new MimeMessage(sendMailSession);
			// �����ʼ������ߵ�ַ    
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// �����ʼ���Ϣ�ķ�����    
			mailMessage.setFrom(from);    
			// �����ʼ��Ľ����ߵ�ַ�������õ��ʼ���Ϣ��    
			Address to = new InternetAddress(mailInfo.getToAddress());
			// Message.RecipientType.TO���Ա�ʾ�����ߵ�����ΪTO    
			mailMessage.setRecipient(Message.RecipientType.TO,to);
			// �����ʼ���Ϣ������    
			mailMessage.setSubject(mailInfo.getSubject());    
			// �����ʼ���Ϣ���͵�ʱ��    
			mailMessage.setSentDate(new Date());
			// MiniMultipart����һ�������࣬����MimeBodyPart���͵Ķ���    
			Multipart mainPart = new MimeMultipart();
			// ����һ������HTML���ݵ�MimeBodyPart    
			BodyPart html = new MimeBodyPart();
			// ����HTML����
			Log.d("==error==",mailInfo.getContent());
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");    
			mainPart.addBodyPart(html);    
			// ��MiniMultipart��������Ϊ�ʼ�����    
			mailMessage.setContent(mainPart);    
			// �����ʼ�    
			Transport.send(mailMessage);
			return true;    
		} catch (Exception ex) {
			ex.printStackTrace();    
		}    
		return false;    
	}    

}
