package com.nit.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nit.entity.PlanEntity;
import com.nit.model.PlanForm;

public interface IPlanService {

	/*
	 * CREATE and UPDATE plane
	 */
	public boolean createPlan(PlanForm plan);

	/*
	 * Get all plan usingpagination
	 */
	public Page<PlanForm> getAllPlan(Integer page);
	/*
	 * Get all plan 
	 */
	public List<PlanForm> getAllPlan() ;

	/*
	 * Get account by id all case worker account
	 */
	public PlanForm getPlanByID(Integer planId);

	/*
	 * Delete plan
	 */
	public Page<PlanForm> deletePlan(Integer planId , Integer page);
	/*
	 * Delete plan
	 */
	public PlanEntity findPlanByName(String planName);

	/*
	 * Switch Active all case worker account
	 */
	public boolean switchPlanStatus(Integer planId, String status);

}
