package com.msgemployee.EmployeeSkillSearchProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msgemployee.EmployeeSkillSearchProject.entity.TechnicalStack;

@Repository
public interface TechnicalStackRepository extends JpaRepository<TechnicalStack, String> {

	@Query(value = "select * from employeeskillsearchreport.technicalstack ts where ts.employee_id = ?1", nativeQuery = true)
	List<TechnicalStack> findTechnicalStackByEmpId(String employeeId);

}
