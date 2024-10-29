package com.nit.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PlanForm {
	private String planName;
	private String planCategory;
	private LocalDate planStartDate;
	private LocalDate planEndDate;

}
