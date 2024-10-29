package com.nit.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "IES_USERS")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Integer user_id;

	@Column(name = "FULL_NAME")
	private String fullName; // FULL_NAME

	@Column(name = "USER_EMAIL", unique = true)
	private String userEmail; // USER_EMAIL

	@Column(name = "USER_PWD")
	private String userPassword; // USER_PWD

	@Column(name = "USER_CCODE")
	private String userCountryCode; // USER_PHNO
	
	@Column(name = "USER_PHNO")
	private String userPhoneNumber; // USER_PHNO

	@Column(name = "USER_GENDER")
	private String userGender; // USER_GENDER

	@Column(name = "USER_DOB")
	private LocalDate userDOB; // USER_DOB

	@Column(name = "USER_SSN")
	private String userSSN; // USER_SSN

	// Active switch
	@Column(name = "ACTIVE_SW")
	private String activeSW = "ACTIVE"; // ACTIVE_SW

	@Column(name = "ACC_STATUS")
	private String accStatus = "LOCKED"; // ACC_STATUS

	@Column(name = "ROLE_NAME")
	private String roleName; // ROLE_ID
	
	@Column(name = "CREATE_DATE", updatable = false)
	@CreationTimestamp
	private LocalDate createDate; // CREATE_DATE

	@Column(name = "UPDATE_DATE")
	@UpdateTimestamp
	private LocalDate updateDate; // UPDATE_DATE
	
	@Column(name="CREATED_BY")
	private Integer createdBy; // CREATED_BY

	@Column(name="UPDATED_BY")
	private Integer updatedBy; // UPDATED_BY
	
	@OneToMany(mappedBy = "createdBy",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PlanEntity> plans;
	@OneToMany(mappedBy = "updatedBy",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<PlanEntity> updatePlans;
}
