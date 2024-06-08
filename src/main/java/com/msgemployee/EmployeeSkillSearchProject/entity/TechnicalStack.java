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
@Table(name = "technicalstack")
@Data
@JsonInclude(Include.NON_NULL)
public class TechnicalStack {

	@Id
	@Column(name = "id")
	private String techStackId;
	
	@Column(name = "technology")
	private String technology;

	@Column(name = "tech_proficiency_level")
	private String techProficiencyLevel;

	@Column(name = "programming_skill")
	private String programmingSkill;

	@Column(name = "prog_skill_proficiency_level")
	private String progSkillProficiencyLevel;

	@Column(name = "language")
	private String language;

	@Column(name = "lang_proficiency_level")
	private String langProficiencyLevel;

	@Column(name = "tools")
	private String tools;
	
	@Column(name = "tool_proficiency_level")
	private String toolProficiencyLevel;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "employee_id")
	private Employee employeeId;

}
