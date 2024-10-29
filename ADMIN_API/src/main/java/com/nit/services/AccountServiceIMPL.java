package com.nit.services;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nit.entity.UserEntity;
import com.nit.model.UnlockAccForm;
import com.nit.model.UserAccountForm;
import com.nit.repositary.UserRepositary;
import com.nit.utils.EmailUtils;
import com.nit.utils.PasswordUtils;



@Service
public class AccountServiceIMPL implements IAccountService {

	@Autowired
	private UserRepositary userRepo;
	
	@Autowired
	private EmailUtils emailSender;

	@Override
	public boolean creatUserAcc(UserAccountForm user) {
		// TODO Auto-generated method stub
		UserEntity data = new UserEntity();
		data.setFullName(user.getFullName());
		data.setUserEmail(user.getEmailId());
		String tempPassword = PasswordUtils.generateTempPassword(6);
		data.setUserPassword(tempPassword);
		data.setUserCountryCode(user.getCountryCode());
		data.setUserPhoneNumber(user.getMobileNumber());
		data.setUserGender(user.getGender());
		data.setUserDOB(user.getDob());
		data.setUserSSN(user.getSSN());
		data.setActiveSW("ACTIVE");
		data.setAccStatus("LOCKED");
		data.setRoleName(user.getRoleName());
		data.setCreatedBy(user.getCretaedBy());
		data.setUpdatedBy(user.getUpdatedBy());
		
		String subject = "User Registration";
		String body = readEmailBody("REG_EMAIL_BODY.txt", data);
		boolean sendEmail = emailSender.sendEmail(data.getUserEmail(),subject,body);
		UserEntity save = userRepo.save(data);
		System.out.println(save);
		
		return save != null&& sendEmail==true;
	}

	@Override
	public Page<UserAccountForm> getAllUserAcc(Integer page) {
		// TODO Auto-generated method stub
		 Page<UserEntity> allEntity = userRepo.findAll(PageRequest.of(page, 5));
		System.out.println(allEntity);
		List<UserAccountForm> collect = allEntity.getContent().stream().map(accEntity->{
			UserAccountForm form=new UserAccountForm();
			BeanUtils.copyProperties(accEntity, form);
			return form;
		}).collect(Collectors.toList());
		// Return a new Page object with the mapped content
	    return new PageImpl<>(collect, allEntity.getPageable(), allEntity.getTotalElements());
	}

	@Override
	public UserAccountForm getUserAccByID(Integer userId) {
		// Fetch the user entity by ID
		Optional<UserEntity> optionalUser = userRepo.findById(userId);
		UserAccountForm form = new UserAccountForm();
		if (optionalUser.isPresent()) {

			// Copy properties from UserEntity to UserAccountForm
			BeanUtils.copyProperties(optionalUser.get(), form); // Use the correct BeanUtils import

		}
		return form; // Return the populated form

	}

	@Override
	public boolean deleteUserAcc(Integer userId) {
		// TODO Auto-generated method stub
		if (userRepo.findById(userId) != null) {

			userRepo.deleteById(userId);
			return true;
		}

		return false;
	}

	@Override
	public boolean switchAccountStatus(Integer userId, String status) {
		// TODO Auto-generated method stub
		Integer count = userRepo.updateAccStatus(userId, status);
		return count > 0 ? true : false;
	}

//	@Override
//	public String unlockUserAccount(UnlockAccForm unlockAccForm) {
//
//		UserEntity entity = userRepo.findByuserEmail(unlockAccForm.getEmail());
//		if (entity != null) {
//			
//			if(entity.getAccStatus().equals("UNLOCKED")) return "Your Account is already Unlock. Please SignIn";
//			entity.setUserPassword(unlockAccForm.getNewPwd());
//			entity.setAccStatus("UNLOCKED");
//
//			userRepo.save(entity);
//			return "Account Unlock successful";
//		} else
//			return "Account Not found!!";
//	}
	
	private String readEmailBody(String filename, UserEntity user) {
		StringBuilder sb = new StringBuilder();
		try (Stream<String> lines = Files.lines(Paths.get(filename))) {
			lines.forEach(line -> {
				line = line.replace("${FNAME}", user.getFullName());
				line = line.replace("${TEMP_PWD}", user.getUserPassword());
				line = line.replace("${EMAIL}", user.getUserEmail());
				sb.append(line);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public boolean findEmail(String email) {
		// TODO Auto-generated method stub
		UserEntity byuserEmail = userRepo.findByuserEmail(email);
		return byuserEmail!=null? true : false;
	}


}
