package com.msgemployee.EmployeeSkillSearchProject.service;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

public interface PdfGeneratorService {
	
	public void export(HttpServletResponse httpResponse, String employeeId) throws DocumentException, IOException;

}
