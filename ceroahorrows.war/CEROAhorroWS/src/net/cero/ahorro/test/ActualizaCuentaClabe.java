package net.cero.ahorro.test;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.logica.GenerarClabeLogic;
import net.cero.data.AhorroContrato;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroContratoDAO;

@Log4j2
public class ActualizaCuentaClabe {
	public static void main(String[] args) {			
		actualizaCuentaClabe();
	}
	
	private static Apps apps = null;
	private static AhorroContratoDAO adao;
	
	private static void initialized() {
		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			adao = (AhorroContratoDAO) s.getApplicationContext().getBean("AhorroContratoDAO");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void actualizaCuentaClabe() {
		initialized();
		try {
			List<AhorroContrato> listaCuentas = new ArrayList<AhorroContrato>();
			listaCuentas = llenaListaCuentas();
			for(AhorroContrato ahorroContrato : listaCuentas) {
				//log.info("Cuenta en proceso :: " + ahorroContrato.getCuenta());
				String cuentaClabe = generaCuentaClabe(ahorroContrato);
				//log.info("Cuenta clabe generada :: " + cuentaClabe);
				log.info("update ahorro_contrato set cuenta_clabe = '" + cuentaClabe + "' where cuenta = '" + ahorroContrato.getCuenta() + "';");
				//log.info("============================================================");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String generaCuentaClabe(AhorroContrato ahorroContratoNuevo) {
		try {
			GenerarClabeLogic generaClabe = new GenerarClabeLogic();
			String cuentaClabe = "";
			cuentaClabe = generaClabe.generarClabe(ahorroContratoNuevo.getReferencia(),
					ahorroContratoNuevo.getTipoAhorroId(), ahorroContratoNuevo.getSucursalApertura());
			return cuentaClabe;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static List<AhorroContrato> llenaListaCuentas() {
		List<AhorroContrato> listaCuentas = new ArrayList<AhorroContrato>();
		try {
			listaCuentas = adao.buscarCuentasSimplificadasAll();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return listaCuentas;
	}
}
