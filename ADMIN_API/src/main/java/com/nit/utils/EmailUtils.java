package com.nit.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public boolean sendEmail(String to,String subject,String body) {
		boolean isSent=false;
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper= new MimeMessageHelper(mimeMessage);
			
//			System.out.println("Email-true-0"+to);
			helper.setTo(to.trim());
//			System.out.println("Email-true-1"+to.trim());
			helper.setSubject(subject);
			helper.setText(body,true);
			
//			System.out.println("Email-true-2");
			mailSender.send(mimeMessage);
			isSent=true;
//			System.out.println("Email-true-3");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
//		System.out.println("Email-"+isSent);
		return isSent;
	}

}
