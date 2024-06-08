package com.msgemployee.EmployeeSkillSearchProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeProject;

@Repository
public interface ProjectRepository extends JpaRepository<EmployeeProject, String>{

	@Query(value = "select * from employeeskillsearchreport.project p where p.employee_id = ?1", nativeQuery = true)
	List<EmployeeProject> findProjectByEmpId(String employeeId);
}
