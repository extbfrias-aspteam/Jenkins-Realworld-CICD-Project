package net.cero.data;

import java.io.Serializable;
import java.math.BigDecimal;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the solicitante database table.
 * 
 */
public class IneOcrReqOBJ implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id; // <archivo en base 64 con el frente de la credencial>
	private String idReverso; // <archivo en base 64 con el reverso de la credencial>
	
	/**
	 * 
	 */
	public IneOcrReqOBJ() {
		super();
	}

	/**
	 * @param id
	 * @param idReverso
	 */
	public IneOcrReqOBJ(String id, String idReverso) {
		super();
		this.id = id;
		this.idReverso = idReverso;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the idReverso
	 */
	public String getIdReverso() {
		return idReverso;
	}

	/**
	 * @param idReverso the idReverso to set
	 */
	public void setIdReverso(String idReverso) {
		this.idReverso = idReverso;
	}
	
}