package net.std.sftp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.request.AltaDocumentoReq;



public class SFTPLogic {
	private static final Logger log = LogManager.getLogger(SFTPLogic.class);
    
    public static RespuestaSVC procesar(AltaDocumentoReq req) {
    	String obs = "OK";
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		String nombreArchivoFecha = "";
        try {
            SFTPConnector sshConnector = new SFTPConnector();
            sshConnector.connect(Constantes.SFTP_USER, Constantes.SFTP_PASS, Constantes.SFTP_HOST, Constantes.SFTP_PORT);

			byte[] backToBytes = Base64.decodeBase64(req.getDocumento());
			 
		    InputStream is = new ByteArrayInputStream(backToBytes);
		    
		    
		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat f = new SimpleDateFormat("YYYYMMdd");
		    nombreArchivoFecha = f.format(cal.getTime()) + "_" + req.getNombreArchivo() + "." + req.getTipoArchivo(); 
		    
            sshConnector.addFile(Constantes.SFTP_RUTA, is,req.getRutaArchivo(),nombreArchivoFecha);
            sshConnector.disconnect();
        } catch (JSchException ex) {
            ex.printStackTrace();
            obs = ex.getMessage();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            obs = ex.getMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
            obs = ex.getMessage();
        } catch (SftpException ex) {
            ex.printStackTrace();
            obs = ex.getMessage();
        }
        if(obs.equals("")){
        	obs = "ERROR";
        }
        if(obs.equals("OK")){
			respuestaSvc.getBody().addValor("ESTATUS", obs);
			respuestaSvc.getBody().addValor("RUTA_ARCHIVO", Constantes.SFTP_RUTA + req.getRutaArchivo() + "/" + nombreArchivoFecha);
    	}
		else{
			respuestaSvc.getErrores().addCodigo("ESTATUS", Errores.ERROR_INESPERADO);
		}
        return respuestaSvc;
    }
}	


