package io.com.vaccine.utils;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtilImpl implements EmailUtil{

	@Autowired
	JavaMailSender mailSender;
	


	@Override
	public void sendEmail(String toAddress, String subject, String body) {
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(toAddress);
			helper.setSubject(subject);
			helper.setText(body);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		mailSender.send(message);
		
	}

	@Override
	public void sendEmailPdf(String toAddress,String filePath) {
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(toAddress);
			helper.setSubject("Vaccine pdf");
			helper.setText("PDF");
			helper.addAttachment("Information", new File(filePath));
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		mailSender.send(message);
				
	}

}
