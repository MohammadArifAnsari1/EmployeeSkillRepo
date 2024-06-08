package com.msgemployee.EmployeeSkillSearchProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "education")
@Data
@JsonInclude(Include.NON_NULL)
public class Education {

	@Id
	@Column(name = "education_id")
	private String educationId;

	@Column(name = "education_from")
	private String educationFrom;

	@Column(name = "education_to")
	private String educationTo;

	@Column(name = "degree")
	private String degree;

	@Column(name = "field")
	private String field;

	@Column(name = "university")
	private String university;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "employee_id")
	private Employee employeeId;

}
