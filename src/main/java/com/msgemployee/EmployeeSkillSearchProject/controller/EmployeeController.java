package com.msgemployee.EmployeeSkillSearchProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeResponse;
import com.msgemployee.EmployeeSkillSearchProject.entity.UserRequest;
import com.msgemployee.EmployeeSkillSearchProject.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("msg/employeeSkillSearchReport")
@Slf4j
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/employeeData/getAllEmployee")
	@Operation(summary = "Get all employee details")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Found Employees", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = EmployeeResponse.class)) }),
			  @ApiResponse(responseCode = "404", description = "Employees not found", 
			    content = @Content) })
	public List<EmployeeResponse> getAllEmployees(){
		log.info("Started fetching all employee response using getAllEmployees()");
		List<EmployeeResponse> allEmployees = employeeService.getAllEmployees();
		log.info("All employee record : {}",allEmployees);
		return allEmployees;
	}
	
	@PostMapping("/employeeData/getById")
	@Operation(summary = "Get the employee details")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Found the employee", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = EmployeeResponse.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
			    content = @Content), 
			  @ApiResponse(responseCode = "404", description = "Employee not found", 
			    content = @Content) })
	public EmployeeResponse getEmployeeDetails(@RequestBody @Valid UserRequest user){
		log.info("Started fetching employee details response using getEmployeeDetails({})",user.getUserId());
		EmployeeResponse employeeDetails = employeeService.getEmployeeDetails(user.getUserId());
		log.info("Employee record : {}",employeeDetails);
		return employeeDetails;
	}

	@PostMapping("/employeeData/filterEmployees")
	public List<EmployeeResponse> getEmployeeFilter(@RequestBody EmployeeResponse filterRequest){
		log.info("Started fetching employee details response using filterRequest");
		List<EmployeeResponse> employeesDetails = employeeService.getFilteredEmployeeDetails(filterRequest);
		log.info("Employee record : {}",employeesDetails);
		return employeesDetails;
	}
}
