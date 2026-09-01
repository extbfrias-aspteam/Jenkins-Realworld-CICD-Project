package net.cero.ahorro.logica;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import net.cero.data.GeneraContratoServElecReq;
import net.cero.data.GeneraDisposicionesLegales;
import net.cero.data.GeneraRegistroContratoReq;
import net.cero.data.GeneraReporteContratoReq;
import net.cero.data.GeneraReporteTarjetaReq;
import net.cero.spring.config.Apps;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Log4j2
public class GeneraReporte {

	private static Apps apps = null;
	private static DriverManagerDataSource ds = null;
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			ds = (DriverManagerDataSource) s.getApplicationContext().getBean("dsPr");

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public byte[] generaTarjetaAhorro(GeneraReporteTarjetaReq req) {
		initialized();
		byte[] pdfBytes;
		Map<String,Object> params = new HashMap<>();
		ResourceBundle path = ResourceBundle.getBundle("path", new Locale("es", "Mx"));		
		String rutaReporte = path.getString("value");
		
		params.put("cuenta", req.getCuentaAhorro());
		params.put("accesoId", req.getAccesoId());
		
		params.put("pRutaimagen", rutaReporte+"/Reportes/img/");
		
		
		try {
			JasperPrint print = JasperFillManager.fillReport(rutaReporte+"/Reportes/TarjetaAhorroSimplificada.jasper", params,ds.getConnection());
			
			pdfBytes = JasperExportManager.exportReportToPdf(print);
		
		} catch (JRException e) {
			log.error("ERROR AL GENERAR REPORTE TARJETA DE AHORRO CUENTA SIMPLIFICADA");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return pdfBytes;
		
	}
	
	public byte[] generaContratoServiciosElectronicos(GeneraReporteContratoReq req) {
		initialized();
		byte[] pdfBytes;
		Map<String,Object> params = new HashMap<>();
		ResourceBundle path = ResourceBundle.getBundle("path", new Locale("es", "Mx"));		
		String rutaReporte = path.getString("value");
		
		params.put("cuenta", req.getCuentaAhorro());
		
		params.put("pRutaimagen", rutaReporte+"/Reportes/img/asp.png");
		
		
		try {
			JasperPrint print = JasperFillManager.fillReport(rutaReporte+"/Reportes/CONT_SERV_MEDIOS_ELECT_subreport2.jasper", params,ds.getConnection());
			
			pdfBytes = JasperExportManager.exportReportToPdf(print);
		
		} catch (JRException e) {
			log.error("ERROR AL GENERAR REPORTE TARJETA DE AHORRO CUENTA SIMPLIFICADA");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return pdfBytes;
		
	}
	
	public byte[] generaContratoCuentaSimplificada(GeneraReporteContratoReq contratoReq) {
		log.info("####### Inicia generaContratoCuentaSimplificada");
		initialized();
		byte[] pdfBytes;
		Map<String,Object> params = new HashMap<>();
		ResourceBundle path = ResourceBundle.getBundle("path", new Locale("es", "Mx"));		
		String rutaReporte = path.getString("value");
		
		params.put("cuenta", contratoReq.getCuentaAhorro());	
		
		try {
			JasperPrint print = JasperFillManager.fillReport(rutaReporte+"/Reportes/ContratoCuentaSimplificada.jasper", params,ds.getConnection());
			
			pdfBytes = JasperExportManager.exportReportToPdf(print);
		
		} catch (JRException e) {
			log.error("ERROR AL GENERAR REPORTE CONTRATO CUENTA SIMPLIFICADA");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return pdfBytes;
	}
	
	public byte[] generaContratoServElecSF(GeneraContratoServElecReq req) {
		initialized();
		byte[] pdfBytes;
		Map<String,Object> params = new HashMap<>();
		ResourceBundle path = ResourceBundle.getBundle("path", new Locale("es", "Mx"));		
		String rutaReporte = path.getString("value");
		
		params.put("pCuenta", req.getCuenta());
		
		params.put("pImagen", rutaReporte+"/Reportes/img/asp.png");
		
		
		try {
			JasperPrint print = JasperFillManager.fillReport(rutaReporte+"/Reportes/CONT_SERV_MEDIOS_ELECT_subreport2.jasper", params,ds.getConnection());
			
			pdfBytes = JasperExportManager.exportReportToPdf(print);
		
		} catch (JRException e) {
			log.error("ERROR AL GENERAR REPORTE CONTRATO DE SERVICIOS ELECTRONICOS");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return pdfBytes;
		
	}
	
	public byte[] generaRegistroContrato(GeneraRegistroContratoReq req) {
		initialized();
		byte[] pdfBytes;
		Map<String,Object> params = new HashMap<>();
		ResourceBundle path = ResourceBundle.getBundle("path", new Locale("es", "Mx"));		
		String rutaReporte = path.getString("value");
		
		params.put("pCuenta", req.getCuenta());
		
		params.put("pImagen", rutaReporte+"/Reportes/img/asp.png");
		
		
		try {
			JasperPrint print = JasperFillManager.fillReport(rutaReporte+"/Reportes/Registro_Contrato.jasper", params,ds.getConnection());
			
			pdfBytes = JasperExportManager.exportReportToPdf(print);
		
		} catch (JRException e) {
			log.error("ERROR AL GENERAR REPORTE CONTRATO DE SERVICIOS ELECTRONICOS");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return pdfBytes;
		
	}
	
	public byte[] generaDisposicionesLegales(GeneraDisposicionesLegales req) {
		initialized();
		byte[] pdfBytes;
		Map<String,Object> params = new HashMap<>();
		ResourceBundle path = ResourceBundle.getBundle("path", new Locale("es", "Mx"));		
		String rutaReporte = path.getString("value");
		
		params.put("pCuenta", req.getCuenta());
		
		params.put("pImagen", rutaReporte+"/Reportes/img/asp.png");
		
		
		try {
			JasperPrint print = JasperFillManager.fillReport(rutaReporte+"/Reportes/disposiciones_legales.jasper", params,ds.getConnection());
			
			pdfBytes = JasperExportManager.exportReportToPdf(print);
		
		} catch (JRException e) {
			log.error("ERROR AL GENERAR REPORTE CONTRATO DE SERVICIOS ELECTRONICOS");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return pdfBytes;
		
	}
	
}
