package com.nit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "IES_PLANS")
public class PlanEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAN_ID")
    private Integer planId; // PLAN_ID

    @Column(name = "PLAN_NAME")
    private String planName; // PLAN_NAME
    
    @Column(name = "PLAN_START_DATE")
    private LocalDate planStartDate; // PLAN_START_DATE

    @Column(name = "PLAN_END_DATE")
    private LocalDate planEndDate; // PLAN_END_DATE

    @Column(name = "PLAN_CATEGORY")
    private String planCategory; // PLAN_CATEGORY

    @Column(name = "ACTIVE_SW")
    private String activeSW; // ACTIVE_SW
    
    @Column(name = "PLAN_STATUS")
    private String planStatus; // ACTIVE_SW

    @Column(name = "CREATED_DATE", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate; // CREATED_DATE

    @Column(name = "UPDATED_DATE")
    @UpdateTimestamp
    private LocalDateTime updatedDate; // UPDATED_DATE
    
    @ManyToOne
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "USER_ID")
    private UserEntity createdBy; // CREATED_BY

    @ManyToOne
    @JoinColumn(name = "UPDATED_BY", referencedColumnName = "USER_ID")
    private UserEntity updatedBy; // UPDATED_BY
}
