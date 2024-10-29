package com.nit.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nit.entity.PlanEntity;
import com.nit.model.PlanForm;
import com.nit.repositary.PlanRepositary;



@Service
public class PlanServiceIMPL implements IPlanService {
	
	@Autowired
	private PlanRepositary planRepo;

	@Override
	public boolean createPlan(PlanForm plan) {
		// TODO Auto-generated method stub
		PlanEntity data=new PlanEntity();
		BeanUtils.copyProperties(plan, data);
		data.setActiveSW("ACTIVE");
		PlanEntity save = planRepo.save(data); System.out.println(save);
		return save!=null;
	}

	@Override
	public List<PlanForm> getAllPlan() {
		// TODO Auto-generated method stub
		List<PlanEntity> all = planRepo.findAll();
		List<PlanForm> list= new ArrayList<>();
		for(PlanEntity planEntity:all) {
			PlanForm form=new PlanForm();
			BeanUtils.copyProperties(planEntity, form);
			list.add(form);
		}
		return list;
	}
	@Override
	public Page<PlanForm> getAllPlan(Integer page) {
	    // Fetch the paginated entities
	    Page<PlanEntity> allEntities = planRepo.findAll(PageRequest.of(page, 4));

	    // Map the entities to the forms
	    List<PlanForm> planForms = allEntities.getContent().stream()
	        .map(planEntity -> {
	            PlanForm form = new PlanForm();
	            BeanUtils.copyProperties(planEntity, form);
	            return form;
	        })
	        .collect(Collectors.toList());

	    // Return a new Page object with the mapped content
	    return new PageImpl<>(planForms, allEntities.getPageable(), allEntities.getTotalElements());
	}

	@Override
	public PlanForm getPlanByID(Integer planId) {
		// TODO Auto-generated method stub
		Optional<PlanEntity> byId = planRepo.findById(planId);
		PlanForm form=new PlanForm();
		if(byId.isPresent()) {
			BeanUtils.copyProperties(byId, form);
		}
		return form;
	}

	@Override
	public Page<PlanForm> deletePlan(Integer planId , Integer page) {
		// TODO Auto-generated method stub
		Page<PlanForm> allPlan = null  ;
		if(planRepo.findById(planId)!=null)
		{
			planRepo.deleteById(planId);
			 allPlan = getAllPlan(page);	
		}
		return allPlan;
	}

	@Override
	public boolean switchPlanStatus(Integer planId, String status) {
		// TODO Auto-generated method stub
		Integer count = planRepo.updateAccStatus(planId, status);	
		return count>0? true: false;
	}

	@Override
	public PlanEntity findPlanByName(String planName) {
		// TODO Auto-generated method stub
		PlanEntity byplanName = planRepo.findByplanName(planName);
		return byplanName;
	}
	

}
