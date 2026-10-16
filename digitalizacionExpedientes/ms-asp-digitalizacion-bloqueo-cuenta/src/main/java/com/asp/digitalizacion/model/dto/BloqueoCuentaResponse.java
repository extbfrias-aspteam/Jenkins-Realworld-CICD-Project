package com.asp.digitalizacion.model.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BloqueoCuentaResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4767644812235016832L;
	
	private Integer cuentasActualizadas;
	
	private List<ErrorBloqueoCuentaDTO> errores;

}
