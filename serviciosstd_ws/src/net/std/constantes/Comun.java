package net.std.constantes;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.google.gson.Gson;

import net.cero.ws.data.RespuestaSVC;

public class Comun implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(Comun.class);
	
	public static String _T(Object obj){
		return obj == null ? "" : obj.toString();
	}
	
	public static String _TX(Object obj){
		return obj == null ? "" : (obj.toString()).trim();
	}
	
	public static Integer _I(Object obj) {
		return obj == null ? null : Integer.parseInt(obj.toString());
	}
	
	public static Integer _IX(Object obj) {
		return obj == null ? 0 : Integer.parseInt(obj.toString());
	}
	
	public static Long _L(Object obj) {
		return obj == null ? null : Long.parseLong(String.valueOf(obj.toString()));
	}
	
	public static Double _D(Object valor) {
		return valor == null ? 0.0d : new Double(String.valueOf(valor));
	}
	
	public static Double _DNull(Object valor) {
		return valor == null ? null : new Double(String.valueOf(valor));
	}
	
	public static BigDecimal _BD(Object valor) {
		return valor == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(valor));
	}
	
	public static String _TN(Object obj){
		return obj == null ? null : obj.toString();
	}
	
	public static String _SError(Integer codigo, String texto){
		return "".equals(_T(texto)) || codigo == null  ? "" : String.format("ERROR : %d , %s", codigo, texto);
	}
	
	public static Boolean _B(Object obj){
		return obj == null ? null : Boolean.valueOf(obj.toString());
	}
	
	public static ResponseEntity<String> Response(RespuestaStd std, HttpStatus estatus){
		try{
			return new ResponseEntity<>(new Gson().toJson(std), estatus);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ResponseEntity<>(estatus);
	}
	
	public static RespuestaSVC RespError(Object codigo, String descripcion){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		log.info(String.format("%s - %s", (String)codigo, descripcion));
		try{
			respuestaSvc.getErrores().addCodigo("GENERICO", (Long)codigo, descripcion);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return respuestaSvc;
	}
	
	public static RespuestaSVC RespOK(String llave, Object dato){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		log.info(String.format("%s - %s", llave, (String)dato));
		try{
			respuestaSvc.getBody().addValor(llave, dato);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return respuestaSvc;
	}
	

	public static String _Dec(Object obj, int pos, boolean redondear){
		return String.format("%,.2f", obj == null ? BigDecimal.ZERO : new BigDecimal(_T(obj)).setScale(pos, redondear ? RoundingMode.HALF_EVEN: RoundingMode.FLOOR));
	}
	
	public static String _Fecha(Date fecha, String formato){
		return fecha == null ? "" : new SimpleDateFormat(formato).format(fecha);
	}
}
