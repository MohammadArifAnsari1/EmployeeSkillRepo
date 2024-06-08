package com.msgemployee.EmployeeSkillSearchProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msgemployee.EmployeeSkillSearchProject.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
	
	@Query(value = "select emp.employee_id, emp.first_name, emp.surname, emp.job_title, emp.internation_experience, emp.community_of_practice from employeeskillsearchreport.employee emp where emp.employee_id = ?1", nativeQuery = true)
	public List<Object[]> findEmployeeByEmpId(String employeeId);
	
	@Query(value = "select * from employeeskillsearchreport.employee", nativeQuery = true)
	public List<Employee> findAllEmployee();

	@Query(value = "select * from employeeskillsearchreport.employee emp where emp.employee_id = ?1", nativeQuery = true)
	public Employee findByEmpId(String employeeId);

	@Query(value = "select emp.email from employeeskillsearchreport.employee emp where emp.employee_id = ?1", nativeQuery = true)
	public String findEmailByEmpId(String employeeId);
	
//	@Query(value = "", nativeQuery = true)
//	public List<Employee> findEmployeeByString(String searchString);
}
