package com.msgemployee.EmployeeSkillSearchProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.msgemployee.EmployeeSkillSearchProject.controller"})
@OpenAPIDefinition(
		info=@Info(title="Employee Skill Search Project", version="V2"),
		servers={@Server(url="http://localhost:8080"),@Server(url="http://oneoone.com")})
public class EmployeeSkillSearchProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeSkillSearchProjectApplication.class, args);
	}

}
