package com.asp.digitalizacion.model.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorBloqueoCuentaDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3714282828299361366L;
	
	private String mensaje;
	
	private String cuenta;

}
