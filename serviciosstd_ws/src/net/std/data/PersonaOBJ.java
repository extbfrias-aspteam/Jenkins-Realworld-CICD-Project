package net.std.data;

import java.io.Serializable;

public class PersonaOBJ implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nombre_cuenta;
	private String tipo_persona_cuenta;
	private String pr_apellido_cuenta;
	private String sg_apellido_cuenta;
	private String denominacion_cuenta;
	private String rfc_cuenta;
	private String curp_cuenta;
	private String calle_principal_cuenta;
	private String calle_secundaria_cuenta;
	private String calle_secundaria2_cuenta;
	private String no_interior_cuenta;
	private String no_exterior_cuenta;
	private String coloniaId_cuenta;
	private String colonia_cuenta;
	private String cp_cuenta;
	private String ciudad_cuenta;
	//private String municipio_cuenta;
	//private String estadoId_cuenta;
	private String celular_cuenta;
	private String correo_cuenta;
	private String genero_cuenta;
	private String tipo_identId_cuenta;
	private String num_ident_cuenta;
	private String fecha_nac_cuenta;
	private String entidad_nacId_cuenta;
	private String pais_nacId_cuenta;
	private String pais_nac_cuenta;
	private String nacionalidadId_cuenta;
	private String nacionalidad_cuenta;
	private String serie_firma_elect_cuenta;
	private String ocupacionId_cuenta;
	private String ocupacion_cuenta;
	private String telefono_cuenta;
	private String geolocalizacion_cuenta;
	private String unidad_negocio_cuenta;
	
	private String monto_max_aho_cuenta;
	private String ingresos_cuenta;
	private String nivel_cuenta;

	public PersonaOBJ() {

	}

	public String getNombre_cuenta() {
		return nombre_cuenta;
	}

	public void setNombre_cuenta(String nombre_cuenta) {
		this.nombre_cuenta = nombre_cuenta;
	}

	public String getTipo_persona_cuenta() {
		return tipo_persona_cuenta;
	}

	public void setTipo_persona_cuenta(String tipo_persona_cuenta) {
		this.tipo_persona_cuenta = tipo_persona_cuenta;
	}

	public String getPr_apellido_cuenta() {
		return pr_apellido_cuenta;
	}

	public void setPr_apellido_cuenta(String pr_apellido_cuenta) {
		this.pr_apellido_cuenta = pr_apellido_cuenta;
	}

	public String getSg_apellido_cuenta() {
		return sg_apellido_cuenta;
	}

	public void setSg_apellido_cuenta(String sg_apellido_cuenta) {
		this.sg_apellido_cuenta = sg_apellido_cuenta;
	}

	public String getRfc_cuenta() {
		return rfc_cuenta;
	}

	public void setRfc_cuenta(String rfc_cuenta) {
		this.rfc_cuenta = rfc_cuenta;
	}

	public String getCurp_cuenta() {
		return curp_cuenta;
	}

	public void setCurp_cuenta(String curp_cuenta) {
		this.curp_cuenta = curp_cuenta;
	}

	public String getNo_interior_cuenta() {
		return no_interior_cuenta;
	}

	public void setNo_interior_cuenta(String no_interior_cuenta) {
		this.no_interior_cuenta = no_interior_cuenta;
	}

	public String getNo_exterior_cuenta() {
		return no_exterior_cuenta;
	}

	public void setNo_exterior_cuenta(String no_exterior_cuenta) {
		this.no_exterior_cuenta = no_exterior_cuenta;
	}
	
	public String getCelular_cuenta() {
		return celular_cuenta;
	}

	public void setCelular_cuenta(String celular_cuenta) {
		this.celular_cuenta = celular_cuenta;
	}

	public String getGenero_cuenta() {
		return genero_cuenta;
	}

	public void setGenero_cuenta(String genero_cuenta) {
		this.genero_cuenta = genero_cuenta;
	}

	public String getNum_ident_cuenta() {
		return num_ident_cuenta;
	}

	public void setNum_ident_cuenta(String num_ident_cuenta) {
		this.num_ident_cuenta = num_ident_cuenta;
	}

	public String getFecha_nac_cuenta() {
		return fecha_nac_cuenta;
	}

	public void setFecha_nac_cuenta(String fecha_nac_cuenta) {
		this.fecha_nac_cuenta = fecha_nac_cuenta;
	}

	public String getSerie_firma_elect_cuenta() {
		return serie_firma_elect_cuenta;
	}

	public void setSerie_firma_elect_cuenta(String serie_firma_elect_cuenta) {
		this.serie_firma_elect_cuenta = serie_firma_elect_cuenta;
	}

	public String getTelefono_cuenta() {
		return telefono_cuenta;
	}

	public void setTelefono_cuenta(String telefono_cuenta) {
		this.telefono_cuenta = telefono_cuenta;
	}

	public String getGeolocalizacion_cuenta() {
		return geolocalizacion_cuenta;
	}

	public void setGeolocalizacion_cuenta(String geolocalizacion_cuenta) {
		this.geolocalizacion_cuenta = geolocalizacion_cuenta;
	}

	public String getUnidad_negocio_cuenta() {
		return unidad_negocio_cuenta;
	}

	public void setUnidad_negocio_cuenta(String unidad_negocio_cuenta) {
		this.unidad_negocio_cuenta = unidad_negocio_cuenta;
	}

	public String getMonto_max_aho_cuenta() {
		return monto_max_aho_cuenta;
	}

	public void setMonto_max_aho_cuenta(String monto_max_aho_cuenta) {
		this.monto_max_aho_cuenta = monto_max_aho_cuenta;
	}

	public String getIngresos_cuenta() {
		return ingresos_cuenta;
	}

	public void setIngresos_cuenta(String ingresos_cuenta) {
		this.ingresos_cuenta = ingresos_cuenta;
	}
	
	public String getDenominacion_cuenta() {
		return denominacion_cuenta;
	}

	public void setDenominacion_cuenta(String denominacion_cuenta) {
		this.denominacion_cuenta = denominacion_cuenta;
	}

	public String getCorreo_cuenta() {
		return correo_cuenta;
	}

	public void setCorreo_cuenta(String correo_cuenta) {
		this.correo_cuenta = correo_cuenta;
	}

	public String getCalle_principal_cuenta() {
		return calle_principal_cuenta;
	}

	public void setCalle_principal_cuenta(String calle_principal_cuenta) {
		this.calle_principal_cuenta = calle_principal_cuenta;
	}

	public String getCalle_secundaria_cuenta() {
		return calle_secundaria_cuenta;
	}

	public void setCalle_secundaria_cuenta(String calle_secundaria_cuenta) {
		this.calle_secundaria_cuenta = calle_secundaria_cuenta;
	}

	public String getCalle_secundaria2_cuenta() {
		return calle_secundaria2_cuenta;
	}

	public void setCalle_secundaria2_cuenta(String calle_secundaria2_cuenta) {
		this.calle_secundaria2_cuenta = calle_secundaria2_cuenta;
	}

	public String getTipo_identId_cuenta() {
		return tipo_identId_cuenta;
	}

	public void setTipo_identId_cuenta(String tipo_identId_cuenta) {
		this.tipo_identId_cuenta = tipo_identId_cuenta;
	}

	public String getEntidad_nacId_cuenta() {
		return entidad_nacId_cuenta;
	}

	public void setEntidad_nacId_cuenta(String entidad_nacId_cuenta) {
		this.entidad_nacId_cuenta = entidad_nacId_cuenta;
	}

	public String getCiudad_cuenta() {
		return ciudad_cuenta;
	}

	public void setCiudad_cuenta(String ciudad_cuenta) {
		this.ciudad_cuenta = ciudad_cuenta;
	}

	public String getCp_cuenta() {
		return cp_cuenta;
	}

	public void setCp_cuenta(String cp_cuenta) {
		this.cp_cuenta = cp_cuenta;
	}

	public String getNivel_cuenta() {
		return nivel_cuenta;
	}

	public void setNivel_cuenta(String nivel_cuenta) {
		this.nivel_cuenta = nivel_cuenta;
	}

	public String getColonia_cuenta() {
		return colonia_cuenta;
	}

	public void setColonia_cuenta(String colonia_cuenta) {
		this.colonia_cuenta = colonia_cuenta;
	}

	public String getPais_nac_cuenta() {
		return pais_nac_cuenta;
	}

	public void setPais_nac_cuenta(String pais_nac_cuenta) {
		this.pais_nac_cuenta = pais_nac_cuenta;
	}

	public String getNacionalidad_cuenta() {
		return nacionalidad_cuenta;
	}

	public void setNacionalidad_cuenta(String nacionalidad_cuenta) {
		this.nacionalidad_cuenta = nacionalidad_cuenta;
	}

	public String getOcupacion_cuenta() {
		return ocupacion_cuenta;
	}

	public void setOcupacion_cuenta(String ocupacion_cuenta) {
		this.ocupacion_cuenta = ocupacion_cuenta;
	}

	public String getColoniaId_cuenta() {
		return coloniaId_cuenta;
	}

	public void setColoniaId_cuenta(String coloniaId_cuenta) {
		this.coloniaId_cuenta = coloniaId_cuenta;
	}

	public String getPais_nacId_cuenta() {
		return pais_nacId_cuenta;
	}

	public void setPais_nacId_cuenta(String pais_nacId_cuenta) {
		this.pais_nacId_cuenta = pais_nacId_cuenta;
	}

	public String getNacionalidadId_cuenta() {
		return nacionalidadId_cuenta;
	}

	public void setNacionalidadId_cuenta(String nacionalidadId_cuenta) {
		this.nacionalidadId_cuenta = nacionalidadId_cuenta;
	}

	public String getOcupacionId_cuenta() {
		return ocupacionId_cuenta;
	}

	public void setOcupacionId_cuenta(String ocupacionId_cuenta) {
		this.ocupacionId_cuenta = ocupacionId_cuenta;
	}
}
