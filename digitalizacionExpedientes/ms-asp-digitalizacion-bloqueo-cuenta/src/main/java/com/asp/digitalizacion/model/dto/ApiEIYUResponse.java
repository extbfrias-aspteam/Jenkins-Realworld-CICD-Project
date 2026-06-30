package com.asp.digitalizacion.model.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiEIYUResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7868156809398615658L;
	
	
	private Boolean success;
	private String message;
	private Object data;
	private List<ErroresEIYUResponse> errors;

}
