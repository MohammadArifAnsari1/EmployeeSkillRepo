package com.msgemployee.EmployeeSkillSearchProject.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class EmployeeResponse {
	
	private String employeeId;
	private String firstName;
	private String surname;
	private String email;
	private String jobTitle;
	private String communityOfPractice;
	private String internationalExperience;
	private String profilePic;
	private String lineManager;
	private String overallExperience;
	private String level;
	private String bench;
	private List<CoreCompetencies> coreCompetenciesList;
	private List<Skills> skillList;
	private List<Certificates> certificateList;
	private List<EmployeeLanguages> languagesList;
	private List<ProgrammingSkills> progSkillsListList;
	private List<EmployeeTools> toolsList;
	private List<EmployeeTechnologies> technologyList;
	private List<Education> educationList;
	private List<EmployeeProject> projectExpList;
	
	public EmployeeResponse() {}
	
	public EmployeeResponse(String employeeId, String firstName, String surname, String email, String jobTitle, 
			String communityOfPractice, String internationalExperience, String profilePic, String lineManager, 
			String overallExperience, String level, String bench, List<CoreCompetencies> coreCompetenciesList, List<Skills> skillList, 
			List<Certificates> certificateList, List<EmployeeLanguages> languagesList, List<ProgrammingSkills> progSkillsListList, 
			List<EmployeeTools> toolsList, List<EmployeeTechnologies> technologyList, List<Education> educationList, List<EmployeeProject> projectExpList) {
		
		super();
		this.employeeId = employeeId; 
		this.firstName = firstName; 
		this.surname = surname; 
		this.email = email; 
		this.jobTitle = jobTitle; 
		this.communityOfPractice = communityOfPractice; 
		this.internationalExperience = internationalExperience; 
		this.profilePic = profilePic; 
		this.lineManager = lineManager; 
		this.overallExperience = overallExperience; 
		this.level = level; 
		this.bench = bench; 
		this.coreCompetenciesList = coreCompetenciesList;
		this.skillList = skillList;
		this.certificateList = certificateList;
		this.languagesList = languagesList;
		this.progSkillsListList = progSkillsListList;
		this.toolsList = toolsList;
		this.technologyList = technologyList;
		this.educationList = educationList;
		this.projectExpList = projectExpList;
	}
	
	@Override
	public String toString() {
		return "EmployeeResponse ["
				+ "employeeId = " + employeeId
				+ ", firstName = " + firstName
				+ ", surname = " + surname
				+ ", email = " + email
				+ ", jobTitle = " + jobTitle
				+ ", communityOfPractice = " + communityOfPractice
				+ ", internationalExperience = " + internationalExperience
				+ ", profilePic = " + profilePic
				+ ", lineManager = " + lineManager
				+ ", overallExperience = " + overallExperience
				+ ", level = " + level
				+ ", bench = " + bench
				+ ", coreCompetenciesList = " + coreCompetenciesList
				+ ", skillList = " + skillList
				+ ", certificateList = " + certificateList
				+ ", languagesList = " + languagesList
				+ ", progSkillsListList = " + progSkillsListList
				+ ", toolsList = " + toolsList
				+ ", technologyList = " + technologyList
				+ ", educationList = " + educationList
				+ ", projectExpList = " + projectExpList
				+ "]";
	}
	

	@Data
	@JsonInclude(Include.NON_NULL)
	public static class Skills {
		private String skillId;
		private String skills;
		private Employee employeeId;
	}
	
}
