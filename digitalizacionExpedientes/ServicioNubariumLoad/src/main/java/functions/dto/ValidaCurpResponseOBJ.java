package functions.dto;

public record ValidaCurpResponseOBJ (String estatus, String codigoValidacion, String curp, String nombre,
		String apellidoPaterno, String apellidoMaterno, String sexo, String fechaNacimiento, String paisNacimiento,
		String estadoNacimiento, Integer docProbatorio, DatosDocProbatorio datosDocProbatorio, String estatusCurp,
		String codigoMensaje){}
