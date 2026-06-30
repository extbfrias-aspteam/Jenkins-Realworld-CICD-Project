package com.asp.digitalizacion.model.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DigitalizacionAPIResponse<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 698648625741329010L;
	
	private String codigo;

	private String mensaje;

	private T contenido;
	
	

}
