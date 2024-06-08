package com.msgemployee.EmployeeSkillSearchProject.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.msgemployee.EmployeeSkillSearchProject.entity.CoreCompetencies;
import com.msgemployee.EmployeeSkillSearchProject.entity.Employee;
import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeResponse;
import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeResponse.Skills;

@Component
public class EmployeeFilterUtil {

	public Employee findSearchStringFromEmpData(EmployeeResponse filterRequest, String searchString) {
		Employee empReqData = new Employee();
		if(!ObjectUtils.isEmpty(filterRequest.getEmployeeId())) {
			empReqData.setEmployeeId(filterRequest.getEmployeeId());
		}
		if(!ObjectUtils.isEmpty(filterRequest.getFirstName())) {
			empReqData.setFirstName(filterRequest.getFirstName());
		}
		if(!ObjectUtils.isEmpty(filterRequest.getSurname())) {
			empReqData.setSurname(filterRequest.getSurname());
		}
		if(!ObjectUtils.isEmpty(filterRequest.getEmail())) {
			empReqData.setEmail(filterRequest.getEmail());
		}
		if(!ObjectUtils.isEmpty(filterRequest.getJobTitle())) {
			empReqData.setJobTitle(filterRequest.getJobTitle());
		}
		if(!ObjectUtils.isEmpty(filterRequest.getCommunityOfPractice())) {
			empReqData.setCommunityOfPractice(filterRequest.getCommunityOfPractice());
		}
		if(!ObjectUtils.isEmpty(filterRequest.getInternationalExperience())) {
			empReqData.setInternationExperience(filterRequest.getInternationalExperience());
		}
		if(!ObjectUtils.isEmpty(filterRequest.getLineManager())) {
			empReqData.setLineManager(filterRequest.getLineManager());
		}
		if(!ObjectUtils.isEmpty(filterRequest.getOverallExperience())) {
			empReqData.setOverallExperience(filterRequest.getOverallExperience());
		}
		if(!ObjectUtils.isEmpty(filterRequest.getLevel())) {
			empReqData.setLevel(filterRequest.getLevel());
		}
		if(!ObjectUtils.isEmpty(filterRequest.getBench())) {
			empReqData.setBench(filterRequest.getBench());
		}
		return empReqData;
	}

	public List<CoreCompetencies> checkCoreCompDataEmpty(List<CoreCompetencies> coreCompFilterList, String searchString) {
		List<CoreCompetencies> coreCompReqDataList = new ArrayList<>();
		if(!ObjectUtils.isEmpty(coreCompFilterList)) {
			for (CoreCompetencies coreCompetencies : coreCompFilterList) {
				CoreCompetencies coreCompReqData = new CoreCompetencies();
				if(!ObjectUtils.isEmpty(coreCompetencies.getFunctionalSkill())){
					coreCompReqData.setFunctionalSkill(coreCompetencies.getFunctionalSkill());
				}
				if(!ObjectUtils.isEmpty(coreCompetencies.getTechnicalSkill())){
					coreCompReqData.setTechnicalSkill(coreCompetencies.getTechnicalSkill());
				}
				if(!ObjectUtils.isEmpty(coreCompetencies.getEmployeeId())){
					coreCompReqData.setEmployeeId(coreCompetencies.getEmployeeId());
				}
				coreCompReqDataList.add(coreCompReqData);
			}
		}
		return coreCompReqDataList;
	}

	public List<Skills> checkSkillDataEmpty(List<Skills> skillFilterList, String searchString) {
		List<Skills> skillReqDataList = new ArrayList<>();
		if(!ObjectUtils.isEmpty(skillFilterList)) {
			for (Skills skill : skillFilterList) {
				Skills skillReqData = new Skills();
				if(!ObjectUtils.isEmpty(skill.getSkills())){
					skillReqData.setSkills(skill.getSkills());
				}
				if(!ObjectUtils.isEmpty(skill.getEmployeeId())){
					skillReqData.setEmployeeId(skill.getEmployeeId());
				}
				skillReqDataList.add(skillReqData);
			}
		}
		return skillReqDataList;
	}
}
