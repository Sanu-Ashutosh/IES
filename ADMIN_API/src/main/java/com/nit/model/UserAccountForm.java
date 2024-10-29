package com.nit.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserAccountForm {
	private Integer userId;
	private String fullName;
	private String emailId;
	private String countryCode;
	private String mobileNumber;
	private String gender;
	private LocalDate dob;
	private String SSN;
	private String roleName;
	private String activeSw;
	private Integer cretaedBy;
	private Integer updatedBy;
}
