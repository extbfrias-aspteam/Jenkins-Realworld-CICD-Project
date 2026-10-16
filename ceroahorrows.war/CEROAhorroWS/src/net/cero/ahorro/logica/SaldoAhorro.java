/*
 * @(#)SaldoAhorro.java 1.0 09/24/18
 * 
 */
package net.cero.ahorro.logica;


import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import net.cero.data.AhorroContrato;
import net.cero.data.AhorroRendimientoVigentes;
import net.cero.data.AhorroSaldos;
import net.cero.data.Respuesta;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroContratoDAO;
import net.cero.spring.dao.AhorroGarantiasDAO;
import net.cero.spring.dao.AhorroRendimientoDAO;
import net.cero.spring.dao.AhorroRendimientoVigenteDAO;
import net.cero.spring.dao.AhorroRenovacionesDAO;
import net.cero.spring.dao.AhorroRetencionesDAO;
import net.cero.spring.dao.AhorroSaldosDAO;



/**
 * Logica de negocio para obtener el saldo de ahorro.
 * @author Israel
 * @version 1.0 09/24/18 
 */
public class SaldoAhorro {
	private static final Logger log = LogManager.getLogger(SaldoAhorro.class);
	
	private static Apps apps = null;
	
	private static AhorroContratoDAO adao;
	private static AhorroSaldosDAO asdao;
	private static AhorroRendimientoVigenteDAO vdao;
	private static AhorroRendimientoDAO ardao;
	private static AhorroRenovacionesDAO aredao;
	private static AhorroGarantiasDAO agdao;
	private static AhorroRetencionesDAO aretdao;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			adao = (AhorroContratoDAO) s.getApplicationContext().getBean("AhorroContratoDAO");
			asdao = (AhorroSaldosDAO) s.getApplicationContext().getBean("AhorroSaldosDAO");
			vdao = (AhorroRendimientoVigenteDAO) s.getApplicationContext().getBean("AhorroRendimientoVigenteDAO");
			ardao = (AhorroRendimientoDAO) s.getApplicationContext().getBean("AhorroRendimientoDAO");
			aredao = (AhorroRenovacionesDAO) s.getApplicationContext().getBean("AhorroRenovacionesDAO");
			agdao = (AhorroGarantiasDAO) s.getApplicationContext().getBean("AhorroGarantiasDAO");
			aretdao = (AhorroRetencionesDAO) s.getApplicationContext().getBean("AhorroRetencionesDAO");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public Respuesta consultaSaldoAhorro(String cuentaAhorro){
		initialized();
		Double vSaldo = (double) 0;
		Respuesta respuesta = new Respuesta();
		AhorroContrato ahorroContrato = new AhorroContrato();
		ahorroContrato = adao.buscarByCuenta(cuentaAhorro);
		
		String tipoRendimiento = ahorroTipocuenta(ahorroContrato.getRendimientoId()); 
		
		if(!tipoRendimiento.equals("S")){			
			respuesta.setCodigo(0);
			respuesta.setMensaje("saldo ahorro");
			respuesta.setData(vSaldo.toString());
			return respuesta;
		}
		
		Boolean vVence = ahorroVence(ahorroContrato, new Date());
		if(!vVence){
			respuesta.setCodigo(0);
			respuesta.setMensaje("saldo ahorro");
			respuesta.setData(vSaldo.toString());
			return respuesta;
		}else{
			vSaldo = ahorroSaldodisponible(ahorroContrato);
		}
		
		respuesta.setCodigo(0);
		respuesta.setMensaje("saldo ahorro");
		respuesta.setData(vSaldo.toString());
		return respuesta;
	}
	
	
	private String ahorroTipocuenta(Integer rendimientoId){		
		String tipoRendimiento = ""; 
		try{
			tipoRendimiento = ardao.ahorroTipocuenta(rendimientoId);
		}catch(Exception e){
			log.error("se presento un problema al obtener el tipo de cuenta :: " + e.getMessage());
			return "";
		}
		return tipoRendimiento;
	}
	
	private Boolean ahorroVence(AhorroContrato ahorroContrato, Date pFecha){
		Boolean vBan = false;
		Calendar calendar = Calendar.getInstance();
		
		try{
			
			Date vFecha = ahorroContrato.getFechaApertura();
			Integer vTipoAhorro = ahorroContrato.getTipoAhorroId();
			if(vFecha == null){
				vBan = false;
			}else{
				Date vFechaRen = aredao.ultimaFechaRenovacion(ahorroContrato.getCuenta());
				if(vFechaRen != null){
					vFecha = vFechaRen;
				}
				AhorroRendimientoVigentes ahorroRendimientoVigentes = new AhorroRendimientoVigentes();
				ahorroRendimientoVigentes = vdao.buscarByCuenta(ahorroContrato.getCuenta());
				if(ahorroRendimientoVigentes.getPlazo() != null && (ahorroRendimientoVigentes.getPlazo() == 0 || vTipoAhorro == 2)){
					vBan = true;
				}else{
					if(ahorroRendimientoVigentes.getTipoCorte().equals("V")){
						while(vFecha.before(pFecha)){
							calendar.setTime(vFecha); 
							calendar.add(Calendar.DATE, ahorroRendimientoVigentes.getPlazo());
							vFecha = calendar.getTime();
						}
					}else{
						calendar.setTime(vFecha); 
						calendar.add(Calendar.MONTH, (ahorroRendimientoVigentes.getPlazo() / 30));
						vFecha = calendar.getTime();
					}
					calendar.setTime(vFecha); 
					calendar.add(Calendar.DATE, (ahorroRendimientoVigentes.getDiasGracia()));
					if ((pFecha.after(vFecha)) && (pFecha.before((Date) calendar.getTime()))){
						vBan = true;
					}
				}				
			}
		}catch(Exception e){
			log.error("se presento un problema al obtener el ahorro vence :: " + e.getMessage());
			return false;
		}
		
		return vBan;
	}
	
	private Double ahorroSaldodisponible(AhorroContrato ahorroContrato){
		Double vSaldo = (double) 0;
		Date vFecha;
		AhorroSaldos ahorroSaldos = new AhorroSaldos();
		try{
			ahorroSaldos = asdao.buscarByCuenta(ahorroContrato.getCuenta());
			vFecha = ahorroSaldos.getFechaCorte();
			if(vFecha == null){
				vSaldo = ahorroContrato.getSaldo();
			}else{
				vSaldo = ahorroSaldos.getSaldoDisponible();
			}
			
			Double vRetiros = (double) 0;
			Double vGarantias = (double) 0;
			vGarantias = agdao.montoGarantia(ahorroContrato.getCuenta());
			if(vGarantias == null){
				vGarantias = (double) 0;
			}
			
			Double vRetenciones = (double) 0;
			vRetenciones = aretdao.montoRetenciones(ahorroContrato.getCuenta());
			
			vSaldo = vSaldo - vRetiros - vGarantias - vRetenciones;
			if(vSaldo < 0){
				vSaldo = (double) 0;
			}			
		}catch(Exception e){
			log.error("se presento un problema al obetener el saldo :: " + e.getMessage());
			return (double) 0;
		}
		
		return vSaldo;		
	}
}
