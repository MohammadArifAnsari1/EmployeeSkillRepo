package com.msgemployee.EmployeeSkillSearchProject.service;

import com.msgemployee.EmployeeSkillSearchProject.entity.User;

public interface LoginService {

	public String register(User user);

	public String loginUser(User user) throws Exception;

	public String passwordUpdate(User user) throws Exception;

	public String authentication(String userId) throws Exception;

	public String findById(String userId);

}
