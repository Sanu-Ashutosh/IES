package com.nit.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AuthenticationResponse {
	 private final String token;
	 private String message;
	 private LocalDateTime createDate;

	    public AuthenticationResponse(String token,String message,LocalDateTime createDate) {
	        this.token = token;
	        this.message=message;
	        this.createDate=createDate;
	    }
}
