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
@Table(name = "project")
@Data
@JsonInclude(Include.NON_NULL)
public class EmployeeProject {

	@Id
	@Column(name = "project_id")
	private String projectId;

	@Column(name = "project_name")
	private String projectName;

	@Column(name = "start_date")
	private String startDate;

	@Column(name = "end_date")
	private String endDate;

	@Column(name = "customer")
	private String customer;

	@Column(name = "location")
	private String location;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "project_role")
	private String projectRole;

	@Column(name = "project_discription")
	private String projectDiscription;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "employee_id")
	private Employee employeeId;

}
