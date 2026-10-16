package com.asp.digitalizacion.model.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BloqueoCuentaEIYURequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2766391096528768432L;
	
	@NotBlank(message = "La CLABE es obligatoria")
	@Size(min = 18, max = 18, message = "La CLABE debe tener exactamente 18 caracteres")
	@Pattern(regexp = "\\d{18}", message = "La CLABE debe contener solo dígitos")
	private String clabe;

}
