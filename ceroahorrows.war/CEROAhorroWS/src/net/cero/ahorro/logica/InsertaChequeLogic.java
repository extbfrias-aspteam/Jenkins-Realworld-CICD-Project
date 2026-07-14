package net.cero.ahorro.logica;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import net.cero.data.CajaDisposicionAhorroReq;
import net.cero.data.InsertaChequeReqOBJ;
import net.cero.data.MinistraOBJ;
import net.cero.data.Respuesta;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroAvisosRetiroDAO;
import net.cero.spring.dao.AhorroDisposicionesDAO;
import net.cero.spring.dao.AhorroMovimientosDAO;
import net.cero.spring.dao.AhorroPagareDAO;
import net.cero.spring.dao.CatinstiDAO;
import net.cero.spring.dao.MinistraDAO;
import net.cero.spring.dao.MovimientosCajaDAO;

public class InsertaChequeLogic {
	private static final Logger log = LogManager.getLogger(InsertaChequeLogic.class);
	
	private static Apps apps = null;
	
	//private static AhorroContratoDAO adao;
	private static AhorroPagareDAO pdao;
	private static AhorroMovimientosDAO mdao;
	private static AhorroDisposicionesDAO adispdao;
	private static MovimientosCajaDAO mcdao;
	private static MinistraDAO mindao;
	private static CatinstiDAO catinstidao;
	//private static AhorroDepositoDAO addao;
	//private static AhorroSaldosDAO asdao;
	//private static AhorroRendimientoVigenteDAO vdao;
	private static AhorroAvisosRetiroDAO ahorroavisodao;
	
	private static AhorroActualizaSaldoLogic ahorroActualizaSaldoL;
	
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
			mindao = (MinistraDAO) s.getApplicationContext().getBean("MinistraDAO");
			catinstidao = (CatinstiDAO) s.getApplicationContext().getBean("CatinstiDAO");
			//addao = (AhorroDepositoDAO) s.getApplicationContext().getBean("AhorroDepositoDAO");
			//asdao = (AhorroSaldosDAO) s.getApplicationContext().getBean("AhorroSaldosDAO");
			//vdao = (AhorroRendimientoVigenteDAO) s.getApplicationContext().getBean("AhorroRendimientoVigenteDAO");
			ahorroavisodao = (AhorroAvisosRetiroDAO) s.getApplicationContext().getBean("AhorroAvisosRetiroDAO");
			adispdao = (AhorroDisposicionesDAO) s.getApplicationContext().getBean("AhorroDisposicionesDAO");
			
			ahorroActualizaSaldoL = new AhorroActualizaSaldoLogic();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public Respuesta insertaCheque(InsertaChequeReqOBJ req){
		initialized();
		Respuesta respuesta = new Respuesta();
		try{
			String wfolio = "";
			String wwChGrupo = "";
			String wwGrupo = "";
			Integer wnumMinistracion = 0;
			Integer vChProductoId = 0;
			Integer vpBancoClie = req.getBancoClie();
			
			if( req.getTipoOperacion().equals("C")){
				vpBancoClie = 0;
			}
			
			MinistraOBJ ministra = new MinistraOBJ();
			ministra = mindao.obtenerMinistraByControlFechaMin(req.getControl(), req.getFecha());
			if(ministra != null){
				if(ministra.getNumero() == 0){
					wnumMinistracion = 1;
				}else{
					wnumMinistracion = ministra.getNumero();
				}
			}else{
				wnumMinistracion = 1;
			}
			
			catinstidao.actualizaSaldoNullo((double) 0, req.getBancoId());
			
			if(req.getTipoCheque().equals("CC")){
				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		respuesta.setCodigo(0);
		respuesta.setMensaje("Deposito registrado");
		return respuesta;
	}

	
	
		
}
