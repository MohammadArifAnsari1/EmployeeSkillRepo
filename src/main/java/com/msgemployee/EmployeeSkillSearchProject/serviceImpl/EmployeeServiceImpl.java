package com.msgemployee.EmployeeSkillSearchProject.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.msgemployee.EmployeeSkillSearchProject.entity.Certificates;
import com.msgemployee.EmployeeSkillSearchProject.entity.CoreCompetencies;
import com.msgemployee.EmployeeSkillSearchProject.entity.Education;
import com.msgemployee.EmployeeSkillSearchProject.entity.Employee;
import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeLanguages;
import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeProject;
import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeResponse;
import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeResponse.Skills;
import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeTechnologies;
import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeTools;
import com.msgemployee.EmployeeSkillSearchProject.entity.ProgrammingSkills;
import com.msgemployee.EmployeeSkillSearchProject.entity.TechnicalStack;
import com.msgemployee.EmployeeSkillSearchProject.repository.CertificateRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.CoreCompetenciesRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.EducationRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.EmployeeRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.ProjectRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.TechnicalStackRepository;
import com.msgemployee.EmployeeSkillSearchProject.service.EmployeeService;
import com.msgemployee.EmployeeSkillSearchProject.util.EmployeeFilterUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	private EmployeeRepository employeeRepo; 
	@Autowired
	private CertificateRepository certificateRepo;
	@Autowired
	private CoreCompetenciesRepository coreCompetencyRepo;
	@Autowired
	private EducationRepository educationRepo;
	@Autowired
	private ProjectRepository projectRepo;
	@Autowired
	private TechnicalStackRepository techStackRepo;
	@Autowired
	private EmployeeFilterUtil empFilterUtil;

	@Override
	public List<EmployeeResponse> getAllEmployees() {
		
		List<Employee> allEmployee = employeeRepo.findAllEmployee();
		List<EmployeeResponse> employeeResponseList = new ArrayList<>();
		log.info("All Employee record fetched from DB : {}",allEmployee);
		for (Employee employee : allEmployee) {
			EmployeeResponse employeeResponse = getOneEmplyeeDetails(employee);
			
			employeeResponseList.add(employeeResponse);
		}
		
		return employeeResponseList;
	}

	@Override
	public EmployeeResponse getEmployeeDetails(String empUserIdOrEmpName) {
		Employee employeeById = employeeRepo.findByEmpId(empUserIdOrEmpName);
		
		EmployeeResponse oneEmplyeeDetails = getOneEmplyeeDetails(employeeById);
		return oneEmplyeeDetails;
	}

	@Override
	public List<EmployeeResponse> getFilteredEmployeeDetails(EmployeeResponse filterRequest) {
//		String searchString = null;
//		
//		Employee empReqData = empFilterUtil.findSearchStringFromEmpData(filterRequest, searchString);
//		List<CoreCompetencies> coreCompReqData = empFilterUtil.checkCoreCompDataEmpty(filterRequest.getCoreCompetenciesList(), searchString);
//		List<Skills> skillReqData = empFilterUtil.checkSkillDataEmpty(filterRequest.getSkillList(), searchString);
//		
//		if(!ObjectUtils.isEmpty(searchString)) {
//			
//		}
		return null;
	}

	private EmployeeResponse getOneEmplyeeDetails(Employee employee) {
		List<Certificates> empCertDetails = findEmpCertDetails(employee.getEmployeeId());
		List<CoreCompetencies> empCoreCompetency = findEmpCoreCompetency(employee.getEmployeeId());
		List<Education> empEducation = findEmpEducation(employee.getEmployeeId());
		List<EmployeeProject> empProject = findEmpProject(employee.getEmployeeId());
		
		List<TechnicalStack> technicalStackByEmpId = techStackRepo.findTechnicalStackByEmpId(employee.getEmployeeId());
		List<EmployeeTechnologies> empTechList = findEmpTechnology(technicalStackByEmpId);
		List<ProgrammingSkills> empProgSkillList = findEmpProgSkills(technicalStackByEmpId);
		List<EmployeeLanguages> empLangList = findEmpLanguage(technicalStackByEmpId);
		List<EmployeeTools> empToolList = findEmpTool(technicalStackByEmpId);
		
		//Taking Employee Skill info from Programming Skill(TechnicalStack table) column
		List<Skills> empSkillsList = new ArrayList<>();
		for(ProgrammingSkills progSkill : empProgSkillList) {
			Skills skills = new Skills();
			skills.setSkills(progSkill.getProgrammingSkill());
			empSkillsList.add(skills);
		}
		
		EmployeeResponse employeeResponse = new EmployeeResponse();
		employeeResponse.setEmployeeId(employee.getEmployeeId());
		employeeResponse.setFirstName(employee.getFirstName());
		employeeResponse.setSurname(employee.getSurname());
		employeeResponse.setEmail(employee.getEmail());
		employeeResponse.setJobTitle(employee.getJobTitle());
		employeeResponse.setCommunityOfPractice(employee.getCommunityOfPractice());
		employeeResponse.setInternationalExperience(employee.getInternationExperience());
		employeeResponse.setProfilePic(employee.getProfilePicture());
		employeeResponse.setLineManager(employee.getLineManager());
		employeeResponse.setOverallExperience(employee.getOverallExperience());
		employeeResponse.setCertificateList(ObjectUtils.isEmpty(empCertDetails)?null:empCertDetails);
		employeeResponse.setCoreCompetenciesList(ObjectUtils.isEmpty(empCoreCompetency)?null:empCoreCompetency);
		employeeResponse.setEducationList(ObjectUtils.isEmpty(empEducation)?null:empEducation);
		employeeResponse.setProjectExpList(ObjectUtils.isEmpty(empProject)?null:empProject);
		employeeResponse.setTechnologyList(ObjectUtils.isEmpty(empTechList)?null:empTechList);
		employeeResponse.setProgSkillsListList(ObjectUtils.isEmpty(empProgSkillList)?null:empProgSkillList);
		employeeResponse.setLanguagesList(ObjectUtils.isEmpty(empLangList)?null:empLangList);
		employeeResponse.setToolsList(ObjectUtils.isEmpty(empToolList)?null:empToolList);
		employeeResponse.setSkillList(ObjectUtils.isEmpty(empSkillsList)?null:empSkillsList);
		return employeeResponse;
	}
	
	private List<Certificates> findEmpCertDetails(String employeeId){
		List<Object[]> certificateByEmpId = certificateRepo.findCertificateByEmpId(employeeId);
		List<Certificates> certificateList = new ArrayList<>();
		for (Object[] result : certificateByEmpId) {
			Certificates certificate = new Certificates();
			certificate.setCertificateName((String) result[0]);
			certificateList.add(certificate);
		}
		return certificateList;
	}
	
	private List<CoreCompetencies> findEmpCoreCompetency(String employeeId){
		List<Object[]> coreCompetenciesByEmpId = coreCompetencyRepo.findCoreCompetenciesByEmpId(employeeId);
		List<CoreCompetencies> coreCompetencyList = new ArrayList<>();
		
		for (Object[] result : coreCompetenciesByEmpId) {
			CoreCompetencies coreComp = new CoreCompetencies();
			coreComp.setFunctionalSkill((String) result[0]);
			coreComp.setTechnicalSkill((String) result[1]);
			coreCompetencyList.add(coreComp);
		}
		
		return coreCompetencyList;
	}

	private List<Education> findEmpEducation(String employeeId){
		List<Education> educationByEmpId = educationRepo.findEducationByEmpId(employeeId);
		List<Education> educationList = new ArrayList<>();
		
		for (Education result : educationByEmpId) {
			Education education = new Education();
			education.setEducationFrom(result.getEducationFrom());
			education.setEducationTo(result.getEducationTo());
			education.setDegree(result.getDegree());
			education.setField(result.getField());
			education.setUniversity(result.getUniversity());
			educationList.add(education);
		}
		
		return educationList;
	}

	private List<EmployeeProject> findEmpProject(String employeeId){
		List<EmployeeProject> projectByEmpId = projectRepo.findProjectByEmpId(employeeId);
		List<EmployeeProject> projectList = new ArrayList<>();
		
		for (EmployeeProject result : projectByEmpId) {
			EmployeeProject project = new EmployeeProject();
			project.setCompanyName(result.getProjectName());
			project.setStartDate(result.getStartDate());
			project.setEndDate(result.getEndDate());
			project.setCustomer(result.getCustomer());
			project.setProjectRole(result.getProjectRole());
			project.setLocation(result.getLocation());
			project.setProjectDiscription(result.getProjectDiscription());
			projectList.add(project);
		}
		
		return projectList;
	}
	
	private List<EmployeeTechnologies> findEmpTechnology(List<TechnicalStack> techStackResultList){
		
		List<EmployeeTechnologies> empTechList = new ArrayList<>();
		for(TechnicalStack result : techStackResultList) {
			if (!ObjectUtils.isEmpty(result.getTechnology()) && !ObjectUtils.isEmpty(result.getTechProficiencyLevel())) {
				EmployeeTechnologies technology = new EmployeeTechnologies();
				technology.setTechnology(result.getTechnology());
				technology.setTechProficiencyLevel(result.getTechProficiencyLevel());
				empTechList.add(technology);
			}
		}
		
		return empTechList;
	}

	private List<ProgrammingSkills> findEmpProgSkills(List<TechnicalStack> techStackResultList) {

		List<ProgrammingSkills> empProgSkillList = new ArrayList<>();
		for(TechnicalStack result : techStackResultList) {
			if (!ObjectUtils.isEmpty(result.getProgrammingSkill()) && !ObjectUtils.isEmpty(result.getProgSkillProficiencyLevel())) {
				ProgrammingSkills progSkill = new ProgrammingSkills();
				progSkill.setProgrammingSkill(result.getProgrammingSkill());
				progSkill.setProgSkillProficiencyLevel(result.getProgSkillProficiencyLevel());
				empProgSkillList.add(progSkill);
			}
		}
		
		return empProgSkillList;
	}

	private List<EmployeeLanguages> findEmpLanguage(List<TechnicalStack> techStackResultList) {

		List<EmployeeLanguages> empLangList = new ArrayList<>();
		for(TechnicalStack result : techStackResultList) {
			if (!ObjectUtils.isEmpty(result.getLanguage()) && !ObjectUtils.isEmpty(result.getLangProficiencyLevel())) {
				EmployeeLanguages language = new EmployeeLanguages();
				language.setLanguage(result.getLanguage());
				language.setLangProficiencyLevel(result.getLangProficiencyLevel());
				empLangList.add(language);
			}
		}
		
		return empLangList;
	}

	private List<EmployeeTools> findEmpTool(List<TechnicalStack> techStackResultList) {

		List<EmployeeTools> empToolList = new ArrayList<>();
		for(TechnicalStack result : techStackResultList) {
			if (!ObjectUtils.isEmpty(result.getTools()) && !ObjectUtils.isEmpty(result.getToolProficiencyLevel())) {
				EmployeeTools tools = new EmployeeTools();
				tools.setTools(result.getTools());
				tools.setToolProficiencyLevel(result.getToolProficiencyLevel());
				empToolList.add(tools);
			}
		}
		
		return empToolList;
	}

}
