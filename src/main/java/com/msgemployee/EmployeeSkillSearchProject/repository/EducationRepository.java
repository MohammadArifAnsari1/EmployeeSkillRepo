package com.msgemployee.EmployeeSkillSearchProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msgemployee.EmployeeSkillSearchProject.entity.Education;

@Repository
public interface EducationRepository extends JpaRepository<Education, String>{
	
	@Query(value = "select * from employeeskillsearchreport.education edu where edu.employee_id = ?1", nativeQuery = true)
	List<Education> findEducationByEmpId(String employeeId);

}
