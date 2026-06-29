package net.std.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.data.AutorizacionOBJ;

public class AutorizacionStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AutorizacionStdDAO.class);

	private JdbcTemplate jdbcTemplate;
	private String Actualiza_AutorizacionStd;

	public RespuestaSVC actualizaAutorizaStdDao(AutorizacionOBJ aut) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			
			int row = jdbcTemplate.update(Actualiza_AutorizacionStd, _T(aut.getAutorizado()), 
																	 Comun._T(aut.getObservaciones()), 
					                                                 Comun._I(Constantes.USUARIO_ID), 
					                                                 aut.getClaveRastreo());
			if(row == 1){
				respuesta.getBody().addValor("RESPUESTA", String.format("REGISTRO ACTUALIZADO, CLAVE RASTREO %s - %s", aut.getClaveRastreo(),
						                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
			}else{	
				//respuesta.getErrores().addCodigo(null, Errores.ERROR_AUTORIZAR, Errores.desc(Errores.ERROR_AUTORIZAR, aut.getClaveRastreo()));
				respuesta.getBody().addValor("RESPUESTA", String.format("REGISTRO CON CLAVE DE RASTREO NO EXISTE O ESTA PROCESADO : %s", aut.getClaveRastreo()));  
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_AUTORIZAR,  ex.getMessage()));
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	private String _T(Object obj){
		return obj == null ? null : String.valueOf(obj);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getActualiza_AutorizacionStd() {
		return Actualiza_AutorizacionStd;
	}


	public void setActualiza_AutorizacionStd(String actualiza_AutorizacionStd) {
		Actualiza_AutorizacionStd = actualiza_AutorizacionStd;
	}
}

