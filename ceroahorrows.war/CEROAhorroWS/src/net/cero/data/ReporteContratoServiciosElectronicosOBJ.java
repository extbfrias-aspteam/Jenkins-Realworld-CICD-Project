package net.cero.data;

/**
 * Clase que contiene los datos que requiere el reporte Contrato de Servicios
 * Electrónicos para ser generado.
 * 
 * @author Gabriel Rodríguez
 * @since 4.1 15/04/19
 * @version 1.0
 *
 */
public class ReporteContratoServiciosElectronicosOBJ {
	
	private String cuenta;
	
	private String personaId;
	
	private String nombreCliente;
	
	private String celular;

	private Long empresaid;

	private int sucursalid;
	
	public ReporteContratoServiciosElectronicosOBJ() {
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getPersonaId() {
		return personaId;
	}

	public void setPersonaId(String personaId) {
		this.personaId = personaId;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Long getEmpresaid() {
		return empresaid;
	}

	public void setEmpresaid(Long empresaid) {
		this.empresaid = empresaid;
	}

	public int getSucursalid() {
		return sucursalid;
	}

	public void setSucursalid(int sucursalid) {
		this.sucursalid = sucursalid;
	}
	
}
