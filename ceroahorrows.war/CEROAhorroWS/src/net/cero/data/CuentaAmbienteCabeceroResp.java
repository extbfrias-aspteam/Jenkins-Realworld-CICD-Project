package net.cero.data;

import java.util.ArrayList;
import java.util.List;

public class CuentaAmbienteCabeceroResp {
	
	private String numero;
	private String nombreCompleto;
	private String direccion;
	private String curp;
	private String telefono;
	private String ocupacion;
	private String fechaNacimiento;
	private String correoElectronico;
	private String lugarNacimiento;
	private String sexo;
	private String RFC;
	private String nivelCuenta;
	private Double saldoCuenta;
	private Beneficiario beneficiario;
	
	private List<MedioDisposicion> mediosDisposicion;

	public CuentaAmbienteCabeceroResp() {
		this.nombreCompleto = "";
		this.direccion = "";
		this.curp = "";
		this.telefono = "";
		this.ocupacion = "";
		this.fechaNacimiento = "";
		this.correoElectronico = "";
		this.lugarNacimiento = "";
		this.sexo = "";
		this.RFC = "";
		this.nivelCuenta = "";
		this.saldoCuenta = 0d;
		this.beneficiario = new Beneficiario();
		this.mediosDisposicion = new ArrayList<>();
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}
	
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	
	public String getDireccion() {
		return direccion;
	}
	
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String getCurp() {
		return curp;
	}
	
	public void setCurp(String curp) {
		this.curp = curp;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getOcupacion() {
		return ocupacion;
	}
	
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
	
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	
	public String getLugarNacimiento() {
		return lugarNacimiento;
	}
	
	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public String getRFC() {
		return RFC;
	}
	
	public void setRFC(String rFC) {
		RFC = rFC;
	}
	
	public String getNivelCuenta() {
		return nivelCuenta;
	}
	
	public void setNivelCuenta(String nivelCuenta) {
		this.nivelCuenta = nivelCuenta;
	}
	
	public Double getSaldoCuenta() {
		return saldoCuenta;
	}
	
	public void setSaldoCuenta(Double saldoCuenta) {
		this.saldoCuenta = saldoCuenta;
	}

	public List<MedioDisposicion> getMediosDisposición() {
		return mediosDisposicion;
	}
	
	public void agregarMedio(MedioDisposicion medio) {
		this.mediosDisposicion.add(medio);
	}

	public List<MedioDisposicion> getMediosDisposicion() {
		return mediosDisposicion;
	}

	public void setMediosDisposicion(List<MedioDisposicion> mediosDisposicion) {
		this.mediosDisposicion = mediosDisposicion;
	}

	public void setMediosDisposición(List<MedioDisposicion> mediosDisposición) {
		this.mediosDisposicion = mediosDisposición;
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

	public static class Beneficiario{
		private String nombre;
		private String calle;
		private String numExt;
		private String numInt;
		private String municipio;
		private String CP;
		private String colonia;
		private String ciudad;
		private String telefono;
		private String entidadfederativa;
		private String descripcionParentesco;
		private String fechaNacimiento;
		private String parentesco;
		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getFechaNacimiento() {
			return fechaNacimiento;
		}

		public void setFechaNacimiento(String fechaNacimiento) {
			this.fechaNacimiento = fechaNacimiento;
		}

		public String getCalle() {
			return calle;
		}

		public void setCalle(String calle) {
			this.calle = calle;
		}

		public String getNumExt() {
			return numExt;
		}

		public void setNumExt(String numExt) {
			this.numExt = numExt;
		}

		public String getNumInt() {
			return numInt;
		}

		public void setNumInt(String numInt) {
			this.numInt = numInt;
		}

		public String getMunicipio() {
			return municipio;
		}

		public void setMunicipio(String municipio) {
			this.municipio = municipio;
		}

		public String getCP() {
			return CP;
		}

		public void setCP(String CP) {
			this.CP = CP;
		}

		public String getColonia() {
			return colonia;
		}

		public void setColonia(String colonia) {
			this.colonia = colonia;
		}

		public String getCiudad() {
			return ciudad;
		}

		public void setCiudad(String ciudad) {
			this.ciudad = ciudad;
		}

		public String getTelefono() {
			return telefono;
		}

		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}

		public String getEntidadfederativa() {
			return entidadfederativa;
		}

		public void setEntidadfederativa(String entidadfederativa) {
			this.entidadfederativa = entidadfederativa;
		}

		public String getDescripcionParentesco() {
			return descripcionParentesco;
		}

		public void setDescripcionParentesco(String descripcionParentesco) {
			this.descripcionParentesco = descripcionParentesco;
		}

		public String getParentesco() {
			return parentesco;
		}

		public void setParentesco(String parentesco) {
			this.parentesco = parentesco;
		}
	}
}
