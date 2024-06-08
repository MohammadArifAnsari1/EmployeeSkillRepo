package com.msgemployee.EmployeeSkillSearchProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msgemployee.EmployeeSkillSearchProject.entity.CoreCompetencies;

@Repository
public interface CoreCompetenciesRepository extends JpaRepository<CoreCompetencies, String> {
	
	@Query(value = "select cc.functional_skills, cc.technical_skills from employeeskillsearchreport.corecompetencies cc where cc.employee_id = ?1", nativeQuery = true)
	List<Object[]> findCoreCompetenciesByEmpId(String employeeId);

}
