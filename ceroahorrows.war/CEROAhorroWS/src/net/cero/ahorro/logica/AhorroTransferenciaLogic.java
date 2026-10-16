/*
 * @(#)DepositoAhorro.java 1.0 04/07/18 
 * 
 */
package net.cero.ahorro.logica;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;
import net.cero.data.AhorroContrato;
import net.cero.data.AhorroDeposito;
import net.cero.data.AhorroMovimiento;
import net.cero.data.AhorroPagare;
import net.cero.data.AhorroRendimientoVigentes;
import net.cero.data.AhorroSaldos;
import net.cero.data.AhorroTransferenciaOBJ;
import net.cero.data.AhorroTransferenciaReq;
import net.cero.data.AhorroTransferenciaReqOBJ;
import net.cero.data.CajaDepositoAhorroReq;
import net.cero.data.CajaDisposicionAhorroReq;
import net.cero.data.MovimientosCaja;
import net.cero.data.Respuesta;
import net.cero.seguridad.utilidades.ConceptosUtil;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroContratoDAO;
import net.cero.spring.dao.AhorroDepositoDAO;
import net.cero.spring.dao.AhorroMovimientosDAO;
import net.cero.spring.dao.AhorroPagareDAO;
import net.cero.spring.dao.AhorroRendimientoVigenteDAO;
import net.cero.spring.dao.AhorroSaldosDAO;
import net.cero.spring.dao.AhorroTransferenciasDAO;
import net.cero.spring.dao.MovimientosCajaDAO;



/**
 * Logica de negocio para registrar un deposito de ahorro.
 * @author Israel
 * @version 1.0 04/07/18
 */
@Log4j2
public class AhorroTransferenciaLogic {

	private static Apps apps = null;
	
	private static AhorroContratoDAO adao;
	private static AhorroPagareDAO pdao;
	private static AhorroMovimientosDAO mdao;
	private static MovimientosCajaDAO mcdao;
	private static AhorroDepositoDAO addao;
	private static AhorroSaldosDAO asdao;
	//private static AhorroIdeValoresDAO avdao;	
	private static AhorroRendimientoVigenteDAO vdao;
	private static AhorroTransferenciasDAO atdao;
	private static CajaDepositoAhorroLogic cajaDepositoAhorroLogic;
	private static CajaDisposicionAhorroLogic cajaDisposicionAhorroLogic;
	private static Gson gson;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			adao = (AhorroContratoDAO) s.getApplicationContext().getBean("AhorroContratoDAO");
			pdao = (AhorroPagareDAO) s.getApplicationContext().getBean("AhorroPagareDAO");
			mdao = (AhorroMovimientosDAO) s.getApplicationContext().getBean("AhorroMovimientosDAO");
			mcdao = (MovimientosCajaDAO) s.getApplicationContext().getBean("MovimientosCajaDAO");
			addao = (AhorroDepositoDAO) s.getApplicationContext().getBean("AhorroDepositoDAO");
			asdao = (AhorroSaldosDAO) s.getApplicationContext().getBean("AhorroSaldosDAO");
			//avdao = (AhorroIdeValoresDAO) s.getApplicationContext().getBean("AhorroIdeValoresDAO");
			vdao = (AhorroRendimientoVigenteDAO) s.getApplicationContext().getBean("AhorroRendimientoVigenteDAO");
			atdao = (AhorroTransferenciasDAO) s.getApplicationContext().getBean("AhorroTransferenciasDAO");
			cajaDepositoAhorroLogic = new CajaDepositoAhorroLogic();
			cajaDisposicionAhorroLogic = new CajaDisposicionAhorroLogic();
			gson = new Gson();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public Respuesta ahorroTransferencia(AhorroTransferenciaReq req){
		initialized();
		Respuesta respuesta = new Respuesta();
		Integer vMovimientoCaja = 0;
		
		
		respuesta.setCodigo(0);
		respuesta.setMensaje("Deposito registrado");
		return respuesta;
	}
	
	public Respuesta procesaTransferencia(AhorroTransferenciaReqOBJ req){
		initialized();
		Respuesta resp = new Respuesta();
		resp.setCodigo(0);
		CajaDepositoAhorroReq cajaDepositoAhorroReq = new CajaDepositoAhorroReq();
		
		CajaDisposicionAhorroReq cajaDisposicionReq = new CajaDisposicionAhorroReq();
		try{
			AhorroContrato ahorroContrato = new AhorroContrato();
			ahorroContrato = adao.buscarByCuenta(req.getCuentaDestino());
			
			cajaDepositoAhorroReq.setCajaId(ConceptosUtil.CAJA_DEP_TRANSFERENCIA_AHORRO);
			cajaDepositoAhorroReq.setFecha(req.getFecha());
			cajaDepositoAhorroReq.setCuentaAhorro(req.getCuentaDestino());
			cajaDepositoAhorroReq.setMonto(req.getMonto());
			cajaDepositoAhorroReq.setFormaPago(ConceptosUtil.FORMA_APGO_DEP_TRANSFERENCIA_AHO);
			cajaDepositoAhorroReq.setBancoId(ConceptosUtil.BANCO_DEP_TRANSFERENCIA_AHORRO);
			cajaDepositoAhorroReq.setObservacion(req.getConceptoDestino());
			cajaDepositoAhorroReq.setCheque("");
			cajaDepositoAhorroReq.setMovtoId(ConceptosUtil.MOVIMIENTO_DEP_TRANSFERENCIA_AHORRO);
			cajaDepositoAhorroReq.setTransaccionId(null);
			cajaDepositoAhorroReq.setTarjetaOperativaId(null);
			cajaDepositoAhorroReq.setApp(null);
			cajaDepositoAhorroReq.setTransaccionVersionId(null);
			cajaDepositoAhorroReq.setParaConciliar(1);
			cajaDepositoAhorroReq.setFechaDeposito(Calendar.getInstance().getTime());
			cajaDepositoAhorroReq.setUsuarioId(ConceptosUtil.USUARIO_DEP_TRANSFERENCIA_AHORRO);
			cajaDepositoAhorroReq.setControl("");
			cajaDepositoAhorroReq.setSucursalId(ahorroContrato.getSucursalId());
			//log.info("******** REGISTTRA DEPOSITO *********");
			
			/*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            //BitacoraProcesamiento.getInstance().inicializaRegistroBitacora(req.getCuentaOrigen(),BitacoraUtil.REALIZA_DEPOSITO_TRANSFERENCIA);
            /*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
			
			String resultTransferenciaDeposito = cajaDepositoAhorroLogic.registrarDeposioto(cajaDepositoAhorroReq).getData();
			
			/*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            //BitacoraProcesamiento.getInstance().finalizaRegistroBitacora();
            /*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            
            /*///////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*/
			
			cajaDisposicionReq.setCajaId(ConceptosUtil.CAJA_DEP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setFecha(req.getFecha());
			cajaDisposicionReq.setUsuarioId(ConceptosUtil.USUARIO_DEP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setCuentaAhorro(req.getCuentaOrigen());
			cajaDisposicionReq.setMonto(req.getMonto());
			cajaDisposicionReq.setFormaPago(ConceptosUtil.FORMA_APGO_DISP_TRANSFERENCIA_AHO);
			cajaDisposicionReq.setBancoId(ConceptosUtil.BANCO_DISP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setObservacion(req.getConceptoOrigen());
			cajaDisposicionReq.setCheque("");
			cajaDisposicionReq.setMovtoId(ConceptosUtil.MOVIMIENTO_DISP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setTransaccionId(null);
			cajaDisposicionReq.setTarjetaOperativaId(null);
			cajaDisposicionReq.setApp(null);
			cajaDisposicionReq.setTransaccionVersionId(null);
			cajaDisposicionReq.setBancoClie(null);
			cajaDisposicionReq.setAvisoId(null);
			cajaDisposicionReq.setSpeiTransferenciaId(null);
			//log.info("******** REGISTRA DISPOSICION *********");
			
			/*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            //BitacoraProcesamiento.getInstance().inicializaRegistroBitacora(req.getCuentaDestino(),BitacoraUtil.REALIZA_DISPOSICION_TRANSFERENCIA);
            /*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
			
			String resultTransferenciaDisposicion = cajaDisposicionAhorroLogic.disposicionAhorro(cajaDisposicionReq).getData();
			
			/*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            //BitacoraProcesamiento.getInstance().finalizaRegistroBitacora();
            /*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
			
			log.info("******** REGISTRA AHORRO TRANSFERENCIA *********");
			log.info("## resultTransferenciaDeposito :: " + resultTransferenciaDeposito);
			log.info("## resultTransferenciaDisposicion :: " + resultTransferenciaDisposicion);
			registraAhorroTransferencia(resultTransferenciaDeposito, resultTransferenciaDisposicion);
		}catch(Exception e){
			e.printStackTrace();
			resp.setCodigo(-1);
			resp.setMensaje("No se pudo rechazar el pago");
			resp.setData("");
		}
		
		return resp;
	}
	
	private void registraAhorroTransferencia(String resultTransferenciaDeposito, String resultTransferenciaDisposicion) {
		try {
			AhorroTransferenciaOBJ movimientoCajaDeposito = new AhorroTransferenciaOBJ();
			AhorroTransferenciaOBJ movimientoCajaDisposicion = new AhorroTransferenciaOBJ();
			AhorroTransferenciaOBJ ahorroTransferencia = new AhorroTransferenciaOBJ();
			if(resultTransferenciaDeposito != null) {
				if(!resultTransferenciaDeposito.isEmpty()) {
					if(resultTransferenciaDisposicion != null) {
						if(!resultTransferenciaDisposicion.isEmpty()){
							movimientoCajaDeposito = gson.fromJson(resultTransferenciaDeposito, AhorroTransferenciaOBJ.class);
							
							movimientoCajaDisposicion = gson.fromJson(resultTransferenciaDisposicion, AhorroTransferenciaOBJ.class);
							
							if(movimientoCajaDeposito != null) {
								if(movimientoCajaDisposicion != null) {
									log.info("## LLENA EL REGISTRO DE AHORRO TRANSFERENCIA ");
									
									ahorroTransferencia.setCuentaOrigen(movimientoCajaDisposicion.getCuentaOrigen());
									ahorroTransferencia.setCuentaDestino(movimientoCajaDeposito.getCuentaDestino());
									ahorroTransferencia.setFecha(Calendar.getInstance().getTime());
									ahorroTransferencia.setMonto(movimientoCajaDisposicion.getMonto());
									ahorroTransferencia.setCreadoPor(ConceptosUtil.USUARIO_DEP_TRANSFERENCIA_AHORRO);
									
									ahorroTransferencia.setMovimientoId(movimientoCajaDisposicion.getMovimientoId());
									ahorroTransferencia.setDisposicionId(movimientoCajaDisposicion.getDisposicionId());
									ahorroTransferencia.setDepositoId(movimientoCajaDeposito.getDepositoId());
									
									log.info("## DATOS DE TRANSFERENCIA A INSERTAR :: " + gson.toJson(ahorroTransferencia));
									ahorroTransferencia.setId(atdao.nuevoAT(ahorroTransferencia));
									if(ahorroTransferencia.getId() > 0) {
										log.info("## TRANSFERENCIA REGISTRADA");
									}
								}
							}
						}							
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			log.info("## ALGO SALIO MAL AL REGISTRAR AHORRO TRANSFERENCIA :: " + e.getMessage());
		}
		
	}
}
