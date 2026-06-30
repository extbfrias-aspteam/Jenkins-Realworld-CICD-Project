package com.asp.eiyu.api.admdocument.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentoRegistroResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigo;
	private Date fechaHoraCarga;
	private String uuid;

}
