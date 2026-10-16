package com.asp.digitalizacion.model.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3686979943051355009L;
	
	private String username;
    private String password;

}
