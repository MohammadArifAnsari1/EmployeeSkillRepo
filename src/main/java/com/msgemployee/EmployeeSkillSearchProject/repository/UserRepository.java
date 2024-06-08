package com.msgemployee.EmployeeSkillSearchProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msgemployee.EmployeeSkillSearchProject.entity.User;

public interface UserRepository extends JpaRepository<User, String>{

}
