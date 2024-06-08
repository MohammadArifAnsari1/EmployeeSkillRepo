package com.msgemployee.EmployeeSkillSearchProject.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Data
@JsonInclude(Include.NON_NULL)
public class User {

	@Id
	@Column(name = "UserId")
	private String userId;

	@Column(name = "Password")
	private String password;

	@Column(name = "Role")
	private String role;
	
	private String oldPassword;
	private String newPassword;

}
