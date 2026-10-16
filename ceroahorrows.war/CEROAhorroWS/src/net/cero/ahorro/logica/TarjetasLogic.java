package net.cero.ahorro.logica;

import java.util.ArrayList;
import java.util.List;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.data.Respuesta;

import net.cero.data.EmpresaOBJ;
import net.cero.data.TarjetaOBJ;
import net.cero.spring.dao.TarjetaDAO;
import net.cero.spring.dao.EmpresaComboDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * logica de negocio para consultar el stock de tarjetas
 * 
 * @author ICORPTTI
 * @version 1.0 25/02/2023
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class TarjetasLogic {
	private static Gson gson = new Gson();
	private final TarjetaDAO tDAO;
	private final EmpresaComboDAO eDAO;

	public Respuesta obtenerListadoTarjetas(int empresaId) {
		Respuesta respuesta =  new Respuesta();
		try{
			
			List<TarjetaOBJ> valores = new ArrayList<>();
			valores = tDAO.obtenerListadoTarjetas(empresaId);
			
			if(valores == null || valores.isEmpty()){
				respuesta.setCodigo(1);
				respuesta.setMensaje("Sin resultados");
				return respuesta;
			}

			valores.parallelStream().forEach(tarjeta -> {
				if(tarjeta != null && !StringUtils.isBlank(tarjeta.getPan()) && tarjeta.getPan().length() > 6)
				{
					tarjeta.setPan(StringUtils.repeat("*",6)
							.concat(tarjeta.getPan().substring(6)));
				}
			});
			
			respuesta.setCodigo(0);
			respuesta.setMensaje("OK");
			respuesta.setData(gson.toJson(valores));
		
		}catch(Exception e){
			log.error("Ocurrió un error dentro del metodo consultaStockTarjetas",e);
			respuesta.setCodigo(-1);
			respuesta.setMensaje("Ocurrió un error inesperado");
		}
		return respuesta;
	}
	
	
	public Respuesta obtenerEmpresaCombo(){
		Respuesta respuesta =  new Respuesta();
		try{
			List<EmpresaOBJ> valores = new ArrayList<>();
			valores = eDAO.obtenerEmpresaCombo();

			if(valores == null || valores.isEmpty()){
				respuesta.setCodigo(1);
				respuesta.setMensaje("Sin resultados");
				return respuesta;
			}		
			
			respuesta.setCodigo(0);
			respuesta.setMensaje("OK");
			respuesta.setData(gson.toJson(valores));


		}catch(Exception e){
			log.error("Ocurrió un error dentro del metodo obtenerEmpresaCombo",e);
			respuesta.setCodigo(-1);
			respuesta.setMensaje("Ocurrió un error inesperado");
		}
		return respuesta;
	}
	
}//Fin clase
