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
@Table(name = "corecompetencies")
@Data
@JsonInclude(Include.NON_NULL)
public class CoreCompetencies {
	
	@Id
	@Column(name = "core_comp_id")
	private String coreCompId;
	
	@Column(name = "functional_skills")
	private String functionalSkill;
	
	@Column(name = "technical_skills")
	private String technicalSkill;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "employee_id")
	private Employee employeeId;

}
