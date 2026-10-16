package net.cero.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;


@RequiredArgsConstructor
@Data
public class TarjetaOBJ implements Serializable{
	private static final long serialVersionUID = 1L;
	private String pan;
	private String estatus;
	private String tipoTarjeta;
	private String cuenta;
	private String claveEmpresa;
	private String nombreCorto;
	
}
