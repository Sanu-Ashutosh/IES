package com.nit.exception;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {
	private String exCode;

	private String exDesc;

	private LocalDateTime exDate;

}
