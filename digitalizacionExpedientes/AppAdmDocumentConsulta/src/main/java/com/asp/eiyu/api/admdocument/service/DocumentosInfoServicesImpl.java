package com.asp.eiyu.api.admdocument.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.asp.eiyu.api.admdocument.dto.ConsultaEstatusCuentaDocs;
import com.asp.eiyu.api.admdocument.dto.Documentos;
import com.asp.eiyu.api.admdocument.dto.SolicitudDocumento;

/**
 * Banco ASP
 * Project: eiyu
 * Class: DocumentosInfoServicesImpl.java
 *
 * Description:
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Feb 29, 2024
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Feb 29, 2024 Herwin: Creacion de la clase
 *
 * @category 
 *
 */
public class DocumentosInfoServicesImpl implements IDocumentosInfoServices {

	@Override
	public SolicitudDocumento obtenerListaDocumentos(String clabe, DataSource pool) throws SQLException {
		SolicitudDocumento solicitud = null;
		String SQL_NIVEL = "SELECT sr.nivel FROM SCREFEIYU sr WHERE sr.codigo = ?";
		String SQL_TIPO_PERSONA = "SELECT tipo_persona FROM SCCOREPERSONA WHERE persona_id = ?";
		
		String SQL_NIVEL_CUENTA = "SELECT clave FROM SCCATNIVCTAHORRO cnca WHERE cnca.clave IN (SELECT sr.nivel FROM SCREFEIYU sr WHERE sr.codigo = ?);";
		
		String SQL_REQUIRED_DOCS = "SELECT DISTINCT catdoc.clave \r\n"
				+ "  FROM SCCATDOCUMENTO catdoc \r\n"
				+ " INNER JOIN SCCTACUMENTOS ctadoc ON catdoc.id = ctadoc.documento_id \r\n"
				+ " INNER JOIN SCCATNIVCTAHORRO cnca ON ctadoc.nivel_cuenta_ahorro_id = cnca.id \r\n"
				+ " WHERE cnca.clave IN (\r\n"
				+ "          SELECT sr.nivel FROM SCREFEIYU sr WHERE sr.codigo = ?\r\n"
				+ "       ) \r\n"
				+ "   AND ctadoc.t_persona = ? \r\n"
				+ " ORDER BY catdoc.clave";

		String SQL_LATEST_STATUS = "SELECT DISTINCT ON (rv.tipo_documento) \r\n"
				+ "       rv.tipo_documento, rv.esvalidonubarium, rv.esvalidocore, rv.estatus \r\n"
				+ "  FROM SCRESVALIDACION rv \r\n"
				+ " WHERE rv.codigo = ? \r\n"
				+ " ORDER BY rv.tipo_documento, rv.fecha_creacion DESC, rv.item DESC";

		
		ConsultaEstatusCuentaDocs rspDocumento = new ConsultaEstatusCuentaDocs(clabe, "",  "", new ArrayList<Documentos>());
		boolean cuentaExiste = false;
		String tipoPersona = "M";
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");

			try (PreparedStatement consultaTipoPersona = conn.prepareStatement(SQL_TIPO_PERSONA)) {
				consultaTipoPersona.setString(1, clabe);

				ResultSet docResults = consultaTipoPersona.executeQuery();
				while (docResults.next()) {
					tipoPersona = normalizarTipoPersona(docResults.getString(1));
				}
			}

			try (PreparedStatement consultaNivel = conn.prepareStatement(SQL_NIVEL)) {
				consultaNivel.setString(1, clabe);

				// Execute the statement
				ResultSet docResults = consultaNivel.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					cuentaExiste = !docResults.getString(1).isEmpty();
				}
			}
			
			try (PreparedStatement consultaNivel = conn.prepareStatement(SQL_NIVEL_CUENTA)) {
				consultaNivel.setString(1, clabe);

				// Execute the statement
				ResultSet docResults = consultaNivel.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					rspDocumento = new ConsultaEstatusCuentaDocs(clabe, docResults.getString(1),  "", new ArrayList<Documentos>());
				}
			}
			
			Map<String, Documentos> documentosPorClave = new LinkedHashMap<>();
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_REQUIRED_DOCS)) {
				consultaDoc.setString(1, clabe);
				consultaDoc.setString(2, tipoPersona);
				ResultSet docResults = consultaDoc.executeQuery();
				while (docResults.next()) {
					documentosPorClave.put(docResults.getString(1), crearDocumento(docResults.getString(1), -1, -1, -1));
				}
			}

			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_LATEST_STATUS)) {
				consultaDoc.setString(1, clabe);
				ResultSet docResults = consultaDoc.executeQuery();
				while (docResults.next()) {
					String claveDocumento = docResults.getString(1);
					Documentos documento = crearDocumento(claveDocumento, docResults.getInt(2), docResults.getInt(3),
							docResults.getInt(4));
					documentosPorClave.put(claveDocumento, documento);
				}
			}

			List<Documentos> listaDocumento = new ArrayList<>(documentosPorClave.values());
			
			if(listaDocumento!=null && !listaDocumento.isEmpty()) {
				rspDocumento = rspDocumento.withListDoc(listaDocumento);
			}
			
			if(listaDocumento!=null && !listaDocumento.isEmpty()) {
				rspDocumento = rspDocumento.withcodResp("0");
			}else if(!cuentaExiste){
				rspDocumento = rspDocumento.withcodResp("3");
			}else {
				rspDocumento = rspDocumento.withcodResp("2");
			}
	
		} catch (SQLException ex) {
			rspDocumento = rspDocumento.withcodResp("-1");
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			throw new SQLException(
					"Unable to successfully connect to the database in the consultaExistencia. Please check the "
							+ "steps in the README and try again.",
					ex);
		}
		solicitud = new SolicitudDocumento(rspDocumento);
		return solicitud;
	}

	private Documentos crearDocumento(String claveDocumento, int esvalidonubarium, int esvalidocore, int status) {
		String estatus = "";
		if (esvalidonubarium == 1 && esvalidocore == 1 && status == 2) {
			estatus = "Cargado Correcto";
		} else if (esvalidonubarium == -1 && esvalidocore == -1 && status == -1) {
			estatus = "Sin Cargar";
		} else if (status != 2) {
			estatus = "Cargado con Errores";
		}
		return new Documentos(claveDocumento, esvalidonubarium, esvalidocore, status, estatus);
	}

	private String normalizarTipoPersona(String tipoPersona) {
		if (tipoPersona == null || tipoPersona.isBlank()) {
			return "M";
		}
		String tipoPersonaNormalizado = tipoPersona.trim().toUpperCase();
		if ("F".equals(tipoPersonaNormalizado) || tipoPersonaNormalizado.contains("FIS")) {
			return "F";
		}
		if ("M".equals(tipoPersonaNormalizado) || tipoPersonaNormalizado.contains("MOR")) {
			return "M";
		}
		return tipoPersonaNormalizado;
	}

}
