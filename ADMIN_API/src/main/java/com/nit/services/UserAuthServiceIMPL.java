	package com.nit.services;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nit.constant.AppConstants;
import com.nit.entity.UserEntity;
import com.nit.model.DasboardCards;
import com.nit.model.ForgotPWDForm;
import com.nit.model.LoginForm;
import com.nit.model.UnlockAccForm;
import com.nit.repositary.PlanRepositary;
import com.nit.repositary.UserRepositary;
import com.nit.utils.EmailUtils;

@Service
public class UserAuthServiceIMPL implements IUserAuthService,UserDetailsService {
	@Autowired
	private UserRepositary userRepo;

	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private PlanRepositary planRepo;
	
	
	

	@Override
	public UserDetails login(LoginForm user) {
		// TODO Auto-generated method stub
		 UserDetails userDetails = loadUserByUsername(user.getUserName());
		 if(userDetails==null) throw new BadCredentialsException("Invalid User Name!!") ;
		 return userDetails;
	}
	
	
    public UserDetails loadUserByUsername(String username)  {
        UserEntity user = userRepo.findByuserEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<GrantedAuthority> authorities=new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(
            user.getUserEmail(), 
            user.getUserPassword(), 
            user.getAccStatus().equals("UNLOCKED"), // isEnabled
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            authorities // authorities
        );
    }

	@Override
	public boolean recoverPass(ForgotPWDForm user) {
		// TODO Auto-generated method stub
		System.out.println("User "+user);
		UserEntity byuserEmail = userRepo.findByuserEmail(user.getEmail());
		System.out.println("byuserEmail"+byuserEmail);
		  String token = UUID.randomUUID().toString();
	        // Store the token with the user's information (this could be in the database or a cache)
//	        storeToken(byuserEmail.getUser_id(), token);
//	        
//	        // Create a recovery link
//	        String recoveryLink = AppConstants.BASE_URL + "/reset-password?token=" + token;

		if (byuserEmail != null && user.getSsn().equals(byuserEmail.getUserSSN())) {
			String subject = AppConstants.RECOVER_SUB;
			String body = readEmailBody(AppConstants.PWD_BODY_FILE, byuserEmail);
			System.out.println("User "+user);
			return emailUtils.sendEmail(byuserEmail.getUserEmail(),subject,body);
		}

		return false;
	}

	@Override
	public String activeAccount(UnlockAccForm user) {
		UserEntity entity = userRepo.findByuserEmail(user.getEmail());
		if (entity != null) {
			
			if(entity.getAccStatus().equals("UNLOCKED")) return "Your Account is already Unlock. Please SignIn";
			entity.setUserPassword(user.getNewPwd());
			entity.setAccStatus("UNLOCKED");

			userRepo.save(entity);
			return "Account Unlock successful";
		} else
			return "Account Not found!!";
	}

	@Override
	public DasboardCards getProfilData() {
		long count = planRepo.count();
		return new DasboardCards();
	}
	
	

	private String readEmailBody(String filename, UserEntity user) {
		StringBuilder sb = new StringBuilder();
		try (Stream<String> lines = Files.lines(Paths.get(filename))) {
			lines.forEach(line -> {
				line = line.replace(AppConstants.FNAME, user.getFullName());
				line = line.replace(AppConstants.PWD, user.getUserPassword());
				line = line.replace(AppConstants.EMAIL, user.getUserEmail());
				sb.append(line);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	

}
