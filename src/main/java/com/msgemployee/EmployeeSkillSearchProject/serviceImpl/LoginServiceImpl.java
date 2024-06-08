package com.msgemployee.EmployeeSkillSearchProject.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msgemployee.EmployeeSkillSearchProject.entity.User;
import com.msgemployee.EmployeeSkillSearchProject.exception.EmployeeNotFoundException;
import com.msgemployee.EmployeeSkillSearchProject.repository.EmployeeRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.UserRepository;
import com.msgemployee.EmployeeSkillSearchProject.service.EmailSenderService;
import com.msgemployee.EmployeeSkillSearchProject.service.LoginService;
import com.msgemployee.EmployeeSkillSearchProject.util.LoginUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private LoginUtil loginUtil;
	@Autowired
	private EmployeeRepository emailRepo;
	@Autowired
	private EmailSenderService emailSenderService;

	@Override
	public String register(User user) {
		String encryptedPassword = loginUtil.encrypt(user.getPassword());
		user.setPassword(encryptedPassword);
		try {
			User savedUser = userRepo.save(user);
			return "User registered successfull with Id: "+savedUser.getUserId();
		}catch (Exception e) {
			return "Error registering user: "+e.getMessage();
		}
	}

//	@Override
//	public String loginUser(User user) throws Exception {
//		String userId = user.getUserId();
//		Optional<User> userById = userRepo.findById(userId);
//		if(userById.get().getRole().equals("admin")) {
//			if(!loginUtil.encrypt(user.getPassword()).equals(userById.get().getPassword()))
//				throw new Exception("Invalid Credential");
//			else 
//				return "Successfull";
//		}else {
//			throw new Exception("You are admin");
//		}
//	}
	
	@Override
	public String loginUser(User user) throws Exception {
		String userId = user.getUserId();
		Optional<User> userdata = userRepo.findById(userId);
		if (!userId.matches("^MSG-\\d{3}$")) {
			throw new Exception(
					"Invalid User id format. Kindly provide the ID in the format of 'MSG-000'");
		}
		if (userdata.get().getRole().equals("admin")) {
//			if (!loginUtil.encrypt(user.getPassword()).equals(userdata.get().getPassword())) {
			if (!(user.getPassword()).equals(userdata.get().getPassword())) {

				throw new Exception("Invalid Crediantial");
			} else {
				return "Sucessful";
			}
		} else {
			throw new Exception("Your not admin");
		}
	}

	@Override
	public String passwordUpdate(User user) throws Exception {
		User mail = new User();
		mail.setUserId(user.getUserId());
		mail.setPassword(loginUtil.encrypt(user.getNewPassword()));
		
		Optional<User> userById = userRepo.findById(mail.getUserId());
		mail.setRole(userById.get().getRole());
		
		String oldPassword = user.getOldPassword();
		
		String encryptedPassword = loginUtil.encrypt(oldPassword);
		if(!encryptedPassword.equals(userById.get().getPassword())) {
			throw new Exception("Please provide correct Old Password");
		}else {
			userRepo.save(mail);
			return "Password Updated Successfully";
		}
	}

	@Override
	public String authentication(String userId) throws Exception {
		Optional<User> userAuth = userRepo.findById(userId);
		if(!userAuth.get().getRole().equals("admin"))
			throw new Exception("You are not admin");
		else
			return "Successfull";
	}

	@Override
	public String findById(String userId) {
		String email = emailRepo.findEmailByEmpId(userId);
		String password = userRepo.findById(userId).get().getPassword();
		String decryptedPassword = loginUtil.decrypt(password);
		String firstName = emailRepo.findById(userId).get().getFirstName();
		log.info("decryptedPassword: {}",decryptedPassword);
		
		String message = "Password Recovery code for Employee Skill Report application";
		String credential = "Hi "+firstName+", \n\nBelow are the credentials "
				+ "\nNeeded to be used in resetting the password for Employee Skill Report application"
				+ "\n\n"
				+ "User_Id: "+userId +"\n"
				+ "Password: "+decryptedPassword
				+ "\n\n\n\n"
				+ "Thanks"
				+ "\n"
				+ "ESR-Admin Portal";
		
		emailSenderService.sendSimpleEmail(email, message, credential);
		return email;
	}

}
