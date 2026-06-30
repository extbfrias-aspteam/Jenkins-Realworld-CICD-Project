package com.asp.eiyu.api.admdocument.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import com.asp.eiyu.api.admdocument.entity.DocumentoRegistroEntity;
import com.asp.eiyu.api.admdocument.entity.SCbitacora;

/**
 * Banco ASP Project: eiyu Class: DocumentoRegistroRepository.java
 *
 * Description: Interface que extiende del JPA para poder realizar el crud de
 * las tabla scarchivodigital
 *
 * @author Herwin TR @company ICORPTTI @created Sep 3, 2023 @since JDK17
 *
 * @version Control de cambios: @version 1.0 Sep 3, 2023 Herwin: Creacion de la
 * clase
 *
 * @category
 *
 */
public class DocumentoRegistroRepository {

	/**
	 * Metodo el cual se utiliza para consultar la existencia de un registro en la
	 * tabla usando los siguientes parametros
	 * 
	 * @param tipo   - tipo de documento
	 * @param codigo - idusuario, cuenta
	 * @return retorna una lista de datos existentes en la base de datos
	 */
	public List<DocumentoRegistroEntity> consultaExistencia(String tipo, String codigo, DataSource pool)
			throws SQLException {
		String SQL_SELECT = "SELECT id,codsistema,persona_id,codigo,ruta_storage,estado,fechahoracarga,tipodocumento,rutanotificacion,fecha_creacion,fecha_modificacion,usuario_creacion,usuario_modificacion "
				+ "       FROM scarchivodigital WHERE tipodocumento= ? AND codigo = ? ORDER BY fechahoracarga DESC";

		List<DocumentoRegistroEntity> listaDocumento = new ArrayList<DocumentoRegistroEntity>();
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			// PreparedStatements are compiled by the database immediately and executed at a
			// later date.
			// Most databases cache previously compiled queries, which improves efficiency.
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				consultaDoc.setString(1, tipo);
				consultaDoc.setString(2, codigo);
				// Execute the statement
				ResultSet docResults = consultaDoc.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					listaDocumento.add(new DocumentoRegistroEntity(docResults.getInt(1), docResults.getString(2),
							docResults.getString(3), docResults.getString(4), docResults.getString(5),
							docResults.getInt(6), docResults.getDate(7), docResults.getString(8),
							docResults.getString(9), docResults.getDate(10), docResults.getDate(11),
							docResults.getString(12), docResults.getString(13)));
				}
				consultaDoc.close();
			}
			conn.close();
		} catch (SQLException ex) {
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
		return listaDocumento;
	}

	/**
	 * Metodo el cual se utiliza para consultar la existencia de un registro en la
	 * tabla usando los siguientes parametros
	 * 
	 * @param tipo   - tipo de documento
	 * @param codigo - idusuario, cuenta
	 * @return retorna una lista de datos existentes en la base de datos
	 */
	public DocumentoRegistroEntity consulta(String tipo, String codigo, String sistema, DataSource pool)
			throws SQLException {
		String SQL_SELECT = "SELECT id,codsistema,persona_id,codigo,ruta_storage,estado,fechahoracarga,tipodocumento,rutanotificacion,fecha_creacion,fecha_modificacion,usuario_creacion,usuario_modificacion "
				+ "       FROM scarchivodigital WHERE tipodocumento= ? AND codigo = ? AND codsistema = ? ORDER BY fechahoracarga DESC";

		DocumentoRegistroEntity documentoConsulta = null;
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			// PreparedStatements are compiled by the database immediately and executed at a
			// later date.
			// Most databases cache previously compiled queries, which improves efficiency.
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				consultaDoc.setString(1, tipo);
				consultaDoc.setString(2, codigo);
				consultaDoc.setString(3, sistema);
				// Execute the statement
				ResultSet docResults = consultaDoc.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					documentoConsulta = new DocumentoRegistroEntity(docResults.getInt(1), docResults.getString(2),
							docResults.getString(3), docResults.getString(4), docResults.getString(5),
							docResults.getInt(6), docResults.getDate(7), docResults.getString(8),
							docResults.getString(9), docResults.getDate(10), docResults.getDate(11),
							docResults.getString(12), docResults.getString(13));
				}
			}
			conn.close();
		} catch (SQLException ex) {
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
		return documentoConsulta;
	}

	public String consultarNombreArchivoCatalogo(String tipoDocumento, DataSource pool) throws SQLException {
		String sql = "SELECT nombre_archivo FROM sctipodocumento WHERE clave = ? LIMIT 1";
		try (Connection conn = pool.getConnection();
				PreparedStatement consultaDoc = conn.prepareStatement(sql)) {
			consultaDoc.setString(1, tipoDocumento);
			try (ResultSet docResults = consultaDoc.executeQuery()) {
				if (docResults.next()) {
					return docResults.getString(1);
				}
			}
		} catch (SQLException ex) {
			throw new SQLException(
					"Unable to successfully connect to the database in consultarNombreArchivoCatalogo. Please check the steps in the README and try again.",
					ex);
		}
		return null;
	}

	public void actualizaDocumento(DocumentoRegistroEntity document, DataSource pool) throws SQLException {
		String SQL_INSERT = "UPDATE scarchivodigital set estado = 2 where tipodocumento = ? and codigo = ? ";

		System.out.println("Dato Contenido:: " + document);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, document.getTipoDocumento());
				insertDoc.setString(2, document.getCodigo());
				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
			conn.close();
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			throw new SQLException(
					"Unable to successfully connect to the database in the guardarDocumento. Please check the "
							+ "steps in the README and try again.",
					ex);
		}
	}

	public boolean existeDocumentos(DocumentoRegistroEntity document, DataSource pool) throws SQLException {
		// Consulta para verificar si el documento existe con estado = 1
		String SQL_EXISTS = "SELECT id FROM scarchivodigital WHERE estado = 1 AND codigo = ? and codsistema =? and tipodocumento =? LIMIT 1";

		System.out.println("Verificando documento si existe en la tabla scarchivodigital: " + document);

		try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL_EXISTS)) {

			stmt.setString(1, document.getCodigo());
			stmt.setString(2, document.getCodSistema());
			stmt.setString(3, document.getTipoDocumento());
			
		try (ResultSet rs = stmt.executeQuery()) {
				return rs.next(); // Retorna true si encuentra el documento, false si no
			}
	
		
		} catch (SQLException ex) {
			throw new SQLException("Error al verificar el documento.", ex);
		}
		
	}

	public void actualizaDocumentoCompleto(DocumentoRegistroEntity document, DataSource pool) throws SQLException {
		String SQL_UPDATE = "UPDATE scarchivodigital \n" + " SET \n"
				+ "    codsistema = CASE WHEN ? <> '' THEN ? ELSE codsistema END, \n"
				+ "    persona_id = CASE WHEN ? <> '' THEN ? ELSE persona_id END, \n"
				+ "    ruta_storage = CASE WHEN ? <> '' THEN ? ELSE ruta_storage END, \n"
				+ "    fechahoracarga = current_timestamp,\n"
				+ "    tipodocumento = CASE WHEN ? <> '' THEN ? ELSE tipodocumento END, \n"
				+ "    rutanotificacion = CASE WHEN ? <> '' THEN ? ELSE rutanotificacion END,\n"
				+ "    fecha_creacion = current_timestamp, \n"
				+ "    usuario_creacion = CASE WHEN ? <> '' THEN ? ELSE usuario_creacion END\n"
				+ " WHERE codigo = ? and estado=1 and codsistema =? and tipodocumento = ? ";

		System.out.println("Dato Contenido:: Al actualizar###$### " + document);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement actualizar = conn.prepareStatement(SQL_UPDATE)) {

				actualizar.setString(1, document.getCodSistema() != null ? document.getCodSistema() : "");
				actualizar.setString(2, document.getCodSistema() != null ? document.getCodSistema() : "");

				actualizar.setString(3, document.getPersona_id() != null ? document.getPersona_id() : "");
				actualizar.setString(4, document.getPersona_id() != null ? document.getPersona_id() : "");

				actualizar.setString(5, document.getRuta_storage() != null ? document.getRuta_storage() : "");
				actualizar.setString(6, document.getRuta_storage() != null ? document.getRuta_storage() : "");

				actualizar.setString(7, document.getTipoDocumento() != null ? document.getTipoDocumento() : "");
				actualizar.setString(8, document.getTipoDocumento() != null ? document.getTipoDocumento() : "");

				actualizar.setString(9, document.getRutaNotificacion() != null ? document.getRutaNotificacion() : "");
				actualizar.setString(10, document.getRutaNotificacion() != null ? document.getRutaNotificacion() : "");

				actualizar.setString(11, document.getUsuario_creacion() != null ? document.getUsuario_creacion() : "");
				actualizar.setString(12, document.getUsuario_creacion() != null ? document.getUsuario_creacion() : "");

				actualizar.setString(13, document.getCodigo()); // primer parámetro para el WHERE
				actualizar.setString(14, document.getCodSistema()); // Último parámetro para el WHERE
				actualizar.setString(15, document.getTipoDocumento());
				// Execute the statement
				int row = actualizar.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo Actualizado >>>>>>>> " + row); // 1
			}
			conn.close();
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			throw new SQLException("Unable to successfully connect to the database in the actualizar. Please check the "
					+ "steps in the README and try again.", ex);
		}
	}

	public void guardarDocumento(DocumentoRegistroEntity document, DataSource pool) throws SQLException {
		String SQL_INSERT = "INSERT INTO scarchivodigital (codsistema,persona_id,codigo,ruta_storage,estado,fechahoracarga,tipodocumento,rutanotificacion,fecha_creacion,usuario_creacion) "
				+ " VALUES (?,?,?,?,?,current_timestamp,?,?,current_timestamp,?) ";
		System.out.println("Dato Contenido:: " + document);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, document.getCodSistema());
				insertDoc.setString(2, document.getPersona_id());
				insertDoc.setString(3, document.getCodigo());
				insertDoc.setString(4, document.getRuta_storage());
				insertDoc.setInt(5, document.getEstado());
				insertDoc.setString(6, document.getTipoDocumento());
				insertDoc.setString(7, document.getRutaNotificacion());
				insertDoc.setString(8, document.getUsuario_creacion());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
			conn.close();
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			throw new SQLException(
					"Unable to successfully connect to the database in the guardarDocumento. Please check the "
							+ "steps in the README and try again.",
					ex);
		}
	}

	public UUID guardarBitacora(SCbitacora document, DataSource pool) throws SQLException {
		String SQL_INSERT = "INSERT INTO scbitacora (codsistema,documentos_id,persona_id,estatus,fecha_creacion) "
				+ " VALUES (?,?,?,?,current_timestamp) " + " RETURNING uuid ";
		System.out.println("Dato Contenido:: " + document);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, document.codsistema());
				insertDoc.setInt(2, document.documentos_id());
				insertDoc.setString(3, document.persona_id());
				insertDoc.setString(4, document.estado());
				// Execute the statement
				try (ResultSet rs = insertDoc.executeQuery()) {
					if (rs.next()) {
						// Recuperamos el UUID generado
						UUID generatedId = (UUID) rs.getObject("uuid"); // Cambiamos 'id' por 'uuid'
						System.out.println("Archivo registrado con UUID >>>>>>>> " + generatedId);
						return generatedId; // Retornamos el UUID generado
					}
				}
			}
			conn.close();
		} catch (SQLException ex) {
			throw new SQLException(
					"Unable to successfully connect to the database in the guardar bitacora. Please check the "
							+ "steps in the README and try again.",
					ex);

		}
		return null;
	}

	
}
