package net.cero.data;

import java.util.ArrayList;
import java.util.List;

public class DashboardCuentasOBJ {
	private String mes;
	private List<EstatusCuentaDashboard> estatusCuenta;

	
	public DashboardCuentasOBJ() {
		estatusCuenta = new ArrayList<>();
	}


	public String getMes() {
		return mes;
	}


	public void setMes(String mes) {
		this.mes = mes;
	}


	public List<EstatusCuentaDashboard> getEstatusCuenta() {
		return estatusCuenta;
	}


	public void setEstatusCuenta(List<EstatusCuentaDashboard> estatusCuenta) {
		this.estatusCuenta = estatusCuenta;
	}


	public class EstatusCuentaDashboard {
		private String estatus;
		private int cantidad;
		public String getEstatus() {
			return estatus;
		}
		public void setEstatus(String estatus) {
			this.estatus = estatus;
		}
		public int getCantidad() {
			return cantidad;
		}
		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}
	}
}
