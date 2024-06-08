package com.msgemployee.EmployeeSkillSearchProject.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailSenderService {
	public void sendSimpleEmail(String toEmatil, String subject, String body);
}
