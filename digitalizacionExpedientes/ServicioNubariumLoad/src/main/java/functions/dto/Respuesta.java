package functions.dto;

public record Respuesta(int scarchivodigital_id, boolean esIne, String codigoCliente, String clave,
		String tipodocumento, String participante_id, String codigoSistema, boolean esValidoCURP, String t_persona,
		boolean coreCompleto, String mensajeCore) {
	
	public Respuesta withParticipante_id(String participante_id) {
		return new Respuesta(scarchivodigital_id(), esIne(), codigoCliente(), clave(), 
				tipodocumento(), participante_id, codigoSistema(), esValidoCURP(), t_persona(), coreCompleto(), mensajeCore());
	}
	
	public Respuesta withTPersona(String tpersona) {
		return new Respuesta(scarchivodigital_id(), esIne(), codigoCliente(), clave(), 
				tipodocumento(), participante_id(), codigoSistema(), esValidoCURP(), tpersona, coreCompleto(), mensajeCore());
	}
	
	public Respuesta withEsValidoCURP(boolean esValidoCURP) {
		return new Respuesta(scarchivodigital_id(), esIne(), codigoCliente(), clave(), 
				tipodocumento(), participante_id(), codigoSistema(), esValidoCURP, t_persona(), coreCompleto(), mensajeCore());
	}

	public Respuesta withCoreCompleto(boolean coreCompleto, String mensajeCore) {
		return new Respuesta(scarchivodigital_id(), esIne(), codigoCliente(), clave(),
				tipodocumento(), participante_id(), codigoSistema(), esValidoCURP(), t_persona(), coreCompleto,
				mensajeCore);
	}
}
