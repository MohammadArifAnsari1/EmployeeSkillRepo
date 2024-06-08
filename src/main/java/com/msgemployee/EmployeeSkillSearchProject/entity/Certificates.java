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
@Table(name = "certificates")
@Data
@JsonInclude(Include.NON_NULL)
public class Certificates {
	
	@Id
	@Column(name = "certificate_id")
	private String certificateId;
	
	@Column(name = "certificate_name")
	private String certificateName;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "employee_id")
	private Employee employee;

}
