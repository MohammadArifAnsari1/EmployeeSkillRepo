package com.msgemployee.EmployeeSkillSearchProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeResponse;
import com.msgemployee.EmployeeSkillSearchProject.entity.User;
import com.msgemployee.EmployeeSkillSearchProject.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/msg/employeeSkillSearchReport")
@Slf4j
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@PostMapping("/register")
	@Operation(summary = "Register employee ")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Employee registered successfully", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = EmployeeResponse.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid User Details supplied", 
			    content = @Content) })
	public String register(@RequestBody User user){
		log.info("Request to register for employee started");
		String message = loginService.register(user);
		return message;
	}

	@PostMapping("/userlogin")
	@Operation(summary = "Login employee ")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Employee logged-in successfully", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = EmployeeResponse.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid User Details supplied", 
			    content = @Content) })
	public String login(@RequestBody User user) throws Exception{
		log.info("Request to login for employee started");
		String message = loginService.loginUser(user);
		return message;
	}

	@PostMapping("/passwordUpdate")
	@Operation(summary = "Update password for employee ")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Employee password updated successfully", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = EmployeeResponse.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid User Details supplied", 
			    content = @Content) })
	public String passwordUpdate(@RequestBody User user) throws Exception{
		log.info("Requested employee to update password started");
		String message = loginService.passwordUpdate(user);
		return message;
	}

	@PostMapping("/email")
	@Operation(summary = "Request valid email of employee ")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Fetched Employee emailId successfully", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = EmployeeResponse.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid User Details supplied", 
			    content = @Content) })
	public String findByEmail(@RequestBody User user) throws Exception{
		log.info("Requested employee to update password started");
		String message = loginService.findById(user.getUserId());
		return message;
	}

	@PostMapping("/validation")
	@Operation(summary = "Validate employee credential")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Employee validated successfully", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = EmployeeResponse.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid User Details supplied", 
			    content = @Content) })
	public String authentication(@RequestBody User user) throws Exception{
		log.info("Requested employee to update password started");
		String message = loginService.authentication(user.getUserId());
		return message;
	}
}
