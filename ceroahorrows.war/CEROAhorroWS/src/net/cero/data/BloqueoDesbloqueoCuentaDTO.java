package net.cero.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cero.seguridad.utilidades.HeaderWS;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloqueoDesbloqueoCuentaDTO {
	@Valid
	@NotNull(message = "El header es requerido")
	private HeaderWS header;
	@Valid
	@NotNull(message = "El numero de la cuenta es requerido")
	@NotEmpty(message = "El numero de la cuenta es requerido")
	private String cuentaAsp;
}
