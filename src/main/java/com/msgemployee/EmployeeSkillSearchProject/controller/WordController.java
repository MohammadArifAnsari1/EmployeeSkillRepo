package com.msgemployee.EmployeeSkillSearchProject.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.msgemployee.EmployeeSkillSearchProject.service.WordGeneratorService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("msg/employeeSkillSearchReport/word")
public class WordController {

	@Autowired
	private WordGeneratorService wordGeneratorService;
	
	@GetMapping("/generate/{userId}")
	public void wordGenerate(@PathVariable String userId, HttpServletResponse httpResponse) throws DocumentException, IOException{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDate = dateFormat.format(new Date());

		httpResponse.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		httpResponse.setHeader("Content-Disposition", "attachment; filename="+userId+"-docx_"+currentDate+".docx");
		
		wordGeneratorService.export(httpResponse, userId);
	}
	
//	@PostMapping("/generate")
//	public void wordGenerate(@RequestBody User user, HttpServletResponse httpResponse) throws DocumentException, IOException{
//		
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
//		String currentDate = dateFormat.format(new Date());
//
//		httpResponse.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//		httpResponse.setHeader("Content-Disposition", "attachment; filename=docx_"+currentDate+".docx");
//		
//		wordGeneratorService.export(httpResponse, user.getUserId());
//	}
	
}
