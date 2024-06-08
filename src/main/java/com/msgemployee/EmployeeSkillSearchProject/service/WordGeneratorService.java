package com.msgemployee.EmployeeSkillSearchProject.service;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

public interface WordGeneratorService {

	public void export(HttpServletResponse httpResponse, String userId) throws DocumentException, IOException;
	
}
