package com.msgemployee.EmployeeSkillSearchProject.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
	
	@NotNull(message = "Please provide valid employee id")
	private String userId;
}
