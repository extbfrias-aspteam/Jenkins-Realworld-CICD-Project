package net.cero.ahorro.logica;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;

import net.cero.data.AhorroContrato;
import net.cero.data.AhorroDeposito;
import net.cero.data.AhorroMovimiento;
import net.cero.data.AhorroPagare;
import net.cero.data.AhorroSaldos;
import net.cero.data.AhorroTransferenciaOBJ;
import net.cero.data.CajaDepositoAhorroReq;
import net.cero.data.MovimientosCaja;
import net.cero.data.Respuesta;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroContratoDAO;
import net.cero.spring.dao.AhorroDepositoDAO;
//import net.cero.spring.dao.AhorroIdeValoresDAO;
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
public class CajaDepositoAhorroLogic {
	private static final Logger log = LogManager.getLogger(CajaDepositoAhorroLogic.class);
	
	private static Apps apps = null;
	
	private static AhorroContratoDAO adao;
	private static AhorroPagareDAO pdao;
	private static AhorroMovimientosDAO mdao;
	private static MovimientosCajaDAO mcdao;
	private static AhorroDepositoDAO addao;
	private static AhorroSaldosDAO asdao;
	
	private static AhorroActualizaSaldoLogic ahorroActualizaSaldoL;
	
	private static Gson gson;
	//private static AhorroRendimientoVigenteDAO vdao;
	
	
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
			
			ahorroActualizaSaldoL = new AhorroActualizaSaldoLogic();
			
			gson = new Gson();
			//avdao = (AhorroIdeValoresDAO) s.getApplicationContext().getBean("AhorroIdeValoresDAO");
			//vdao = (AhorroRendimientoVigenteDAO) s.getApplicationContext().getBean("AhorroRendimientoVigenteDAO");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public Respuesta registrarDeposioto(CajaDepositoAhorroReq req){
		initialized();
		Respuesta respuesta = new Respuesta();
		Integer vMovimientoCaja = 0;
		
		AhorroContrato ahorroContrato = new AhorroContrato();
		ahorroContrato = adao.buscarByCuenta(req.getCuentaAhorro());
		
		AhorroPagare ahorroPagares = pdao.buscarByCuenta(req.getCuentaAhorro());
		
		Timestamp vFecha = new Timestamp(System.currentTimeMillis());
		
		AhorroMovimiento ahorroMovimiento = new AhorroMovimiento();
		ahorroMovimiento = mdao.buscarById(req.getMovtoId());
		
		AhorroDeposito ahorroDeposito = new AhorroDeposito();
		
		MovimientosCaja movimientoCajaResult = new MovimientosCaja();
		
		AhorroTransferenciaOBJ ahorroTransferencia = new AhorroTransferenciaOBJ();
		
		if(ahorroContrato.getSaldo() == 0){
			AhorroPagare ahorroPagaresNuevo = new AhorroPagare();
			ahorroPagaresNuevo.setCuenta(req.getCuentaAhorro());
			
			if(ahorroPagares != null){
				if(ahorroPagares.getNumero() != null){
					ahorroPagaresNuevo.setNumero(ahorroPagares.getNumero() + 1);
				}else{
					ahorroPagaresNuevo.setNumero((long) 1);
				}
			}else{
				ahorroPagaresNuevo.setNumero((long) 1);
			}
			
			ahorroPagaresNuevo.setMonto(req.getMonto());
			ahorroPagaresNuevo.setFechaInicio(req.getFecha());
			ahorroPagaresNuevo.setCreadoPor((int) req.getUsuarioId());
			ahorroPagaresNuevo.setFechaCreacion(vFecha);
			ahorroPagaresNuevo.setModificadoPor((int) req.getUsuarioId());
			ahorroPagaresNuevo.setFechaModificacion(vFecha);
			
			ahorroPagaresNuevo.setPagareId(pdao.nuevo(ahorroPagaresNuevo));
		}
		
		vMovimientoCaja = null;
		if(req.getParaConciliar() == 1){
			vMovimientoCaja = mcdao.obtenerMovimientoId(req.getCuentaAhorro(), req.getMonto(), req.getFecha());
		}
		
		if(vMovimientoCaja == null){
			MovimientosCaja movimientoCaja = new MovimientosCaja();
			movimientoCaja.setControl(req.getControl());
			movimientoCaja.setCajaId(req.getCajaId());
			movimientoCaja.setCajeroId((int) req.getUsuarioId());
			movimientoCaja.setFecha(req.getFecha());
			movimientoCaja.setTipoMovId(req.getMovtoId());
			movimientoCaja.setMonedaId(1);
			movimientoCaja.setMonto(req.getMonto());
			movimientoCaja.setCuenta(req.getCuentaAhorro());
			movimientoCaja.setFormaPago(req.getFormaPago());
			movimientoCaja.setObs(req.getObservacion());
			movimientoCaja.setBancoId(req.getBancoId());
			movimientoCaja.setBancoOrigen(req.getBancoId());
			movimientoCaja.setRegionId((int) req.getSucursalId());
			movimientoCaja.setCreadoPor((int) req.getUsuarioId());
			movimientoCaja.setModificadoPor((int) req.getUsuarioId());
			movimientoCaja.setEstatus("C");
			movimientoCaja.setFechaCreacion(vFecha);
			
			movimientoCaja.setMovimientoId(mcdao.nuevo(movimientoCaja));
			movimientoCajaResult = movimientoCaja;
			if(req.getMovtoId() == 2){
				ahorroDeposito.setDepositoId(movimientoCaja.getMovimientoId());
				ahorroDeposito.setCuenta(req.getCuentaAhorro());
				ahorroDeposito.setMonto(req.getMonto());
				ahorroDeposito.setFecha(vFecha);
				ahorroDeposito.setFormaPago(req.getFormaPago());
				ahorroDeposito.setBanco(req.getBancoId());
				ahorroDeposito.setNoCheque(req.getCheque());
				ahorroDeposito.setObservaciones(req.getObservacion());
				ahorroDeposito.setCreadoPor((int) req.getUsuarioId());
				ahorroDeposito.setFechaCreacion(vFecha);
				ahorroDeposito.setModificadoPor((int) req.getUsuarioId());
				ahorroDeposito.setFechaModificacion(vFecha);
			}
			
			if(ahorroMovimiento.getSalvoBuenCobro() == null){
				ahorroActualizaSaldoL.ahorroActualizasaldo(req.getFecha(),req.getCuentaAhorro(), req.getMonto(), ahorroMovimiento.getOperacion());
			}else{
				if(!ahorroMovimiento.getSalvoBuenCobro().equals("S")){
					ahorroActualizaSaldoL.ahorroActualizasaldo(req.getFecha(),req.getCuentaAhorro(), req.getMonto(), ahorroMovimiento.getOperacion());
				}
			}
		}else{
			MovimientosCaja movimientoCajaExistente = new MovimientosCaja();
			movimientoCajaExistente = mcdao.findMovimientoById(vMovimientoCaja);
			movimientoCajaExistente.setEstatus("C");
			movimientoCajaExistente.setBancoId(req.getBancoId());
			movimientoCajaExistente.setBancoOrigen(req.getBancoId());
			movimientoCajaExistente.setFechaDeposito(req.getFechaDeposito());
			movimientoCajaExistente.setModificadoPor((int) req.getUsuarioId());
			mcdao.actualizaMovimiento(movimientoCajaExistente);
			
			movimientoCajaResult = movimientoCajaExistente;
			if(ahorroMovimiento.getSalvoBuenCobro() == null){
				ahorroActualizaSaldoL.ahorroActualizasaldo(req.getFecha(),req.getCuentaAhorro(), req.getMonto(), ahorroMovimiento.getOperacion());
			}else{
				if(!ahorroMovimiento.getSalvoBuenCobro().equals("S")){
					ahorroActualizaSaldoL.ahorroActualizasaldo(req.getFecha(),req.getCuentaAhorro(), req.getMonto(), ahorroMovimiento.getOperacion());
				}
			}
		}
		
		if(req.getMovtoId() != 38){
			Double  vide = ahorroIde(req.getCuentaAhorro(),req.getFecha(),req.getFecha());
			
			if(vide > 0){
				MovimientosCaja movimientoCajaNuevo = new MovimientosCaja();
				movimientoCajaNuevo.setCajaId(req.getCajaId());
				movimientoCajaNuevo.setCajeroId((int) req.getUsuarioId());
				movimientoCajaNuevo.setFecha(vFecha);
				movimientoCajaNuevo.setTipoMovId(14);
				movimientoCajaNuevo.setMonedaId(1);
				movimientoCajaNuevo.setMonto(vide);
				movimientoCajaNuevo.setCuenta(req.getCuentaAhorro());
				movimientoCajaNuevo.setFormaPago(4);
				movimientoCajaNuevo.setCreadoPor((int) req.getUsuarioId());
				movimientoCajaNuevo.setFechaCreacion(vFecha);
				movimientoCajaNuevo.setModificadoPor((int) req.getUsuarioId());
				movimientoCajaNuevo.setFechaModificacion(vFecha);
				movimientoCajaNuevo.setObs("ide");
				movimientoCajaNuevo.setRegionId((int) req.getSucursalId());
				
				movimientoCajaNuevo.setMovimientoId(mcdao.nuevo(movimientoCajaNuevo));
				movimientoCajaResult = movimientoCajaNuevo;
				AhorroSaldos ahorroSaldos = new AhorroSaldos();
				ahorroSaldos = asdao.buscarByCuenta(req.getCuentaAhorro());
				ahorroSaldos.setSaldoReal((ahorroSaldos.getSaldoReal() - vide));
				ahorroSaldos.setSaldoDisponible((ahorroSaldos.getSaldoDisponible() - vide));
				
				asdao.actualizar(ahorroSaldos);
				
				ahorroContrato.setSaldo(ahorroContrato.getSaldo() - vide);
				adao.actualizar(ahorroContrato);
			}
			
		}
		
		ahorroTransferencia.setDepositoId(movimientoCajaResult.getMovimientoId());
		ahorroTransferencia.setCuentaDestino(req.getCuentaAhorro());
		
		respuesta.setCodigo(0);
		respuesta.setMensaje("Deposito registrado");
		respuesta.setData(gson.toJson(ahorroTransferencia));
		return respuesta;
	}

	private Double ahorroIde(String cuenta, Date fechaDesde, Date fechaHasta) {
		//Double total = (double) 0;
		Double valor = (double) 0;
		//Double ideAnt = (double) 0;
		
		return valor;
		/*		
		AhorroIdeValores ahorroIdeValores = new AhorroIdeValores();
		
		List<MovimientosCaja> movimientosCajaLista = mcdao.findByCuentaAndFechas(cuenta, fechaDesde, fechaHasta);
		for(MovimientosCaja movimiento : movimientosCajaLista){
			List<MovimientosCaja> movimientosCajaReferenciaLista = mcdao.findByReferenciaAndFechas(movimiento.getMovimientoId(), fechaDesde, fechaHasta);
			for(MovimientosCaja movimientoReferencia : movimientosCajaReferenciaLista){
				if(movimiento.getCuenta() == null){
					total += movimiento.getMonto();
				}
			}
		}
		
		ahorroIdeValores = avdao.findByFechas(fechaDesde, fechaHasta);
		if(ahorroIdeValores.getMonto() == null){
			valor = (double) 0;
		}else{
			total = total - ahorroIdeValores.getMonto();
			if( total > 0 ){
				 valor = (total * (ahorroIdeValores.getPorcentaje()/100));
			}else{
				 valor = (double) 0;
			}
		}
		
		ideAnt = mcdao.obtenerIdeAnterior(cuenta, fechaDesde, fechaHasta);
		if(ideAnt == null){
			ideAnt = (double) 0;
		}
		if(ideAnt > 0 ){
			valor = (valor - ideAnt);
		}
		
		return valor;*/
	}

	/*private void ahorroActualizasaldo(Date fecha, String cuenta, Double monto, String operacion) {
		Boolean continua;
		Boolean actualiza;
		
		
		AhorroSaldosOBJ saldos = new AhorroSaldosOBJ();
		AhorroContratoOBJ ahorroContrato = new AhorroContratoOBJ();
		AhorroRendimientoVigentesOBJ ahorroRendimientoVigentes = new AhorroRendimientoVigentesOBJ();
		
		
		ahorroContrato = adao.buscarByCuenta(cuenta);		
		
		saldos = asdao.buscarByCuenta(cuenta);
		continua = false;
		if(saldos != null)
			if(saldos.getCuenta() != null)
				continua = true;
		
		if((!continua) && operacion.equals("+")){
			saldos = new AhorroSaldosOBJ();
			saldos.setCuenta(cuenta);
			saldos.setSolicitanteId(ahorroContrato.getSolicitante());
			saldos.setSaldoReal(monto);
			saldos.setSaldoPromedio((double) 0);
			saldos.setSaldoAcumulado((double) 0);
			saldos.setDias(0);
			saldos.setIntereses((double) 0);
			saldos.setIva((double) 0);
			saldos.setIsr((double) 0);
			saldos.setRetenciones((double) 0);
			saldos.setSaldoDisponible(monto);
			saldos.setFechaDeposito(fecha);
			saldos.setAhorroSaldosId(asdao.nuevo(saldos));
			ahorroContrato.setSaldo(ahorroContrato.getSaldo() + monto);

			actualiza = adao.actualizar(ahorroContrato);
			
			ahorroRendimientoVigentes = vdao.buscarByCuenta(cuenta);
			if(ahorroRendimientoVigentes.getEstatus().equals("P")){
				ahorroRendimientoVigentes.setFechaDeposito(fecha);
				ahorroRendimientoVigentes.setCapital(monto);
				ahorroRendimientoVigentes.setFechaInicio(fecha);
				Date fechaFinal = diaHabil(fecha, ahorroRendimientoVigentes.getPlazo(), "D");
				ahorroRendimientoVigentes.setFechaFinal(fechaFinal);
				ahorroRendimientoVigentes.setEstatus("A");
				
				vdao.actualizar(ahorroRendimientoVigentes);
			}
		}else{
			if(operacion.equals("+")){
				ahorroContrato.setSaldo(ahorroContrato.getSaldo() + monto);
				actualiza = adao.actualizar(ahorroContrato);
				
				saldos.setSaldoDisponible(saldos.getSaldoDisponible() + monto);
				saldos.setSaldoReal(saldos.getSaldoReal() + monto);
				saldos.setSaldoAcumulado(saldos.getSaldoAcumulado() + monto);
				asdao.actualizar(saldos);
				
				ahorroRendimientoVigentes = vdao.buscarByCuenta(cuenta);
				if(ahorroRendimientoVigentes.getEstatus() != null){
					if(ahorroRendimientoVigentes.getEstatus().equals("P")){
						ahorroRendimientoVigentes.setFechaDeposito(fecha);
						ahorroRendimientoVigentes.setCapital(monto);
						ahorroRendimientoVigentes.setFechaInicio(fecha);
						Date fechaFinal = diaHabil(fecha, ahorroRendimientoVigentes.getPlazo(), "D");
						ahorroRendimientoVigentes.setFechaFinal(fechaFinal);
						ahorroRendimientoVigentes.setEstatus("A");
						
						vdao.actualizar(ahorroRendimientoVigentes);
					}
				}
			}else{
				ahorroContrato.setSaldo(ahorroContrato.getSaldo() - monto);
				adao.actualizar(ahorroContrato);
				
				saldos.setSaldoDisponible(saldos.getSaldoDisponible() - monto);
				saldos.setSaldoReal(saldos.getSaldoReal() - monto);
				saldos.setSaldoAcumulado(saldos.getSaldoAcumulado() - monto);
				asdao.actualizar(saldos);
			}
		}
	}

	private Date diaHabil(Date fecha, Integer plazo, String string) {
		Calendar calendar = Calendar.getInstance();
		List<Date> fechasInhabiles = new ArrayList<Date>();
		calendar.setTime(fecha);
		Boolean continua = false;
		while(!continua){
			if(calendar.get(calendar.DAY_OF_WEEK) == 6){
				calendar.add(Calendar.DAY_OF_YEAR, (2));
			}else if(calendar.get(calendar.DAY_OF_WEEK) == 0){
				calendar.add(Calendar.DAY_OF_YEAR, (1));
			}
			if(fechasInhabiles.contains(calendar.getTime())){
				calendar.add(Calendar.DAY_OF_YEAR, (1));
			}else{
				continua = true;
			}
		}
		return calendar.getTime();
	}*/
	
	
}
