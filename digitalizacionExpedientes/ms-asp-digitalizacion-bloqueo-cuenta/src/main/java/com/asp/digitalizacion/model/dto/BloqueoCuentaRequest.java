package com.asp.digitalizacion.model.dto;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BloqueoCuentaRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6344863611190209090L;
	
	@NotBlank(message = "El tipo de bloqueo es obligatorio")
	@Pattern(regexp = "^(A|O|I|NONE)$", message = "El tipo de bloqueo debe ser A, O, I o NONE")
	private String tipoBloqueo;
	
//	@NotNull(message = "El id del participante es obligatorio")
//	private Integer pblu;
	
	@NotEmpty(message = "Se requiere al menos una cuenta para bloquear")
	private List<String> cuentas;

}
