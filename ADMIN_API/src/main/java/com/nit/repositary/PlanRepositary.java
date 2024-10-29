package com.nit.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nit.entity.PlanEntity;


public interface PlanRepositary extends JpaRepository<PlanEntity, Integer> {
	
	@Query("update PlanEntity set planStatus=:status where planId=:plan_Id")
    public Integer updateAccStatus(Integer plan_Id, String status);
	
	public PlanEntity findByplanName(String PlanName);
}
