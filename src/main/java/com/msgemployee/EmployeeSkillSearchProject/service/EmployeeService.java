package com.msgemployee.EmployeeSkillSearchProject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeResponse;

@Service
public interface EmployeeService {
	
	public List<EmployeeResponse> getAllEmployees();
	public EmployeeResponse getEmployeeDetails(String empUserIdOrEmpName);
	public List<EmployeeResponse> getFilteredEmployeeDetails(EmployeeResponse filterRequest);

}
