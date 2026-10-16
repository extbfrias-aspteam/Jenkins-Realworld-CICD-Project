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
import net.cero.data.AhorroAvisoRetiroOBJ;
import net.cero.data.AhorroDisposicionesOBJ;
import net.cero.data.AhorroMovimiento;
import net.cero.data.AhorroPagare;
import net.cero.data.AhorroTransferenciaOBJ;
import net.cero.data.CajaDepositoAhorroReq;
import net.cero.data.CajaDisposicionAhorroReq;
import net.cero.data.MovimientosCaja;
import net.cero.data.Respuesta;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroAvisosRetiroDAO;
import net.cero.spring.dao.AhorroContratoDAO;
import net.cero.spring.dao.AhorroDepositoDAO;
import net.cero.spring.dao.AhorroDisposicionesDAO;
import net.cero.spring.dao.AhorroMovimientosDAO;
import net.cero.spring.dao.AhorroPagareDAO;
import net.cero.spring.dao.AhorroRendimientoVigenteDAO;
import net.cero.spring.dao.AhorroSaldosDAO;
import net.cero.spring.dao.MovimientosCajaDAO;



/**
 * Logica de negocio para registrar un deposito de ahorro.
 * @author Israel
 * @version 1.0 04/07/18
 */
@Log4j2
public class CajaDisposicionAhorroLogic {
	private static Apps apps = null;
	
	//private static AhorroContratoDAO adao;
	private static AhorroPagareDAO pdao;
	private static AhorroMovimientosDAO mdao;
	private static AhorroDisposicionesDAO adispdao;
	private static MovimientosCajaDAO mcdao;
	//private static AhorroDepositoDAO addao;
	//private static AhorroSaldosDAO asdao;
	//private static AhorroRendimientoVigenteDAO vdao;
	private static AhorroAvisosRetiroDAO ahorroavisodao;
	
	private static AhorroActualizaSaldoLogic ahorroActualizaSaldoL;
	private static InsertaChequeLogic insertaChequeLogic;
	private static Gson gson;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			//adao = (AhorroContratoDAO) s.getApplicationContext().getBean("AhorroContratoDAO");
			pdao = (AhorroPagareDAO) s.getApplicationContext().getBean("AhorroPagareDAO");
			mdao = (AhorroMovimientosDAO) s.getApplicationContext().getBean("AhorroMovimientosDAO");
			mcdao = (MovimientosCajaDAO) s.getApplicationContext().getBean("MovimientosCajaDAO");
			//addao = (AhorroDepositoDAO) s.getApplicationContext().getBean("AhorroDepositoDAO");
			//asdao = (AhorroSaldosDAO) s.getApplicationContext().getBean("AhorroSaldosDAO");
			//vdao = (AhorroRendimientoVigenteDAO) s.getApplicationContext().getBean("AhorroRendimientoVigenteDAO");
			ahorroavisodao = (AhorroAvisosRetiroDAO) s.getApplicationContext().getBean("AhorroAvisosRetiroDAO");
			adispdao = (AhorroDisposicionesDAO) s.getApplicationContext().getBean("AhorroDisposicionesDAO");
			
			gson = new Gson();
			ahorroActualizaSaldoL = new AhorroActualizaSaldoLogic();
			insertaChequeLogic = new InsertaChequeLogic();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public Respuesta disposicionAhorro(CajaDisposicionAhorroReq req){
		initialized();
		//log.info("******** INICIA DISPOSICION *********");
		Respuesta respuesta = new Respuesta();
		AhorroDisposicionesOBJ ahorroDisposicion = new AhorroDisposicionesOBJ();
		MovimientosCaja movimientoCaja = new MovimientosCaja();
		AhorroTransferenciaOBJ ahorroTransferencia = new AhorroTransferenciaOBJ();
		try{
			AhorroAvisoRetiroOBJ ahorroAviso = new AhorroAvisoRetiroOBJ();
			if(req.getAvisoId() != null){
				ahorroAviso = ahorroavisodao.obtenerAvisoRetiroById(req.getAvisoId());
			}

			Integer ahorroPagareNumero = 0;
			ahorroPagareNumero = pdao.buscarMaxNumPagareByCuenta(req.getCuentaAhorro());

			AhorroMovimiento ahorroMovimiento = new AhorroMovimiento();
			ahorroMovimiento = mdao.buscarById(req.getMovtoId());
			
			//log.info("ANTES DE EVALUAR AHORRO MOVIMEINTO*+*+*+*+*+*+*+*+");
			if(ahorroMovimiento != null){
				if(ahorroMovimiento.getSalvoBuenCobro() != null){
					if(!ahorroMovimiento.getSalvoBuenCobro().equals("S")){
						//log.info("Actualiza saldo disposicion");
						//log.info("Fecha :: " + req.getFecha());
						//log.info("Cuenta :: " + req.getCuentaAhorro());
						//log.info("monto :: " + req.getMonto());
						//log.info("Operacion :: " + ahorroMovimiento.getOperacion());
						ahorroActualizaSaldoL.ahorroActualizasaldo(req.getFecha(), req.getCuentaAhorro(), req.getMonto(), ahorroMovimiento.getOperacion());
					}else{
						//log.info("Ahorro movimiento salvo buen cobro <> S +++++++++++++++++++++++++++");
					}
				}else{
					//log.info("Ahorro movimiento salvo buen cobro null +++++++++++++++++++++++++++");
					//log.info("Actualiza saldo disposicion");
					//log.info("Fecha :: " + req.getFecha());
					//log.info("Cuenta :: " + req.getCuentaAhorro());
					//log.info("monto :: " + req.getMonto());
					//log.info("Operacion :: " + ahorroMovimiento.getOperacion());
					ahorroActualizaSaldoL.ahorroActualizasaldo(req.getFecha(), req.getCuentaAhorro(), req.getMonto(), ahorroMovimiento.getOperacion());
				}
			}else{
				//log.info("Ahorro movimiento null +++++++++++++++++++++++++++");
			}

			
			movimientoCaja.setCajaId(req.getCajaId());
			movimientoCaja.setCajeroId(req.getUsuarioId());
			movimientoCaja.setFecha(req.getFecha());
			movimientoCaja.setTipoMovId(req.getMovtoId());
			movimientoCaja.setMonedaId(1);
			movimientoCaja.setMonto(req.getMonto());
			movimientoCaja.setCuenta(req.getCuentaAhorro());
			movimientoCaja.setFormaPago(req.getFormaPago());
			movimientoCaja.setCreadoPor(req.getUsuarioId());
			movimientoCaja.setModificadoPor(req.getUsuarioId());
			movimientoCaja.setBancoId(req.getBancoId());
			movimientoCaja.setCheque(req.getCheque());
			movimientoCaja.setObs(req.getObservacion());
			movimientoCaja.setEstatus("C");
			if(req.getTransaccionId() != null){
				movimientoCaja.setTransaccionId(req.getTransaccionId().toString());
			}
			if(req.getTarjetaOperativaId() != null){
				movimientoCaja.setTarjetaOperativaOd(req.getTarjetaOperativaId().toString());
			}
			if(ahorroAviso.getRegion() != null){
				movimientoCaja.setRegionId(ahorroAviso.getRegion());
			}

			movimientoCaja.setMovimientoId(mcdao.nuevo(movimientoCaja));

			if(ahorroPagareNumero > 0){
				AhorroPagare ahorroPagare = new AhorroPagare();
				ahorroPagare = pdao.buscarByCuentaNumero(req.getCuentaAhorro(), ahorroPagareNumero);
				Double montoNuevo = (double) 0;
				montoNuevo = (ahorroPagare.getMonto() - req.getMonto());
				ahorroPagare.setMonto(montoNuevo);
			}

			String tipoTrans = "C";
			if(req.getSpeiTransferenciaId() != null){
				if(req.getSpeiTransferenciaId() > 0){
					tipoTrans = "S";
				}
			}

			if(req.getMovtoId() != 37){
				ahorroDisposicion.setCuenta(req.getCuentaAhorro());
				ahorroDisposicion.setFecha(req.getFecha());
				ahorroDisposicion.setMonto(req.getMonto());
				ahorroDisposicion.setFormaPagoId(req.getFormaPago());
				ahorroDisposicion.setBancoId(req.getBancoId());
				ahorroDisposicion.setCheque(req.getCheque());
				ahorroDisposicion.setMovtoId(req.getMovtoId());
				ahorroDisposicion.setCreadoPor(req.getUsuarioId());
				if(req.getTransaccionId() != null){
					ahorroDisposicion.setTransaccionId(req.getTransaccionId());
				}
				if(req.getTarjetaOperativaId() != null){
					ahorroDisposicion.setTarjetaOperativaId(req.getTarjetaOperativaId());
				}
				if(req.getApp() != null){
					ahorroDisposicion.setApp(req.getApp());
				}
				if(req.getTransaccionVersionId() != null){
					ahorroDisposicion.setTransaccionVersionId(req.getTransaccionVersionId());
				}
				if(req.getAvisoId() != null){
					ahorroDisposicion.setAvisoId(req.getAvisoId());
				}
				ahorroDisposicion.setId(adispdao.nuevo(ahorroDisposicion));

				ahorroTransferencia.setMovimientoId(movimientoCaja.getMovimientoId());
				ahorroTransferencia.setDisposicionId(ahorroDisposicion.getId());
				ahorroTransferencia.setCuentaOrigen(req.getCuentaAhorro());
				ahorroTransferencia.setMonto(req.getMonto());
				if(req.getCheque() != null){
					if(!req.getCheque().trim().isEmpty()){
						//ESTA SECCION ES PARA CREDITOS DE PROCREA POR LO QUE NO SE EJECUTA
						//perform inserta_cheque (pcuenta, cast('H' as varchar), cast(pmonto as numeric(10,2)),pcheque,pbanco,puser,pfecha,p_banco_clie,tipo_trans,viddis,p_spei_transferencia_id);
						//InsertaChequeReqOBJ insertaChequeReq = new InsertaChequeReqOBJ();
						//insertaChequeLogic.insertaCheque(insertaChequeReq);
					}
				}
			}

			if(req.getAvisoId() != null){
				if(req.getAvisoId() > 0){
					ahorroAviso = ahorroavisodao.obtenerAvisoRetiroById(req.getAvisoId());
					if(ahorroAviso != null){
						if(ahorroAviso.getAvisoId() != null){
							if(ahorroAviso.getAvisoId() > 0){
								ahorroAviso.setMovimientoId(movimientoCaja.getMovimientoId());

								if(req.getBancoId() == 555){
									ahorroAviso.setEstatus("E");
								}
								ahorroAviso.setModificadoPor(req.getUsuarioId());
								ahorroavisodao.actualizaAvisoRetiro(ahorroAviso);
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		respuesta.setCodigo(0);
		respuesta.setMensaje("Deposito registrado");
		respuesta.setData(gson.toJson(ahorroTransferencia));
		return respuesta;
	}

	
	
		
}
