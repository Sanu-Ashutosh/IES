package com.nit.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nit.entity.UserEntity;
import com.nit.model.DasboardCards;
import com.nit.model.ForgotPWDForm;
import com.nit.model.LoginForm;
import com.nit.model.UnlockAccForm;

public interface IUserAuthService {

	/*
	 * Login check user
	 */
	public UserDetails login(LoginForm user);

	/*
	 * recover password with sending mail to his account
	 */
	public boolean recoverPass(ForgotPWDForm user);
	
	
	/*
	 * Active account with sending mail to his account
	 */
	public String activeAccount(UnlockAccForm user);

	/*
	 * get DasboardCard details
	 */
	public DasboardCards getProfilData();
//	/*
//	 * get user details by email 
//	 */
	 public UserDetails loadUserByUsername(String username);
	/*
	 * for security
	 */
//	public UserDetails loadUserByUserName(String username);

}
