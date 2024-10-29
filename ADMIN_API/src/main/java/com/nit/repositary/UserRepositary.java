package com.nit.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.nit.entity.UserEntity;

public interface UserRepositary extends JpaRepository<UserEntity, Integer> {
	 // Corrected the named parameter to match the method parameter
    @Modifying
    @Transactional
    @Query("update UserEntity set accStatus=:status where user_id=:userId")
    Integer updateAccStatus(Integer userId, String status);
    
    UserEntity findByuserEmail(String email);
}
