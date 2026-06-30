package functions.dto;

/**
 * The persistent class for the solicitante database table.
 * 
 */
public record IneOcrReqOBJ(String id, String idReverso){
	
	public IneOcrReqOBJ withIneOcrReqOBJId(String id) {
		return new IneOcrReqOBJ(id, idReverso());
	}
	
	public IneOcrReqOBJ withIneOcrReqOBJIdRev(String idReverso) {
		return new IneOcrReqOBJ(id(), idReverso);
	}
	
}