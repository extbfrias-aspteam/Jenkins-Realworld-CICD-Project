package net.cero.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class EmpresaOBJ implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String claveEmpresa;
	private String nombre;
}
