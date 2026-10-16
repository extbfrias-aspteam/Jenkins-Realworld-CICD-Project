package topico.dto;

public record Respuesta(int scarchivodigital_id, boolean esIne, String codigoCliente, String clave,
		String tipodocumento, String participante_id, String codigoSistema, boolean esValidoCURP, String t_persona) {}
