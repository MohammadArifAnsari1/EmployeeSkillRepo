package com.msgemployee.EmployeeSkillSearchProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msgemployee.EmployeeSkillSearchProject.entity.Certificates;

@Repository
public interface CertificateRepository extends JpaRepository<Certificates, String>{
	
	@Query(value = "select cert.certificate_name from employeeskillsearchreport.certificates cert where cert.employee_id = ?1", nativeQuery = true)
	List<Object[]> findCertificateByEmpId(String employeeId);

}
