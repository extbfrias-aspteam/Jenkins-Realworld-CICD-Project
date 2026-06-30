package functions.dto;

public record ValidaINEResponseOBJ(String mensaje, String claveMensaje, String codigoValidacion, String estatus,
		String claveElector, Integer numeroEmision, String cic, String ocr, String anioRegistro, String anioEmision,
		String vigencia) {}
