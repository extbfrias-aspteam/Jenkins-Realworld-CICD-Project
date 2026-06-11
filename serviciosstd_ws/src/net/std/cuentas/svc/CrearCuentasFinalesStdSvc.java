package net.std.cuentas.svc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.data.AltaCuentasOBJ;
import net.std.data.DatosMatrizRiesgoOBJ;
import net.std.data.DatosPldOBJ;
import net.std.request.AltaCuentasReq;
import net.std.request.SolicitanteReq;
import net.std.response.AltaCuentasRes;
import net.std.servicios.ProcesoBitLogger;

@Controller
public class CrearCuentasFinalesStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CrearCuentasFinalesStdSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value="/crearCuentasFinalesStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		AltaCuentasReq req = null;
		List<AltaCuentasRes> lstResultado = null;
		SolicitanteReq altaSol = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));

		try{
			req = new Gson().fromJson(json, AltaCuentasReq.class);
			if(req == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS), HttpStatus.BAD_REQUEST);
				return response;
			}
		}catch(Exception ex){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
			return response;
		}

		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_PERMISO), HttpStatus.UNAUTHORIZED);
			return response;
		}

		try{
			/* DESENCRIPTA EL LISTADO DE CUENTAS POR DAR DE ALTA PARA PROCESARLAS UNA A UNA */
			String jsondecoded = new Gson().fromJson(req.getCuentas(), String.class);
			log.info(jsondecoded);

			byte[] bytedec = Base64.getDecoder().decode(jsondecoded.getBytes());
			InputStream is = new ByteArrayInputStream(bytedec);
			List<AltaCuentasOBJ> list = parseCSVWithHeader(is);
			
			if(list != null){
				for(AltaCuentasOBJ ctaAlta : list){
					ProcesoBitLogger.procesar(Constantes.PROCESO, "ALTA CUENTAS", new Gson().toJson(ctaAlta));

					altaSol = new SolicitanteReq(ctaAlta);
					/* 
					 * VALORES POR DEFAULT DE PLD 
					 */
					
					if(altaSol.getPld() == null){
						altaSol.setPld(getPldGeneral());
					}
					
					if(altaSol.getLstMatriz() == null){
						altaSol.setLstMatriz(getListMRGeneral());
					}
					
					
					RespuestaSVC respCtas = ProcesoAltaCuentasStd.crearCuentaCeroStd(altaSol);
					if(lstResultado == null) lstResultado = new ArrayList<>();

					if(respCtas.getErrores().getCodigoError() == 0L){
						lstResultado.add(new AltaCuentasRes(respCtas.getErrores().getCodigoError(), 
															altaSol.getSolicitante().getNombreCompleto(),
															altaSol.getSolicitante().getRfc(), 
															altaSol.getSolicitante().getCurp(), 
															(Map<String,String>)respCtas.getBody().getValor("RESULTADO")));
								
					}else{
						lstResultado.add(new AltaCuentasRes(respCtas.getErrores().getCodigoError(), 
								                            altaSol.getSolicitante().getNombreCompleto(),
															altaSol.getSolicitante().getRfc(), 
															altaSol.getSolicitante().getCurp(), 
															respCtas.getErrores().getDescError()));
					}
				}
			}
			
			response = new ResponseEntity<>(new Gson().toJson(lstResultado), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}

		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}

	public static List<AltaCuentasOBJ> parseCSVWithHeader(InputStream is) { 
		List<AltaCuentasOBJ> lstCuentas = null;
		CSVReader reader = null;

		try{
			reader = new CSVReader(new InputStreamReader(is), ';');
			HeaderColumnNameMappingStrategy<AltaCuentasOBJ> beanStrategy = new HeaderColumnNameMappingStrategy<AltaCuentasOBJ>();
			beanStrategy.setType(AltaCuentasOBJ.class);

			CsvToBean<AltaCuentasOBJ> csvToBean = new CsvToBean<AltaCuentasOBJ>();
			lstCuentas = csvToBean.parse(beanStrategy, reader);
			reader.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}

		return lstCuentas;
	}
	
	private DatosPldOBJ getPldGeneral(){
		DatosPldOBJ pld = new DatosPldOBJ();
		pld.setIngreso_mensual(Comun._D(Constantes.PLD_INGRESO_MENSUAL));
		pld.setMonto_maximo_ahorro(Comun._D(Constantes.PLD_MONTO_MAXIMO_AHORRO));
		pld.setPuesto(Comun._T(Constantes.PLD_PUESTO));
		return pld;
	}
	
	private List<DatosMatrizRiesgoOBJ> getListMRGeneral(){
		List<DatosMatrizRiesgoOBJ> lstMR = new ArrayList<>();
		DatosMatrizRiesgoOBJ mr = new DatosMatrizRiesgoOBJ();
		mr.setTipo("ACT");
		mr.setClave(Comun._T(Constantes.MR_ACT_CLAVE));
		mr.setDescripcion(Comun._T(Constantes.MR_ACT_DESCRIPCION));
		lstMR.add(mr);
		
		mr = new DatosMatrizRiesgoOBJ();
		mr.setTipo("GIR");
		mr.setClave(Comun._T(Constantes.MR_GIR_CLAVE));
		mr.setDescripcion(Comun._T(Constantes.MR_GIR_DESCRIPCION));
		lstMR.add(mr);
		
		mr = new DatosMatrizRiesgoOBJ();
		mr.setTipo("LOC");
		mr.setClave(Comun._T(Constantes.MR_LOC_CLAVE));
		mr.setDescripcion(Comun._T(Constantes.MR_LOC_DESCRIPCION));
		lstMR.add(mr);
		
		mr = new DatosMatrizRiesgoOBJ();
		mr.setTipo("OCU");
		mr.setClave(Comun._T(Constantes.MR_OCU_CLAVE));
		mr.setDescripcion(Comun._T(Constantes.MR_OCU_DESCRIPCION));
		lstMR.add(mr);
		
		mr = new DatosMatrizRiesgoOBJ();
		mr.setTipo("DES");
		mr.setClave(Comun._T(Constantes.MR_DES_CLAVE));
		mr.setDescripcion(Comun._T(Constantes.MR_DES_DESCRIPCION));
		lstMR.add(mr);
		
		return lstMR;
	}
}

