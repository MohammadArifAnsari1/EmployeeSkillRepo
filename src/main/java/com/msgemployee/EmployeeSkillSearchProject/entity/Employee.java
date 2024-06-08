package com.msgemployee.EmployeeSkillSearchProject.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "employee")
@Data
@JsonInclude(Include.NON_NULL)
public class Employee {
	
	@Id
	@Column(name = "employee_id")
	private String employeeId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "surname")
	private String surname;

	@Column(name = "email")
	private String email;

	@Column(name = "job_title")
	private String jobTitle;

	@Column(name = "community_of_practice")
	private String communityOfPractice;

	@Column(name = "internation_experience")
	private String internationExperience;

	@Column(name = "profile_picture")
	private String profilePicture;

	@Column(name = "line_manager")
	private String lineManager;

	@Column(name = "overall_experience")
	private String overallExperience;

	@Column(name = "level")
	private String level;

	@Column(name = "bench")
	private String bench;
}
