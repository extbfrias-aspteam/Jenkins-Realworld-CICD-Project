package com.asp.digitalizacion.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DigitalizacionException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7078976778583550173L;
	/**
	 * 
	 */
	
	
	private String mensaje;
	private HttpStatus httpStatus;
	
	public DigitalizacionException(String mensaje, HttpStatus httpStatus) {
		super(mensaje);
		this.mensaje = mensaje;
		this.httpStatus = httpStatus;
	}

}
