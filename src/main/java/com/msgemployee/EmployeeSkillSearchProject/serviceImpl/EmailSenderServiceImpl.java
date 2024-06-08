package com.msgemployee.EmployeeSkillSearchProject.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.msgemployee.EmployeeSkillSearchProject.service.EmailSenderService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService{
	
	@Value("${spring.mail.username}")
	private String mailFrom;
	
	private JavaMailSender mailSender;

	@Override
	public void sendSimpleEmail(String toEmatil, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mailFrom);
		message.setTo(toEmatil);
		message.setText(body);
		message.setSubject(subject);
		mailSender.send(message);
		
		log.info("Mail Sent to: "+message.getTo());
		
	}

}
