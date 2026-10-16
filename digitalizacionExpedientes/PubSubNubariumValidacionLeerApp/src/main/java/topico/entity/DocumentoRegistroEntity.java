package topico.entity;

import java.io.Serializable;

/**
 * Banco ASP Project: eiyu Class: DocumentoRegistroEntity.java
 *
 * Description:Mapeo de la tabla Documento registro en el cual contiene la
 * informacion recibida de los documentos
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Sep 3, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Sep 3, 2023 Herwin: Creacion de la clase
 *
 * @category Entity
 *
 */
public class DocumentoRegistroEntity implements Serializable {

	private static final long serialVersionUID = 1014578557961295355L;

	private String tipoDocumento;

	private String codigo;

	private int estatus;

	private String verificacion;

	/**
	 * 
	 */
	public DocumentoRegistroEntity() {
		super();
		tipoDocumento = "";
		codigo= "";
		estatus= 0;
		verificacion= "";
	}
	
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public String getVerificacion() {
		return verificacion;
	}

	public void setVerificacion(String verificacion) {
		this.verificacion = verificacion;
	}

	@Override
	public String toString() {
		return "DocumentoRegistroEntity [tipoDocumento=" + tipoDocumento + ", codigo=" + codigo + ", estatus=" + estatus
				+ ", verificacion=" + verificacion + "]";
	}

}
