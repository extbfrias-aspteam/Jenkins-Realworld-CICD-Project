package net.cero.ahorro.logica;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.cero.data.MovimientoDenegadoObj;
import net.cero.data.Respuesta;
import net.cero.spring.dao.MovimientosDAO;
import net.cero.spring.config.Apps;
/**
 * Clase que contendra la logica de movimientos, utiliza las clases DAO para operar con la BD
 * @author rodym
 *
 */
public class MovimientosLogic {	
	private static final Logger log = LogManager.getLogger(MovimientosLogic.class);
	/**
	 * contexto de la aplicacion para poder inyectar los DAO 
	 */
	private static Apps apps = null;
	/**
	 * Dao para operar los movimientos en la Base de Datos
	 */
	private static MovimientosDAO movdao;
	
	/**
	 * Inicialización para "inyectar" el DAO
	 */
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) 
					apps = s; 		
			}			
			movdao = (MovimientosDAO) s.getApplicationContext().getBean("MovimientosDAO");
		} catch (Exception e) {
			log.error("error al obtener el bean ", e);
		}
	}
		
	/**
	 * @return the movdao
	 */
	public static MovimientosDAO getMovdao() {
		return movdao;
	}


	/**
	 * @param movdao the movdao to set
	 */
	public static void setMovdao(MovimientosDAO movdao) {
		MovimientosLogic.movdao = movdao;
		
	}


	public Respuesta consultaCatalogoMovimientosManuales() {
		// TODO Auto-generated method stub
		initialized();
		final Respuesta respuesta = movdao.consultaCatalagoMovimientosManuales();
		return respuesta;
	}

	public Respuesta consultaMovimientosDenegados(MovimientoDenegadoObj movimientosDenegado) {
		
		return null;
	}
	
}
