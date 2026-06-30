package topico.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import topico.dto.Respuesta;
import topico.dto.SCbitacora;

/**
 * Banco ASP
 * Project: eiyu
 * Class: DocumentoRegistroValidacionImpl.java
 *
 * Description:
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Nov 21, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Nov 21, 2023 Herwin: Creacion de la clase
 *
 * @category 
 *
 */
public class DocumentoRegistroValidacionImpl {

	public void actualizaSCresvalidacion(Respuesta respuestaEstatus, DataSource pool) throws SQLException {
		int esValidoCore = respuestaEstatus.esValidoCURP()?1:0;
		String SQL_CONTADOR = "(SELECT CASE WHEN COUNT(*) < 4 THEN 0 ELSE 1 END FROM ( "
				+ "	SELECT estado_consulta FROM scnubocrine WHERE scarchivodigital_id = ?"
				+ "	 UNION ALL"
				+ "	SELECT estado_consulta FROM scnubvalidacurp WHERE scarchivodigital_id = ?"
				+ "	 UNION ALL"
				+ "	SELECT estado_consulta FROM scnubvalidaine WHERE scarchivodigital_id = ?"
				+ "	 UNION ALL"
				+ "	SELECT estado_consulta FROM scnubvalidarfc WHERE scarchivodigital_id = ?"
				+ "	) AS resultado WHERE resultado.estado_consulta = 'OK')";
		
		String SQL_UPDATE = "INSERT INTO SCRESVALIDACION (participante_id,tipo_documento,item,codsistema,codigo,esvalidonubarium,esvalidocore,estatus,notificado,fechanotificacion,fecha_hora,fecha_creacion)"
				+ "               VALUES (?, ?, 1, ?, ?,"+SQL_CONTADOR+", ?, 1, 0, current_timestamp, current_timestamp, current_timestamp) "
				+ "          ON CONFLICT (participante_id,tipo_documento) "
				+ "                   DO "
				+ "	 UPDATE SET item = ((Select item from SCRESVALIDACION where participante_id = ? and tipo_documento = ?)+1), esvalidonubarium = "+SQL_CONTADOR+", esvalidocore = ?, fecha_modificacion = current_timestamp, usuario_modificacion = 'autoupdate'";
		System.out.println("Dato Contenido:: " + respuestaEstatus);
		System.out.println("Dato Contenido:: " + SQL_UPDATE.replace("  ", ""));
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_UPDATE)) {
				insertDoc.setString(1, respuestaEstatus.participante_id());
				insertDoc.setString(2, respuestaEstatus.tipodocumento());
				insertDoc.setString(3, respuestaEstatus.codigoSistema());
				insertDoc.setString(4, respuestaEstatus.codigoCliente());
				insertDoc.setInt(5, respuestaEstatus.scarchivodigital_id());
				insertDoc.setInt(6, respuestaEstatus.scarchivodigital_id());
				insertDoc.setInt(7, respuestaEstatus.scarchivodigital_id());
				insertDoc.setInt(8, respuestaEstatus.scarchivodigital_id());
				insertDoc.setInt(9, esValidoCore);
				insertDoc.setString(10, respuestaEstatus.participante_id());
				insertDoc.setString(11, respuestaEstatus.tipodocumento());
				insertDoc.setInt(12, respuestaEstatus.scarchivodigital_id());
				insertDoc.setInt(13, respuestaEstatus.scarchivodigital_id());
				insertDoc.setInt(14, respuestaEstatus.scarchivodigital_id());
				insertDoc.setInt(15, respuestaEstatus.scarchivodigital_id());
				insertDoc.setInt(16, esValidoCore);
				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo actualizado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			throw new SQLException("Unable to successfully connect to the database in the guardarDocumento. Please check the "
					+ "steps in the README and try again.", ex);
		}
	}
	
	public void actualizaSCresvalidacionIsNotINE(Respuesta respuestaEstatus, DataSource pool) throws SQLException {
		String SQL_UPDATE = "INSERT INTO SCRESVALIDACION (participante_id,tipo_documento,item,codsistema,codigo,esvalidonubarium,esvalidocore,estatus,notificado,fechanotificacion,fecha_hora,fecha_creacion)"
				+ "               VALUES (?, ?, 1, ?, ?, 1, 1, 2, 0, current_timestamp, current_timestamp, current_timestamp) "
				+ "          ON CONFLICT (participante_id,tipo_documento) "
				+ "                   DO "
				+ "	 UPDATE SET item = ((SELECT item FROM SCRESVALIDACION WHERE participante_id = ? AND tipo_documento = ?)+1), fecha_modificacion = current_timestamp, usuario_modificacion = 'autoupdate'";
		System.out.println("Dato Contenido:: " + respuestaEstatus);
		System.out.println("Dato Contenido:: " + SQL_UPDATE.replace("  ", ""));
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_UPDATE)) {
				insertDoc.setString(1, respuestaEstatus.participante_id());
				insertDoc.setString(2, respuestaEstatus.tipodocumento());
				insertDoc.setString(3, respuestaEstatus.codigoSistema());
				insertDoc.setString(4, respuestaEstatus.codigoCliente());
				insertDoc.setString(5, respuestaEstatus.participante_id());
				insertDoc.setString(6, respuestaEstatus.tipodocumento());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo actualizado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			throw new SQLException("Unable to successfully connect to the database in the guardarDocumento. Please check the "
					+ "steps in the README and try again.", ex);
		}
	}
	
	
	public void actualizaSCresultvalfile(Respuesta respuestaEstatus, DataSource pool) throws SQLException {
		
		String SQL_UPDATE = "INSERT INTO scresultvalfile (participante_id,tipo_documento,item,scarchivodigital_id,fecha_creacion)"
				+ "               VALUES (?, ?, (SELECT item FROM SCRESVALIDACION WHERE participante_id= ? AND tipo_documento =?), ?,current_timestamp) ";
		System.out.println("Dato Contenido:: " + respuestaEstatus);
		System.out.println("Dato Contenido:: " + SQL_UPDATE.replace("  ", ""));
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_UPDATE)) {
				insertDoc.setString(1, respuestaEstatus.participante_id());
				insertDoc.setString(2, respuestaEstatus.tipodocumento());
				insertDoc.setString(3, respuestaEstatus.participante_id());
				insertDoc.setString(4, respuestaEstatus.tipodocumento());
				insertDoc.setInt(5, respuestaEstatus.scarchivodigital_id());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo actualizado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			throw new SQLException("Unable to successfully connect to the database in the guardarDocumento. Please check the "
					+ "steps in the README and try again.", ex);
		}
	}

	public void guardarBitacora(SCbitacora bitacora, DataSource pool) throws SQLException {
		String SQL_INSERT = "INSERT INTO scbitacora (codsistema,documentos_id,persona_id,estatus,fecha_creacion) "
				+ " VALUES (?,?,?,?,current_timestamp)";
		System.out.println("Dato Contenido:: " + bitacora);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, bitacora.codsistema());
				insertDoc.setInt(2, bitacora.documentos_id());
				insertDoc.setString(3, bitacora.persona_id());
				insertDoc.setString(4, bitacora.estado());
				// Execute the statement
				int row = insertDoc.executeUpdate();
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			throw new SQLException(
					"Unable to successfully connect to the database in the guardarDocumento. Please check the "
							+ "steps in the README and try again.",
					ex);
		}
	}
}
