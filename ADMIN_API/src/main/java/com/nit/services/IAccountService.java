package com.nit.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nit.entity.UserEntity;
import com.nit.model.UnlockAccForm;
import com.nit.model.UserAccountForm;

public interface IAccountService {

	/*
	 * CREATE case worker/user account and UPDATE
	 */
	public boolean creatUserAcc(UserAccountForm user);

	/*
	 * Get all case worker account
	 */
	public Page<UserAccountForm> getAllUserAcc(Integer page);

	/*
	 * Get account by id all case worker account
	 */
	public UserAccountForm getUserAccByID(Integer userId);

	/*
	 * Delete all case worker account
	 */
	public boolean deleteUserAcc(Integer userId);

	/*
	 * Switch Active  case worker account
	 */
	public boolean switchAccountStatus(Integer userId, String status);
	
	/*
	 * Unlock  case worker account
	 */
//	public String unlockUserAccount(UnlockAccForm unlockAccForm) ;
	
	/*
	 * Delete all case worker account
	 */
	public boolean findEmail(String email);

}
