package com.msgemployee.EmployeeSkillSearchProject.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.msgemployee.EmployeeSkillSearchProject.entity.User;
import com.msgemployee.EmployeeSkillSearchProject.service.PdfGeneratorService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("msg/employeeSkillSearchReport/pdf")
public class PdfExportController {
	
	@Autowired
	private PdfGeneratorService pdfGeneratorService;
	
	@PostMapping("/generate")
	public void pdfGenerate(@RequestBody User user, HttpServletResponse httpResponse) throws DocumentException, IOException{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDate = dateFormat.format(new Date());

		httpResponse.setContentType("application/pdf");
		httpResponse.setHeader("Content-Disposition", "attachment; filename=pdf_"+currentDate+".pdf");
		
		pdfGeneratorService.export(httpResponse, user.getUserId());
	}
	
}
