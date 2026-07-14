package net.cero.ahorro.logica;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;

import net.cero.data.IneOcrReqOBJ;
import net.cero.data.ResponseService;
import net.cero.data.ValoresPropertiesOBJ;
import net.cero.promesi.AuthHeadersRequest;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AgenteDAO;
import net.cero.spring.dao.AhorroConceptosDAO;
import net.cero.spring.dao.AhorroContratoDAO;
import net.cero.spring.dao.AhorroContratoDatosDAO;
import net.cero.spring.dao.AhorroSaldosDAO;
import net.cero.spring.dao.AuditoriaDAO;
import net.cero.spring.dao.CampaniaDAO;
import net.cero.spring.dao.CanalesDAO;
import net.cero.spring.dao.ColoniaDAO;
import net.cero.spring.dao.DirectorioTelefonicoDAO;
import net.cero.spring.dao.PINDAO;
import net.cero.spring.dao.ParametrosGeneralesDAO;
import net.cero.spring.dao.RegionesDAO;
import net.cero.spring.dao.RegistroCodiDAO;
import net.cero.spring.dao.RegistroServiciosDigitalesDAO;
import net.cero.spring.dao.SolicitanteDAO;
import net.cero.spring.dao.ValoresPropertiesInicialesDAO;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CuentaAhorroEstadoSwitchLogic {
	private static Apps apps = null;
	private static ValoresPropertiesInicialesDAO vDao;

	private static void initialized() {
		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			vDao =  (ValoresPropertiesInicialesDAO) s.getApplicationContext().getBean("ValoresPropertiesInicialesDAO");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResponseService obtenerDatosIne() {
		initialized();
		ResponseService respuesta = new ResponseService();
		try{
			ValoresPropertiesOBJ valore = new ValoresPropertiesOBJ();
			
			valore = vDao.obtenerEstatusSwitchOCR();
			
			if(valore == null) {
				respuesta.setCode(-1);
				respuesta.setMenssage("ERROR");
				
				return respuesta;
			}
			respuesta.setCode(0);
			respuesta.setMenssage("ok");
			respuesta.setData(valore.getValor());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return respuesta;
	}
}
