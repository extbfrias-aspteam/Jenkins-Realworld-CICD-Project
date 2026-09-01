package net.cero.data;

import java.io.Serializable;
import java.math.BigDecimal;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the solicitante database table.
 * 
 */
public class Solicitante implements Serializable {
	private static final long serialVersionUID = 1L;

	private String numero;
	private Integer actividad;
	private Integer aestudios;
	private String agenteId;
	private String apellidoC;
	private String apellidoM;
	private String apellidoMC;
	private String apellidomAlt;
	private String apellidopAlt;
	private String apellidos;
	private String asociacion;
	private String atencionHaa;
	private String atencionHd;
	private String atencionSemana;
	private Integer bancoProveedor;
	private Integer bancoSolicitante;
	private String beneficiarioProveedor;
	private Integer bloqueado;
	private Integer catDomicilio1;
	private Integer catDomicilio2;
	private Integer catDomicilio3;
	private Integer catDomicilio4;
	private Integer catDomicilio5;
	private String cedulaProfesional;
	private String celular;
	private String centroTrabajo;
	private String clabeProveedor;
	private String clausulaRecaudador;
	private String claveDistritoDesRural;
	private Integer colonia;
	private Integer coloniaC;
	private Integer coloniaEm;
	private BigDecimal compras;
	private String control;
	private String correo;
	private String correoAsp;
	private String correoContacto;
	private String correoElectronicoProveedor;
	private Integer creadoPor;
	private String credencialIfe;
	private String credencialIfeC;
	private String curp;
	private String curpValidar;
	private Integer cveNacionalidad;
	private Integer cveNacionalidadC;
	private Integer cveOcupacion;
	private Integer cveOcupacionC;
	private String descProveedor;
	private String descripcionDomicilio1;
	private String descripcionDomicilio2;
	private String descripcionDomicilio3;
	private String descripcionDomicilio4;
	private String descripcionDomicilio5;
	private Integer diaCorte;
	private Integer diaCorte2;
	private Integer diaPago;
	private Integer diaPago2;
	private String discapacidad;
	private String domicilio;
	private String domicilioC;
	private String domicilioEm;
	private String domiciloObservaciones;
	private String edoCivil;
	private Integer edoNacId;
	private String empresa;
	private String empresaReferencia;
	private String estudios;
	private Integer estudiosId;
	private String fax;
	private Date fechaClienteDesde;
	private Date fechaCreacion;
	private Date fechaIngreso;
	private Date fechaModificacion;
	private String folioFiscal;
	private Integer giro;
	private Integer giroAnt;
	private Integer gobierno;
	private String grupoId;
	private String hijosEdades;
	private Integer homonimo;
	private String idConyuge;
	private String idDeudor;
	private Long idIzel;
	private String imgDomicilio;
	private BigDecimal ingresoMensual;
	private BigDecimal ingresos;
	private String lenguaIndigena;
	private BigDecimal lineacredito;
	private String lugarTrabajo;
	private Integer modificadoPor;
	private BigDecimal montoInicial;
	private BigDecimal montoMaxAhorro;
	private BigDecimal montoMaxAnticipo;
	private BigDecimal montoMaxPago;
	private String nomContacto;
	private String nombre;
	private String nombreAlt;
	private String nombreC;
	private String nombreP;
	private String nombrepAlt;
	private String nss;
	private Integer numSec;
	private String numTrasferencia;
	private String numTrasferenciaTipo;
	private String numeroCasa;
	private String numeroCasaC;
	private String numeroCasaEm;
	private Integer numeroFirma;
	private Integer numeroHijos;
	private String originario;
	private String originarioC;
	private Integer parentesco;
	private BigDecimal patrimonio;
	private String permisoSecretaria;
	private String personalidad;
	private String propietarioCasa;
	private String propietarioEmp;
	private String puesto;
	private Integer recaudador;
	private String redesSociales;
	private String referencia;
	private BigDecimal rentaCasa;
	private BigDecimal rentaEmp;
	private String responsab;
	private String rfc1;
	private String rfc1C;
	private String rfc2;
	private String rfc2C;
	private String rfc3;
	private String rfc3C;
	private Integer sector;
	private String sexo;
	private String sexoC;
	private String tPersona;
	private Boolean tProveedor;
	private String telPropEmp;
	private String telefono;
	private String telefonoEm;
	private String tenenciaTierra;
	private String testudios;
	private Integer tiempoCasa;
	private Integer tipoClienteId;
	private Integer tipoLugarEmp;
	private Integer tipoSociedadId;
	private String ubicacionGeografica;
	private String usoInternet;
	private Integer viviendaId;
	private List<AhorroContrato> ahorroContratos;

	public Solicitante() {
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Integer getActividad() {
		return this.actividad;
	}

	public void setActividad(Integer actividad) {
		this.actividad = actividad;
	}

	public Integer getAestudios() {
		return this.aestudios;
	}

	public void setAestudios(Integer aestudios) {
		this.aestudios = aestudios;
	}

	public String getAgenteId() {
		return this.agenteId;
	}

	public void setAgenteId(String agenteId) {
		this.agenteId = agenteId;
	}

	public String getApellidoC() {
		return this.apellidoC;
	}

	public void setApellidoC(String apellidoC) {
		this.apellidoC = apellidoC;
	}

	public String getApellidoM() {
		return this.apellidoM;
	}

	public void setApellidoM(String apellidoM) {
		this.apellidoM = apellidoM;
	}

	public String getApellidoMC() {
		return this.apellidoMC;
	}

	public void setApellidoMC(String apellidoMC) {
		this.apellidoMC = apellidoMC;
	}

	public String getApellidomAlt() {
		return this.apellidomAlt;
	}

	public void setApellidomAlt(String apellidomAlt) {
		this.apellidomAlt = apellidomAlt;
	}

	public String getApellidopAlt() {
		return this.apellidopAlt;
	}

	public void setApellidopAlt(String apellidopAlt) {
		this.apellidopAlt = apellidopAlt;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getAsociacion() {
		return this.asociacion;
	}

	public void setAsociacion(String asociacion) {
		this.asociacion = asociacion;
	}

	public String getAtencionHaa() {
		return this.atencionHaa;
	}

	public void setAtencionHaa(String atencionHaa) {
		this.atencionHaa = atencionHaa;
	}

	public String getAtencionHd() {
		return this.atencionHd;
	}

	public void setAtencionHd(String atencionHd) {
		this.atencionHd = atencionHd;
	}

	public String getAtencionSemana() {
		return this.atencionSemana;
	}

	public void setAtencionSemana(String atencionSemana) {
		this.atencionSemana = atencionSemana;
	}

	public Integer getBancoProveedor() {
		return this.bancoProveedor;
	}

	public void setBancoProveedor(Integer bancoProveedor) {
		this.bancoProveedor = bancoProveedor;
	}

	public Integer getBancoSolicitante() {
		return this.bancoSolicitante;
	}

	public void setBancoSolicitante(Integer bancoSolicitante) {
		this.bancoSolicitante = bancoSolicitante;
	}

	public String getBeneficiarioProveedor() {
		return this.beneficiarioProveedor;
	}

	public void setBeneficiarioProveedor(String beneficiarioProveedor) {
		this.beneficiarioProveedor = beneficiarioProveedor;
	}

	public Integer getBloqueado() {
		return this.bloqueado;
	}

	public void setBloqueado(Integer bloqueado) {
		this.bloqueado = bloqueado;
	}

	public Integer getCatDomicilio1() {
		return this.catDomicilio1;
	}

	public void setCatDomicilio1(Integer catDomicilio1) {
		this.catDomicilio1 = catDomicilio1;
	}

	public Integer getCatDomicilio2() {
		return this.catDomicilio2;
	}

	public void setCatDomicilio2(Integer catDomicilio2) {
		this.catDomicilio2 = catDomicilio2;
	}

	public Integer getCatDomicilio3() {
		return this.catDomicilio3;
	}

	public void setCatDomicilio3(Integer catDomicilio3) {
		this.catDomicilio3 = catDomicilio3;
	}

	public Integer getCatDomicilio4() {
		return this.catDomicilio4;
	}

	public void setCatDomicilio4(Integer catDomicilio4) {
		this.catDomicilio4 = catDomicilio4;
	}

	public Integer getCatDomicilio5() {
		return this.catDomicilio5;
	}

	public void setCatDomicilio5(Integer catDomicilio5) {
		this.catDomicilio5 = catDomicilio5;
	}

	public String getCedulaProfesional() {
		return this.cedulaProfesional;
	}

	public void setCedulaProfesional(String cedulaProfesional) {
		this.cedulaProfesional = cedulaProfesional;
	}

	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCentroTrabajo() {
		return this.centroTrabajo;
	}

	public void setCentroTrabajo(String centroTrabajo) {
		this.centroTrabajo = centroTrabajo;
	}

	public String getClabeProveedor() {
		return this.clabeProveedor;
	}

	public void setClabeProveedor(String clabeProveedor) {
		this.clabeProveedor = clabeProveedor;
	}

	public String getClausulaRecaudador() {
		return this.clausulaRecaudador;
	}

	public void setClausulaRecaudador(String clausulaRecaudador) {
		this.clausulaRecaudador = clausulaRecaudador;
	}

	public String getClaveDistritoDesRural() {
		return this.claveDistritoDesRural;
	}

	public void setClaveDistritoDesRural(String claveDistritoDesRural) {
		this.claveDistritoDesRural = claveDistritoDesRural;
	}

	public Integer getColonia() {
		return this.colonia;
	}

	public void setColonia(Integer colonia) {
		this.colonia = colonia;
	}

	public Integer getColoniaC() {
		return this.coloniaC;
	}

	public void setColoniaC(Integer coloniaC) {
		this.coloniaC = coloniaC;
	}

	public Integer getColoniaEm() {
		return this.coloniaEm;
	}

	public void setColoniaEm(Integer coloniaEm) {
		this.coloniaEm = coloniaEm;
	}

	public BigDecimal getCompras() {
		return this.compras;
	}

	public void setCompras(BigDecimal compras) {
		this.compras = compras;
	}

	public String getControl() {
		return this.control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getCorreoAsp() {
		return this.correoAsp;
	}

	public void setCorreoAsp(String correoAsp) {
		this.correoAsp = correoAsp;
	}

	public String getCorreoContacto() {
		return this.correoContacto;
	}

	public void setCorreoContacto(String correoContacto) {
		this.correoContacto = correoContacto;
	}

	public String getCorreoElectronicoProveedor() {
		return this.correoElectronicoProveedor;
	}

	public void setCorreoElectronicoProveedor(String correoElectronicoProveedor) {
		this.correoElectronicoProveedor = correoElectronicoProveedor;
	}

	public Integer getCreadoPor() {
		return this.creadoPor;
	}

	public void setCreadoPor(Integer creadoPor) {
		this.creadoPor = creadoPor;
	}

	public String getCredencialIfe() {
		return this.credencialIfe;
	}

	public void setCredencialIfe(String credencialIfe) {
		this.credencialIfe = credencialIfe;
	}

	public String getCredencialIfeC() {
		return this.credencialIfeC;
	}

	public void setCredencialIfeC(String credencialIfeC) {
		this.credencialIfeC = credencialIfeC;
	}

	public String getCurp() {
		return this.curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getCurpValidar() {
		return this.curpValidar;
	}

	public void setCurpValidar(String curpValidar) {
		this.curpValidar = curpValidar;
	}

	public Integer getCveNacionalidad() {
		return this.cveNacionalidad;
	}

	public void setCveNacionalidad(Integer cveNacionalidad) {
		this.cveNacionalidad = cveNacionalidad;
	}

	public Integer getCveNacionalidadC() {
		return this.cveNacionalidadC;
	}

	public void setCveNacionalidadC(Integer cveNacionalidadC) {
		this.cveNacionalidadC = cveNacionalidadC;
	}

	public Integer getCveOcupacion() {
		return this.cveOcupacion;
	}

	public void setCveOcupacion(Integer cveOcupacion) {
		this.cveOcupacion = cveOcupacion;
	}

	public Integer getCveOcupacionC() {
		return this.cveOcupacionC;
	}

	public void setCveOcupacionC(Integer cveOcupacionC) {
		this.cveOcupacionC = cveOcupacionC;
	}

	public String getDescProveedor() {
		return this.descProveedor;
	}

	public void setDescProveedor(String descProveedor) {
		this.descProveedor = descProveedor;
	}

	public String getDescripcionDomicilio1() {
		return this.descripcionDomicilio1;
	}

	public void setDescripcionDomicilio1(String descripcionDomicilio1) {
		this.descripcionDomicilio1 = descripcionDomicilio1;
	}

	public String getDescripcionDomicilio2() {
		return this.descripcionDomicilio2;
	}

	public void setDescripcionDomicilio2(String descripcionDomicilio2) {
		this.descripcionDomicilio2 = descripcionDomicilio2;
	}

	public String getDescripcionDomicilio3() {
		return this.descripcionDomicilio3;
	}

	public void setDescripcionDomicilio3(String descripcionDomicilio3) {
		this.descripcionDomicilio3 = descripcionDomicilio3;
	}

	public String getDescripcionDomicilio4() {
		return this.descripcionDomicilio4;
	}

	public void setDescripcionDomicilio4(String descripcionDomicilio4) {
		this.descripcionDomicilio4 = descripcionDomicilio4;
	}

	public String getDescripcionDomicilio5() {
		return this.descripcionDomicilio5;
	}

	public void setDescripcionDomicilio5(String descripcionDomicilio5) {
		this.descripcionDomicilio5 = descripcionDomicilio5;
	}

	public Integer getDiaCorte() {
		return this.diaCorte;
	}

	public void setDiaCorte(Integer diaCorte) {
		this.diaCorte = diaCorte;
	}

	public Integer getDiaCorte2() {
		return this.diaCorte2;
	}

	public void setDiaCorte2(Integer diaCorte2) {
		this.diaCorte2 = diaCorte2;
	}

	public Integer getDiaPago() {
		return this.diaPago;
	}

	public void setDiaPago(Integer diaPago) {
		this.diaPago = diaPago;
	}

	public Integer getDiaPago2() {
		return this.diaPago2;
	}

	public void setDiaPago2(Integer diaPago2) {
		this.diaPago2 = diaPago2;
	}

	public String getDiscapacidad() {
		return this.discapacidad;
	}

	public void setDiscapacidad(String discapacidad) {
		this.discapacidad = discapacidad;
	}

	public String getDomicilio() {
		return this.domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getDomicilioC() {
		return this.domicilioC;
	}

	public void setDomicilioC(String domicilioC) {
		this.domicilioC = domicilioC;
	}

	public String getDomicilioEm() {
		return this.domicilioEm;
	}

	public void setDomicilioEm(String domicilioEm) {
		this.domicilioEm = domicilioEm;
	}

	public String getDomiciloObservaciones() {
		return this.domiciloObservaciones;
	}

	public void setDomiciloObservaciones(String domiciloObservaciones) {
		this.domiciloObservaciones = domiciloObservaciones;
	}

	public String getEdoCivil() {
		return this.edoCivil;
	}

	public void setEdoCivil(String edoCivil) {
		this.edoCivil = edoCivil;
	}

	public Integer getEdoNacId() {
		return this.edoNacId;
	}

	public void setEdoNacId(Integer edoNacId) {
		this.edoNacId = edoNacId;
	}

	public String getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getEmpresaReferencia() {
		return this.empresaReferencia;
	}

	public void setEmpresaReferencia(String empresaReferencia) {
		this.empresaReferencia = empresaReferencia;
	}

	public String getEstudios() {
		return this.estudios;
	}

	public void setEstudios(String estudios) {
		this.estudios = estudios;
	}

	public Integer getEstudiosId() {
		return this.estudiosId;
	}

	public void setEstudiosId(Integer estudiosId) {
		this.estudiosId = estudiosId;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Date getFechaClienteDesde() {
		return this.fechaClienteDesde;
	}

	public void setFechaClienteDesde(Date fechaClienteDesde) {
		this.fechaClienteDesde = fechaClienteDesde;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getFolioFiscal() {
		return this.folioFiscal;
	}

	public void setFolioFiscal(String folioFiscal) {
		this.folioFiscal = folioFiscal;
	}

	public Integer getGiro() {
		return this.giro;
	}

	public void setGiro(Integer giro) {
		this.giro = giro;
	}

	public Integer getGiroAnt() {
		return this.giroAnt;
	}

	public void setGiroAnt(Integer giroAnt) {
		this.giroAnt = giroAnt;
	}

	public Integer getGobierno() {
		return this.gobierno;
	}

	public void setGobierno(Integer gobierno) {
		this.gobierno = gobierno;
	}

	public String getGrupoId() {
		return this.grupoId;
	}

	public void setGrupoId(String grupoId) {
		this.grupoId = grupoId;
	}

	public String getHijosEdades() {
		return this.hijosEdades;
	}

	public void setHijosEdades(String hijosEdades) {
		this.hijosEdades = hijosEdades;
	}

	public Integer getHomonimo() {
		return this.homonimo;
	}

	public void setHomonimo(Integer homonimo) {
		this.homonimo = homonimo;
	}

	public String getIdConyuge() {
		return this.idConyuge;
	}

	public void setIdConyuge(String idConyuge) {
		this.idConyuge = idConyuge;
	}

	public String getIdDeudor() {
		return this.idDeudor;
	}

	public void setIdDeudor(String idDeudor) {
		this.idDeudor = idDeudor;
	}

	public Long getIdIzel() {
		return this.idIzel;
	}

	public void setIdIzel(Long idIzel) {
		this.idIzel = idIzel;
	}

	public String getImgDomicilio() {
		return this.imgDomicilio;
	}

	public void setImgDomicilio(String imgDomicilio) {
		this.imgDomicilio = imgDomicilio;
	}

	public BigDecimal getIngresoMensual() {
		return this.ingresoMensual;
	}

	public void setIngresoMensual(BigDecimal ingresoMensual) {
		this.ingresoMensual = ingresoMensual;
	}

	public BigDecimal getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(BigDecimal ingresos) {
		this.ingresos = ingresos;
	}

	public String getLenguaIndigena() {
		return this.lenguaIndigena;
	}

	public void setLenguaIndigena(String lenguaIndigena) {
		this.lenguaIndigena = lenguaIndigena;
	}

	public BigDecimal getLineacredito() {
		return this.lineacredito;
	}

	public void setLineacredito(BigDecimal lineacredito) {
		this.lineacredito = lineacredito;
	}

	public String getLugarTrabajo() {
		return this.lugarTrabajo;
	}

	public void setLugarTrabajo(String lugarTrabajo) {
		this.lugarTrabajo = lugarTrabajo;
	}

	public Integer getModificadoPor() {
		return this.modificadoPor;
	}

	public void setModificadoPor(Integer modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public BigDecimal getMontoInicial() {
		return this.montoInicial;
	}

	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}

	public BigDecimal getMontoMaxAhorro() {
		return this.montoMaxAhorro;
	}

	public void setMontoMaxAhorro(BigDecimal montoMaxAhorro) {
		this.montoMaxAhorro = montoMaxAhorro;
	}

	public BigDecimal getMontoMaxAnticipo() {
		return this.montoMaxAnticipo;
	}

	public void setMontoMaxAnticipo(BigDecimal montoMaxAnticipo) {
		this.montoMaxAnticipo = montoMaxAnticipo;
	}

	public BigDecimal getMontoMaxPago() {
		return this.montoMaxPago;
	}

	public void setMontoMaxPago(BigDecimal montoMaxPago) {
		this.montoMaxPago = montoMaxPago;
	}

	public String getNomContacto() {
		return this.nomContacto;
	}

	public void setNomContacto(String nomContacto) {
		this.nomContacto = nomContacto;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreAlt() {
		return this.nombreAlt;
	}

	public void setNombreAlt(String nombreAlt) {
		this.nombreAlt = nombreAlt;
	}

	public String getNombreC() {
		return this.nombreC;
	}

	public void setNombreC(String nombreC) {
		this.nombreC = nombreC;
	}

	public String getNombreP() {
		return this.nombreP;
	}

	public void setNombreP(String nombreP) {
		this.nombreP = nombreP;
	}

	public String getNombrepAlt() {
		return this.nombrepAlt;
	}

	public void setNombrepAlt(String nombrepAlt) {
		this.nombrepAlt = nombrepAlt;
	}

	public String getNss() {
		return this.nss;
	}

	public void setNss(String nss) {
		this.nss = nss;
	}

	public Integer getNumSec() {
		return this.numSec;
	}

	public void setNumSec(Integer numSec) {
		this.numSec = numSec;
	}

	public String getNumTrasferencia() {
		return this.numTrasferencia;
	}

	public void setNumTrasferencia(String numTrasferencia) {
		this.numTrasferencia = numTrasferencia;
	}

	public String getNumTrasferenciaTipo() {
		return this.numTrasferenciaTipo;
	}

	public void setNumTrasferenciaTipo(String numTrasferenciaTipo) {
		this.numTrasferenciaTipo = numTrasferenciaTipo;
	}

	public String getNumeroCasa() {
		return this.numeroCasa;
	}

	public void setNumeroCasa(String numeroCasa) {
		this.numeroCasa = numeroCasa;
	}

	public String getNumeroCasaC() {
		return this.numeroCasaC;
	}

	public void setNumeroCasaC(String numeroCasaC) {
		this.numeroCasaC = numeroCasaC;
	}

	public String getNumeroCasaEm() {
		return this.numeroCasaEm;
	}

	public void setNumeroCasaEm(String numeroCasaEm) {
		this.numeroCasaEm = numeroCasaEm;
	}

	public Integer getNumeroFirma() {
		return this.numeroFirma;
	}

	public void setNumeroFirma(Integer numeroFirma) {
		this.numeroFirma = numeroFirma;
	}

	public Integer getNumeroHijos() {
		return this.numeroHijos;
	}

	public void setNumeroHijos(Integer numeroHijos) {
		this.numeroHijos = numeroHijos;
	}

	public String getOriginario() {
		return this.originario;
	}

	public void setOriginario(String originario) {
		this.originario = originario;
	}

	public String getOriginarioC() {
		return this.originarioC;
	}

	public void setOriginarioC(String originarioC) {
		this.originarioC = originarioC;
	}

	public Integer getParentesco() {
		return this.parentesco;
	}

	public void setParentesco(Integer parentesco) {
		this.parentesco = parentesco;
	}

	public BigDecimal getPatrimonio() {
		return this.patrimonio;
	}

	public void setPatrimonio(BigDecimal patrimonio) {
		this.patrimonio = patrimonio;
	}

	public String getPermisoSecretaria() {
		return this.permisoSecretaria;
	}

	public void setPermisoSecretaria(String permisoSecretaria) {
		this.permisoSecretaria = permisoSecretaria;
	}

	public String getPersonalidad() {
		return this.personalidad;
	}

	public void setPersonalidad(String personalidad) {
		this.personalidad = personalidad;
	}

	public String getPropietarioCasa() {
		return this.propietarioCasa;
	}

	public void setPropietarioCasa(String propietarioCasa) {
		this.propietarioCasa = propietarioCasa;
	}

	public String getPropietarioEmp() {
		return this.propietarioEmp;
	}

	public void setPropietarioEmp(String propietarioEmp) {
		this.propietarioEmp = propietarioEmp;
	}

	public String getPuesto() {
		return this.puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public Integer getRecaudador() {
		return this.recaudador;
	}

	public void setRecaudador(Integer recaudador) {
		this.recaudador = recaudador;
	}

	public String getRedesSociales() {
		return this.redesSociales;
	}

	public void setRedesSociales(String redesSociales) {
		this.redesSociales = redesSociales;
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public BigDecimal getRentaCasa() {
		return this.rentaCasa;
	}

	public void setRentaCasa(BigDecimal rentaCasa) {
		this.rentaCasa = rentaCasa;
	}

	public BigDecimal getRentaEmp() {
		return this.rentaEmp;
	}

	public void setRentaEmp(BigDecimal rentaEmp) {
		this.rentaEmp = rentaEmp;
	}

	public String getResponsab() {
		return this.responsab;
	}

	public void setResponsab(String responsab) {
		this.responsab = responsab;
	}

	public String getRfc1() {
		return this.rfc1;
	}

	public void setRfc1(String rfc1) {
		this.rfc1 = rfc1;
	}

	public String getRfc1C() {
		return this.rfc1C;
	}

	public void setRfc1C(String rfc1C) {
		this.rfc1C = rfc1C;
	}

	public String getRfc2() {
		return this.rfc2;
	}

	public void setRfc2(String rfc2) {
		this.rfc2 = rfc2;
	}

	public String getRfc2C() {
		return this.rfc2C;
	}

	public void setRfc2C(String rfc2C) {
		this.rfc2C = rfc2C;
	}

	public String getRfc3() {
		return this.rfc3;
	}

	public void setRfc3(String rfc3) {
		this.rfc3 = rfc3;
	}

	public String getRfc3C() {
		return this.rfc3C;
	}

	public void setRfc3C(String rfc3C) {
		this.rfc3C = rfc3C;
	}

	public Integer getSector() {
		return this.sector;
	}

	public void setSector(Integer sector) {
		this.sector = sector;
	}

	public String getSexo() {
		return this.sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getSexoC() {
		return this.sexoC;
	}

	public void setSexoC(String sexoC) {
		this.sexoC = sexoC;
	}

	public String getTPersona() {
		return this.tPersona;
	}

	public void setTPersona(String tPersona) {
		this.tPersona = tPersona;
	}

	public Boolean getTProveedor() {
		return this.tProveedor;
	}

	public void setTProveedor(Boolean tProveedor) {
		this.tProveedor = tProveedor;
	}

	public String getTelPropEmp() {
		return this.telPropEmp;
	}

	public void setTelPropEmp(String telPropEmp) {
		this.telPropEmp = telPropEmp;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefonoEm() {
		return this.telefonoEm;
	}

	public void setTelefonoEm(String telefonoEm) {
		this.telefonoEm = telefonoEm;
	}

	public String getTenenciaTierra() {
		return this.tenenciaTierra;
	}

	public void setTenenciaTierra(String tenenciaTierra) {
		this.tenenciaTierra = tenenciaTierra;
	}

	public String getTestudios() {
		return this.testudios;
	}

	public void setTestudios(String testudios) {
		this.testudios = testudios;
	}

	public Integer getTiempoCasa() {
		return this.tiempoCasa;
	}

	public void setTiempoCasa(Integer tiempoCasa) {
		this.tiempoCasa = tiempoCasa;
	}

	public Integer getTipoClienteId() {
		return this.tipoClienteId;
	}

	public void setTipoClienteId(Integer tipoClienteId) {
		this.tipoClienteId = tipoClienteId;
	}

	public Integer getTipoLugarEmp() {
		return this.tipoLugarEmp;
	}

	public void setTipoLugarEmp(Integer tipoLugarEmp) {
		this.tipoLugarEmp = tipoLugarEmp;
	}

	public Integer getTipoSociedadId() {
		return this.tipoSociedadId;
	}

	public void setTipoSociedadId(Integer tipoSociedadId) {
		this.tipoSociedadId = tipoSociedadId;
	}

	public String getUbicacionGeografica() {
		return this.ubicacionGeografica;
	}

	public void setUbicacionGeografica(String ubicacionGeografica) {
		this.ubicacionGeografica = ubicacionGeografica;
	}

	public String getUsoInternet() {
		return this.usoInternet;
	}

	public void setUsoInternet(String usoInternet) {
		this.usoInternet = usoInternet;
	}

	public Integer getViviendaId() {
		return this.viviendaId;
	}

	public void setViviendaId(Integer viviendaId) {
		this.viviendaId = viviendaId;
	}

	public List<AhorroContrato> getAhorroContratos() {
		return this.ahorroContratos;
	}

	public void setAhorroContratos(List<AhorroContrato> ahorroContratos) {
		this.ahorroContratos = ahorroContratos;
	}

	public AhorroContrato addAhorroContrato(AhorroContrato ahorroContrato) {
		getAhorroContratos().add(ahorroContrato);
		ahorroContrato.setSolicitante(this.numero);

		return ahorroContrato;
	}

	public AhorroContrato removeAhorroContrato(AhorroContrato ahorroContrato) {
		getAhorroContratos().remove(ahorroContrato);
		ahorroContrato.setSolicitante(null);

		return ahorroContrato;
	}

}