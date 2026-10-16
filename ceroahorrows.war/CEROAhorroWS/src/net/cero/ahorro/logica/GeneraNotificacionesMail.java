/*
 * @(#)RegistraDisposicionLinea.java 1.0 05/07/19 
 * 
 */
package net.cero.ahorro.logica;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;

import net.cero.data.Atachments;
import net.cero.data.GeneraContratoServElecReq;
import net.cero.data.GeneraDisposicionesLegales;
import net.cero.data.GeneraNotificacionCallCenterReqOBJ;
import net.cero.data.GeneraRegistroContratoReq;
import net.cero.data.GeneraReporteContratoReq;
import net.cero.data.GeneraReporteTarjetaReq;
import net.cero.data.Respuesta;

/**
 * Logica de negocio para envio de Anexo A.
 * 
 * @author Israel
 * @version 1.0 14/07/18
 */
@Log4j2
public class GeneraNotificacionesMail {
	public Respuesta enviaTarjetaAhorroYContrato(GeneraReporteTarjetaReq tarjeta, GeneraReporteContratoReq contrato,
			GeneraContratoServElecReq servElec, GeneraRegistroContratoReq regContrato, GeneraDisposicionesLegales regDispleg)  {
		GeneraReporte reporte = new GeneraReporte();
		EnviaNotificacionesMail enviaNotificacion = new EnviaNotificacionesMail();
		Respuesta resp = new Respuesta();
		List<Atachments> atachmentsList = new ArrayList<Atachments>();
		Atachments atachment = new Atachments();
		try {
			byte[] pdfBytesTarjeta = reporte.generaTarjetaAhorro(tarjeta);
			atachment = new Atachments();
			atachment.setTheAttachmentBytes(pdfBytesTarjeta);
			atachment.setTheFilename("TarjetaAhorro" + ".pdf");
			atachment.setTheContentType("application/pdf");
			atachmentsList.add(atachment);

			byte[] pdfBytesContrato = reporte.generaContratoCuentaSimplificada(contrato);
			atachment = new Atachments();
			atachment.setTheAttachmentBytes(pdfBytesContrato);
			atachment.setTheFilename("ContratoCuentaSimplificada" + ".pdf");
			atachment.setTheContentType("application/pdf");
			atachmentsList.add(atachment);

			byte[] pdfBytesServElec = reporte.generaContratoServElecSF(servElec);
			atachment = new Atachments();
			atachment.setTheAttachmentBytes(pdfBytesServElec);
			atachment.setTheFilename("ContratoServiciosElectronicos" + ".pdf");
			atachment.setTheContentType("application/pdf");
			atachmentsList.add(atachment);
			
			byte[] pdfBytesRegCont = reporte.generaRegistroContrato(regContrato);
			atachment = new Atachments();
			atachment.setTheAttachmentBytes(pdfBytesRegCont);
			atachment.setTheFilename("Registro_Contrato" + ".pdf");
			atachment.setTheContentType("application/pdf");
			atachmentsList.add(atachment);
			
			byte[] pdfBytesDispLeg = reporte.generaDisposicionesLegales(regDispleg);
			atachment = new Atachments();
			atachment.setTheAttachmentBytes(pdfBytesDispLeg);
			atachment.setTheFilename("disposiciones_legales" + ".pdf");
			atachment.setTheContentType("application/pdf");
			atachmentsList.add(atachment);
			
			ArrayList<byte[]> arregloBytes = new ArrayList<>();
			arregloBytes.add(pdfBytesTarjeta);
			arregloBytes.add(pdfBytesContrato);
			arregloBytes.add(pdfBytesServElec);
			arregloBytes.add(pdfBytesRegCont);
			arregloBytes.add(pdfBytesDispLeg);
			
			String json = new Gson().toJson(arregloBytes);
			
			enviaNotificacion.sendMail(tarjeta.getMailFrom(), tarjeta.getMailTo(), tarjeta.getSubject(),
					tarjeta.getMailBody(), atachmentsList);
			resp.setCodigo(0);
			resp.setMensaje("Todo bien");
			resp.setData(json);
			
		} catch (Exception e) {
			log.info("Error en generaNotificacionesMail:: " + e.getMessage());
			log.error("Error en generaNotificacionesMail:: " + e.getMessage());
			resp.setCodigo(1);
			resp.setMensaje(e.getMessage());
		}

		return resp;
	}

	public Respuesta enviaTarjetaAhorro(GeneraReporteTarjetaReq req) {
		GeneraReporte reporte = new GeneraReporte();
		EnviaNotificacionesMail enviaNotificacion = new EnviaNotificacionesMail();
		Respuesta resp = new Respuesta();
		List<Atachments> atachmentsList = new ArrayList<Atachments>();
		Atachments atachment = new Atachments();
		try {
			byte[] pdfBytesAnexo = reporte.generaTarjetaAhorro(req);
			atachment = new Atachments();
			atachment.setTheAttachmentBytes(pdfBytesAnexo);
			atachment.setTheFilename("TarjetaAhorro" + ".pdf");
			atachment.setTheContentType("application/pdf");
			atachmentsList.add(atachment);

			enviaNotificacion.sendMail(req.getMailFrom(), req.getMailTo(), req.getSubject(), req.getMailBody(),
					atachmentsList);
			resp.setCodigo(0);
			resp.setMensaje("Todo bien");
		} catch (Exception e) {
			log.info("Error en generaNotificacionesMail:: " + e.getMessage());
			log.error("Error en generaNotificacionesMail:: " + e.getMessage());
			resp.setCodigo(1);
			resp.setMensaje(e.getMessage());
		}

		return resp;
	}

	public Respuesta enviarPIN(GeneraReporteTarjetaReq req) {
		EnviaNotificacionesMail enviaNotificacion = new EnviaNotificacionesMail();
		Respuesta resp = new Respuesta();
		try {
			enviaNotificacion.sendMail(req.getMailFrom(), req.getMailTo(), req.getSubject(), req.getMailBody(), null);
			resp.setCodigo(0);
			resp.setMensaje("Todo bien");
		} catch (Exception e) {
			log.info("Error en generaNotificacionesMail:: " + e.getMessage());
			log.error("Error en generaNotificacionesMail:: " + e.getMessage());
			resp.setCodigo(1);
			resp.setMensaje(e.getMessage());
		}

		return resp;
	}
	
	public Respuesta enviarNotificacionCallCenter(GeneraNotificacionCallCenterReqOBJ req) {
		EnviaNotificacionesMail enviaNotificacion = new EnviaNotificacionesMail();
		Respuesta resp = new Respuesta();
		try {
			enviaNotificacion.sendMail(req.getMailFrom(), req.getMailTo(), req.getSubject(), req.getMailBody(), null);
			resp.setCodigo(0);
			resp.setMensaje("Todo bien");
		} catch (Exception e) {
			log.info("Error en generaNotificacionesMail:: " + e.getMessage());
			log.error("Error en generaNotificacionesMail:: " + e.getMessage());
			resp.setCodigo(1);
			resp.setMensaje(e.getMessage());
		}

		return resp;
	}

	public Respuesta enviaContratoCuentaSimplificada(GeneraReporteContratoReq contratoReq) {
		log.info("####### Inicia enviaContratoCuentaSimplificada");
		GeneraReporte reporte = new GeneraReporte();
		EnviaNotificacionesMail enviaNotificacion = new EnviaNotificacionesMail();
		Respuesta resp = new Respuesta();
		List<Atachments> atachmentsList = new ArrayList<Atachments>();
		Atachments atachment = new Atachments();
		try {
			byte[] pdfBytesAnexo = reporte.generaContratoCuentaSimplificada(contratoReq);
			atachment = new Atachments();
			atachment.setTheAttachmentBytes(pdfBytesAnexo);
			atachment.setTheFilename("ContratoCuentaSimplificada" + ".pdf");
			atachment.setTheContentType("application/pdf");
			atachmentsList.add(atachment);
			log.info("####### Inicia enviaNotificacion");
			enviaNotificacion.sendMail(contratoReq.getMailFrom(), contratoReq.getMailTo(), contratoReq.getSubject(),
					contratoReq.getMailBody(), atachmentsList);
			resp.setCodigo(0);
			resp.setMensaje("Todo bien");
		} catch (Exception e) {
			log.info("Error en generaNotificacionesMail:: " + e.getMessage());
			log.error("Error en generaNotificacionesMail:: " + e.getMessage());
			resp.setCodigo(1);
			resp.setMensaje(e.getMessage());
		}
		return resp;
	}
}
