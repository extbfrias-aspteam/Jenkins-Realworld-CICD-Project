package net.cero.data;

public class CatalogoMovimientosOBJ {

	private String claveMovimiento;
	private String descripcionMovimiento;
	private String tipoMovimiento;
	
	
	public CatalogoMovimientosOBJ(String claveMovimiento, String descripcionMovimiento, String tipoMovimiento) {
		this.claveMovimiento = claveMovimiento;
		this.descripcionMovimiento = descripcionMovimiento;
		this.tipoMovimiento = tipoMovimiento;
	}
	public CatalogoMovimientosOBJ() {
	}
	public String getClaveMovimiento() {
		return claveMovimiento;
	}
	public void setClaveMovimiento(String claveMovimiento) {
		this.claveMovimiento = claveMovimiento;
	}
	public String getDescripcionMovimiento() {
		return descripcionMovimiento;
	}
	public void setDescripcionMovimiento(String descripcionMovimiento) {
		this.descripcionMovimiento = descripcionMovimiento;
	}
	public String getTipoMovimiento() {
		return tipoMovimiento;
	}
	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}
	
	
}
