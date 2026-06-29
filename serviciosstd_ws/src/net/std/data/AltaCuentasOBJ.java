package net.std.data;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class AltaCuentasOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private String identificador;
	private String cuenta_clabe_eje;
	private String productoAhorro;
	private String tipoPersona;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String nombreCompleto;
	private String rfc;
	private String curp;
	private String lugarNacimiento;
	private String nacionalidadId;
	private String estadoCivilId;
	private String telefono;
	private String celular;
	private String correo;
	private String ocupacionId;
	private String fechaNacimiento;
	private String sexo;
	private String gradoEstudiosId;
	private String tipoIdentificacionId;
	private String identificacion;
	private String idUbicacion1;
	private String descripcionUbicacion1;
	private String idUbicacion2;
	private String descripcionUbicacion2;
	private String idUbicacion3;
	private String descripcionUbicacion3;
	private String referencia;
	private String idUbicacion4;
	private String descripcionUbicacion4;
	private String idUbicacion5;
	private String descripcionUbicacion5;
	private String idColonia;
	private String domicilioCompleto;
	private String ingreso_mensual;
	private String monto_maximo_ahorro;
	private String puesto;
	private String tipo1;
	private String clave1;
	private String descripcion1;
	private String tipo2;
	private String clave2;
	private String descripcion2;
	private String tipo3;
	private String clave3;
	private String descripcion3;
	private String tipo4;
	private String clave4;
	private String descripcion4;
	private String tipo5;
	private String clave5;
	private String descripcion5;

	public AltaCuentasOBJ(){

	}

	public AltaCuentasOBJ(String identificador, String cuenta_clabe_eje, String productoAhorro, String tipoPersona, String nombre, 
			String apellidoPaterno, String apellidoMaterno, String nombreCompleto, String rfc, String curp, 
			String lugarNacimiento, String nacionalidadId, String estadoCivilId, String telefono, String celular, 
			String correo, String ocupacionId, String fechaNacimiento, String sexo, String gradoEstudiosId, 
			String tipoIdentificacionId, String identificacion, String idUbicacion1, String descripcionUbicacion1, 
			String idUbicacion2, String descripcionUbicacion2, String idUbicacion3, String descripcionUbicacion3, 
			String referencia, String idUbicacion4, String descripcionUbicacion4, String idUbicacion5, 
			String descripcionUbicacion5, String idColonia, String domicilioCompleto, String ingreso_mensual, 
			String monto_maximo_ahorro, String puesto, String tipo1, String clave1, String descripcion1, String tipo2, 
			String clave2, String descripcion2, String tipo3, String clave3, String descripcion3, String tipo4, 
			String clave4, String descripcion4, String tipo5, String clave5, String descripcion5){

		this.identificador = identificador;
		this.cuenta_clabe_eje = cuenta_clabe_eje;
		this.productoAhorro = productoAhorro;
		this.tipoPersona = tipoPersona;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.nombreCompleto = nombreCompleto;
		this.rfc = rfc;
		this.curp = curp;
		this.lugarNacimiento = lugarNacimiento;
		this.nacionalidadId = nacionalidadId;
		this.estadoCivilId = estadoCivilId;
		this.telefono = telefono;
		this.celular = celular;
		this.correo = correo;
		this.ocupacionId = ocupacionId;
		this.fechaNacimiento = fechaNacimiento;
		this.sexo = sexo;
		this.gradoEstudiosId = gradoEstudiosId;
		this.tipoIdentificacionId = tipoIdentificacionId;
		this.identificacion = identificacion;
		this.idUbicacion1 = idUbicacion1;
		this.descripcionUbicacion1 = descripcionUbicacion1;
		this.idUbicacion2 = idUbicacion2;
		this.descripcionUbicacion2 = descripcionUbicacion2;
		this.idUbicacion3 = idUbicacion3;
		this.descripcionUbicacion3 = descripcionUbicacion3;
		this.referencia = referencia;
		this.idUbicacion4 = idUbicacion4;
		this.descripcionUbicacion4 = descripcionUbicacion4;
		this.idUbicacion5 = idUbicacion5;
		this.descripcionUbicacion5 = descripcionUbicacion5;
		this.idColonia = idColonia;
		this.domicilioCompleto = domicilioCompleto;
		this.ingreso_mensual = ingreso_mensual;
		this.monto_maximo_ahorro = monto_maximo_ahorro;
		this.puesto = puesto;
		this.tipo1 = tipo1;
		this.clave1 = clave1;
		this.descripcion1 = descripcion1;
		this.tipo2 = tipo2;
		this.clave2 = clave2;
		this.descripcion2 = descripcion2;
		this.tipo3 = tipo3;
		this.clave3 = clave3;
		this.descripcion3 = descripcion3;
		this.tipo4 = tipo4;
		this.clave4 = clave4;
		this.descripcion4 = descripcion4;
		this.tipo5 = tipo5;
		this.clave5 = clave5;
		this.descripcion5 = descripcion5;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getCuenta_clabe_eje() {
		return cuenta_clabe_eje;
	}

	public void setCuenta_clabe_eje(String cuenta_clabe_eje) {
		this.cuenta_clabe_eje = cuenta_clabe_eje;
	}

	public String getProductoAhorro() {
		return productoAhorro;
	}

	public void setProductoAhorro(String productoAhorro) {
		this.productoAhorro = productoAhorro;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getLugarNacimiento() {
		return lugarNacimiento;
	}

	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}

	public String getNacionalidadId() {
		return nacionalidadId;
	}

	public void setNacionalidadId(String nacionalidadId) {
		this.nacionalidadId = nacionalidadId;
	}

	public String getEstadoCivilId() {
		return estadoCivilId;
	}

	public void setEstadoCivilId(String estadoCivilId) {
		this.estadoCivilId = estadoCivilId;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getOcupacionId() {
		return ocupacionId;
	}

	public void setOcupacionId(String ocupacionId) {
		this.ocupacionId = ocupacionId;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getGradoEstudiosId() {
		return gradoEstudiosId;
	}

	public void setGradoEstudiosId(String gradoEstudiosId) {
		this.gradoEstudiosId = gradoEstudiosId;
	}

	public String getTipoIdentificacionId() {
		return tipoIdentificacionId;
	}

	public void setTipoIdentificacionId(String tipoIdentificacionId) {
		this.tipoIdentificacionId = tipoIdentificacionId;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getIdUbicacion1() {
		return idUbicacion1;
	}

	public void setIdUbicacion1(String idUbicacion1) {
		this.idUbicacion1 = idUbicacion1;
	}

	public String getDescripcionUbicacion1() {
		return descripcionUbicacion1;
	}

	public void setDescripcionUbicacion1(String descripcionUbicacion1) {
		this.descripcionUbicacion1 = descripcionUbicacion1;
	}

	public String getIdUbicacion2() {
		return idUbicacion2;
	}

	public void setIdUbicacion2(String idUbicacion2) {
		this.idUbicacion2 = idUbicacion2;
	}

	public String getDescripcionUbicacion2() {
		return descripcionUbicacion2;
	}

	public void setDescripcionUbicacion2(String descripcionUbicacion2) {
		this.descripcionUbicacion2 = descripcionUbicacion2;
	}

	public String getIdUbicacion3() {
		return idUbicacion3;
	}

	public void setIdUbicacion3(String idUbicacion3) {
		this.idUbicacion3 = idUbicacion3;
	}

	public String getDescripcionUbicacion3() {
		return descripcionUbicacion3;
	}

	public void setDescripcionUbicacion3(String descripcionUbicacion3) {
		this.descripcionUbicacion3 = descripcionUbicacion3;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getIdUbicacion4() {
		return idUbicacion4;
	}

	public void setIdUbicacion4(String idUbicacion4) {
		this.idUbicacion4 = idUbicacion4;
	}

	public String getDescripcionUbicacion4() {
		return descripcionUbicacion4;
	}

	public void setDescripcionUbicacion4(String descripcionUbicacion4) {
		this.descripcionUbicacion4 = descripcionUbicacion4;
	}

	public String getIdUbicacion5() {
		return idUbicacion5;
	}

	public void setIdUbicacion5(String idUbicacion5) {
		this.idUbicacion5 = idUbicacion5;
	}

	public String getDescripcionUbicacion5() {
		return descripcionUbicacion5;
	}

	public void setDescripcionUbicacion5(String descripcionUbicacion5) {
		this.descripcionUbicacion5 = descripcionUbicacion5;
	}

	public String getIdColonia() {
		return idColonia;
	}

	public void setIdColonia(String idColonia) {
		this.idColonia = idColonia;
	}

	public String getDomicilioCompleto() {
		return domicilioCompleto;
	}

	public void setDomicilioCompleto(String domicilioCompleto) {
		this.domicilioCompleto = domicilioCompleto;
	}

	public String getIngreso_mensual() {
		return ingreso_mensual;
	}

	public void setIngreso_mensual(String ingreso_mensual) {
		this.ingreso_mensual = ingreso_mensual;
	}

	public String getMonto_maximo_ahorro() {
		return monto_maximo_ahorro;
	}

	public void setMonto_maximo_ahorro(String monto_maximo_ahorro) {
		this.monto_maximo_ahorro = monto_maximo_ahorro;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getTipo1() {
		return tipo1;
	}

	public void setTipo1(String tipo1) {
		this.tipo1 = tipo1;
	}

	public String getClave1() {
		return clave1;
	}

	public void setClave1(String clave1) {
		this.clave1 = clave1;
	}

	public String getDescripcion1() {
		return descripcion1;
	}

	public void setDescripcion1(String descripcion1) {
		this.descripcion1 = descripcion1;
	}

	public String getTipo2() {
		return tipo2;
	}

	public void setTipo2(String tipo2) {
		this.tipo2 = tipo2;
	}

	public String getClave2() {
		return clave2;
	}

	public void setClave2(String clave2) {
		this.clave2 = clave2;
	}

	public String getDescripcion2() {
		return descripcion2;
	}

	public void setDescripcion2(String descripcion2) {
		this.descripcion2 = descripcion2;
	}

	public String getTipo3() {
		return tipo3;
	}

	public void setTipo3(String tipo3) {
		this.tipo3 = tipo3;
	}

	public String getClave3() {
		return clave3;
	}

	public void setClave3(String clave3) {
		this.clave3 = clave3;
	}

	public String getDescripcion3() {
		return descripcion3;
	}

	public void setDescripcion3(String descripcion3) {
		this.descripcion3 = descripcion3;
	}

	public String getTipo4() {
		return tipo4;
	}

	public void setTipo4(String tipo4) {
		this.tipo4 = tipo4;
	}

	public String getClave4() {
		return clave4;
	}

	public void setClave4(String clave4) {
		this.clave4 = clave4;
	}

	public String getDescripcion4() {
		return descripcion4;
	}

	public void setDescripcion4(String descripcion4) {
		this.descripcion4 = descripcion4;
	}

	public String getTipo5() {
		return tipo5;
	}

	public void setTipo5(String tipo5) {
		this.tipo5 = tipo5;
	}

	public String getClave5() {
		return clave5;
	}

	public void setClave5(String clave5) {
		this.clave5 = clave5;
	}

	public String getDescripcion5() {
		return descripcion5;
	}

	public void setDescripcion5(String descripcion5) {
		this.descripcion5 = descripcion5;
	}

	public String getGSon(){
		String gson = "{ " +
				"\"identificador\" : " + this.identificador + "," +
				"\"cuenta_clabe_eje\" : " +  this.cuenta_clabe_eje + "," +
				"\"cuenta\" : " + 
				"{ " +
				"\"productoAhorro\" : " +  this.productoAhorro + 
				"}, " +
				"\"solicitante\" : " + 
				"{ " +
				"\"tipoPersona\" : " +  this.tipoPersona + "," +
				"\"nombre\" : " +  this.nombre + "," +
				"\"apellidoPaterno\" : " +  this.apellidoPaterno + "," +
				"\"apellidoMaterno\" : " +  this.apellidoMaterno + "," +
				"\"nombreCompleto\" : " +  this.nombreCompleto + "," +
				"\"rfc\" : " +  this.rfc + "," +
				"\"curp\" : " +  this.curp + "," +
				"\"lugarNacimiento\" : " +  this.lugarNacimiento + "," +
				"\"nacionalidadId\" : " + this.nacionalidadId + "," +
				"\"estadoCivilId\" : " +  this.estadoCivilId + "," +
				"\"telefono\" : " +  this.telefono + "," +
				"\"celular\" : " +  this.celular + "," +
				"\"correo\" : " +  this.correo + "," +
				"\"ocupacionId\" : " + this.ocupacionId + "," +
				"\"fechaNacimiento\" : " +  this.fechaNacimiento + "," +
				"\"sexo\" : " +  this.sexo + "," +
				"\"gradoEstudiosId\" : " + this.gradoEstudiosId + "," +
				"\"tipoIdentificacionId\" : " + this.tipoIdentificacionId + "," +
				"\"identificacion\" : " +  this.identificacion +
				"}, " +
				"\"domicilio\" : " + 
				"{ " +
				"\"idUbicacion1\" : " + this.idUbicacion1 + "," +
				"\"descripcionUbicacion1\" : " +  this.descripcionUbicacion1 + "," +
				"\"idUbicacion2\" : " + this.idUbicacion2 + "," +
				"\"descripcionUbicacion2\" : " +  this.descripcionUbicacion2 + "," +
				"\"idUbicacion3\" : " + this.idUbicacion3 + "," +
				"\"descripcionUbicacion3\" : " +  this.descripcionUbicacion3 + "," +
				"\"referencia\" : " +  this.referencia + "," +
				"\"idUbicacion4\" : " + this.idUbicacion4 + "," +
				"\"descripcionUbicacion4\" : " +  this.descripcionUbicacion4 + "," +
				"\"idUbicacion5\" : " + this.idUbicacion5 + "," +
				"\"descripcionUbicacion5\" : " +  this.descripcionUbicacion5 + "," +
				"\"idColonia\" : " +  this.idColonia + "," +
				"\"domicilioCompleto\" : " +  this.domicilioCompleto +
				"}, " +
				"\"pld\" : " + 
				"{ " +
				"\"ingreso_mensual\" : " + this.ingreso_mensual + "," +
				"\"monto_maximo_ahorro\" : " + this.monto_maximo_ahorro + "," +
				"\"puesto\" : " +  this.puesto +
				"}, " +
				"\"lstMatriz\" : " + 
				"[ " +
				"{\"tipo\" : " +  this.tipo1 + "," + "\"clave\" : " +  this.clave1 + "," + "\"descripcion\" : " +  this.descripcion1 + "}," +
				"{\"tipo\" : " +  this.tipo2 + "," + "\"clave\" : " +  this.clave2 + "," + "\"descripcion\" : " +  this.descripcion2 + "}," +
				"{\"tipo\" : " +  this.tipo3 + "," + "\"clave\" : " +  this.clave3 + "," + "\"descripcion\" : " +  this.descripcion3 + "}," +
				"{\"tipo\" : " +  this.tipo4 + "," + "\"clave\" : " +  this.clave4 + "," + "\"descripcion\" : " +  this.descripcion4 + "}," +
				"{\"tipo\" : " +  this.tipo5 + "," + "\"clave\" : " +  this.clave5 + "," + "\"descripcion\" : " +  this.descripcion5 + "}" +
				"] " +
				"} ";

		return gson;
	}

	public LinkedHashMap <String, String> getMap(){
		LinkedHashMap <String, String> map = new LinkedHashMap <>();
		map.put("identificador", "identificador");
		map.put("cuenta_clabe_eje", "cuenta_clabe_eje");
		map.put("productoAhorro", "productoAhorro");
		map.put("tipoPersona", "tipoPersona");
		map.put("nombre", "nombre");
		map.put("apellidoPaterno", "apellidoPaterno");
		map.put("apellidoMaterno", "apellidoMaterno");
		map.put("nombreCompleto", "nombreCompleto");
		map.put("rfc", "rfc");
		map.put("curp", "curp");
		map.put("lugarNacimiento", "lugarNacimiento");
		map.put("nacionalidadId", "nacionalidadId");
		map.put("estadoCivilId", "estadoCivilId");
		map.put("telefono", "telefono");
		map.put("celular", "celular");
		map.put("correo", "correo");
		map.put("ocupacionId", "ocupacionId");
		map.put("fechaNacimiento", "fechaNacimiento");
		map.put("sexo", "sexo");
		map.put("gradoEstudiosId", "gradoEstudiosId");
		map.put("tipoIdentificacionId", "tipoIdentificacionId");
		map.put("identificacion", "identificacion");
		map.put("idUbicacion1", "idUbicacion1");
		map.put("descripcionUbicacion1", "descripcionUbicacion1");
		map.put("idUbicacion2", "idUbicacion2");
		map.put("descripcionUbicacion2", "descripcionUbicacion2");
		map.put("idUbicacion3", "idUbicacion3");
		map.put("descripcionUbicacion3", "descripcionUbicacion3");
		map.put("referencia", "referencia");
		map.put("idUbicacion4", "idUbicacion4");
		map.put("descripcionUbicacion4", "descripcionUbicacion4");
		map.put("idUbicacion5", "idUbicacion5");
		map.put("descripcionUbicacion5", "descripcionUbicacion5");
		map.put("idColonia", "idColonia");
		map.put("domicilioCompleto", "domicilioCompleto");
		map.put("ingreso_mensual", "ingreso_mensual");
		map.put("monto_maximo_ahorro", "monto_maximo_ahorro");
		map.put("puesto", "puesto");
		map.put("tipo1", "tipo1");
		map.put("clave1", "clave1");
		map.put("descripcion1", "descripcion1");
		map.put("tipo2", "tipo2");
		map.put("clave2", "clave2");
		map.put("descripcion2", "descripcion2");
		map.put("tipo3", "tipo3");
		map.put("clave3", "clave3");
		map.put("descripcion3", "descripcion3");
		map.put("tipo4", "tipo4");
		map.put("clave4", "clave4");
		map.put("descripcion4", "descripcion4");
		map.put("tipo5", "tipo5");
		map.put("clave5", "clave5");
		map.put("descripcion5", "descripcion5");

		return map;
	}
	
	public LinkedHashMap <String, String> getMapValor(){
		LinkedHashMap <String, String> map = new LinkedHashMap <>();
		map.put("identificador", this.identificador);
		map.put("cuenta_clabe_eje", this.cuenta_clabe_eje);
		map.put("productoAhorro", this.productoAhorro);
		map.put("tipoPersona", this.tipoPersona);
		map.put("nombre", this.nombre);
		map.put("apellidoPaterno", this.apellidoPaterno);
		map.put("apellidoMaterno", this.apellidoMaterno);
		map.put("nombreCompleto", this.nombreCompleto);
		map.put("rfc", this.rfc);
		map.put("curp", this.curp);
		map.put("lugarNacimiento", this.lugarNacimiento);
		map.put("nacionalidadId", this.nacionalidadId);
		map.put("estadoCivilId", this.estadoCivilId);
		map.put("telefono", this.telefono);
		map.put("celular", this.celular);
		map.put("correo", this.correo);
		map.put("ocupacionId", this.ocupacionId);
		map.put("fechaNacimiento", this.fechaNacimiento);
		map.put("sexo", this.sexo);
		map.put("gradoEstudiosId", this.gradoEstudiosId);
		map.put("tipoIdentificacionId", this.tipoIdentificacionId);
		map.put("identificacion", this.identificacion);
		map.put("idUbicacion1", this.idUbicacion1);
		map.put("descripcionUbicacion1", this.descripcionUbicacion1);
		map.put("idUbicacion2", this.idUbicacion2);
		map.put("descripcionUbicacion2", this.descripcionUbicacion2);
		map.put("idUbicacion3", this.idUbicacion3);
		map.put("descripcionUbicacion3", this.descripcionUbicacion3);
		map.put("referencia", this.referencia);
		map.put("idUbicacion4", this.idUbicacion4);
		map.put("descripcionUbicacion4", this.descripcionUbicacion4);
		map.put("idUbicacion5", this.idUbicacion5);
		map.put("descripcionUbicacion5", this.descripcionUbicacion5);
		map.put("idColonia", this.idColonia);
		map.put("domicilioCompleto", this.domicilioCompleto);
		map.put("ingreso_mensual", this.ingreso_mensual);
		map.put("monto_maximo_ahorro", this.monto_maximo_ahorro);
		map.put("puesto", this.puesto);
		map.put("tipo1", this.tipo1);
		map.put("clave1", this.clave1);
		map.put("descripcion1", this.descripcion1);
		map.put("tipo2", this.tipo2);
		map.put("clave2", this.clave2);
		map.put("descripcion2", this.descripcion2);
		map.put("tipo3", this.tipo3);
		map.put("clave3", this.clave3);
		map.put("descripcion3", this.descripcion3);
		map.put("tipo4", this.tipo4);
		map.put("clave4", this.clave4);
		map.put("descripcion4", this.descripcion4);
		map.put("tipo5", this.tipo5);
		map.put("clave5", this.clave5);
		map.put("descripcion5", this.descripcion5);

		return map;
	}
}
