package com.asp.eiyu.api.admdocument.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.asp.eiyu.api.admdocument.entity.DocumentoRegistroEntity;

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
		String SQL_SELECT = "SELECT  id, codsistema, persona_id, codigo, ruta_storage, estado, fechahoracarga, tipodocumento, rutanotificacion, fecha_creacion, "
				+ "				fecha_modificacion, usuario_creacion, usuario_modificacion"
				+ "       FROM scarchivodigital  WHERE tipodocumento= ? AND codigo = ? order by fecha_creacion desc ";

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
							docResults.getString(12),docResults.getString(13)));
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
	public String eliminarScarchivodigital(Integer idScarchivo, DataSource pool) {
	    String SQL_DELETE = "DELETE FROM scarchivodigital WHERE id = ?";
	    try (Connection conn = pool.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

	        stmt.setInt(1, idScarchivo);
	        int filasAfectadas = stmt.executeUpdate();

	        if (filasAfectadas > 0) {
	            return "Eliminado correctamente";
	        } else {
	            return "No se encontró el registro para eliminar";
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace(); // Puedes loguearlo mejor si usas un logger
	        return "Error al eliminar el registro: " + ex.getMessage();
	    }
	}
}