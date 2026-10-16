package com.asp.digitalizacion.model.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErroresEIYUResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8832617512432251917L;

	private String code;
	
	private String message;

	private String field;

}
