package com.asp.eiyu.api.admdocument.gtw.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.asp.eiyu.api.admdocument.gtw.entity.SCTipoDocumento;

/**
 * Banco ASP Project: eiyu Class: DocumentoRegistroRepository.java
 *
 * Description: Interface que extiende del JPA para poder realizar el crud de
 * las tabla documento_registro
 *
 * @author Herwin TR @company ICORPTTI @created Sep 3, 2023 @since JDK17
 *
 * @version Control de cambios: @version 1.0 Sep 3, 2023 Herwin: Creacion de la
 * clase
 *
 * @category
 *
 */
public class TipoDocumentoRepository {

	/**
	 * Metodo el cual se utiliza para consultar la existencia de un registro en la
	 * tabla usando los siguientes parametros
	 * 
	 * @param clave - tipo documento cargando
	 * @param pool  - el pool de conexion utilizado y establecido en el sistema
	 * @return retorna una lista de valores encontrados en la base de datos
	 * @throws SQLException - en caso de ocurrir un error al consultar en la base de
	 *                      datos mostrar el error.
	 */
	public List<SCTipoDocumento> consultarTipoDoscumentos(String clave, DataSource pool) throws SQLException {
		System.out.println("clave>> " + clave);
		String SQL_SELECT = "SELECT documentos_id,clave,nombre_archivo from SCTIPODOCUMENTO where  clave = ? ";

		List<SCTipoDocumento> listaDocumento = new ArrayList<SCTipoDocumento>();
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			// PreparedStatements are compiled by the database immediately and executed at a
			// later date.
			// Most databases cache previously compiled queries, which improves efficiency.
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				consultaDoc.setString(1, clave);
				// Execute the statement
				ResultSet docResults = consultaDoc.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					listaDocumento.add(new SCTipoDocumento(docResults.getInt(1), docResults.getString(2),
							docResults.getString(3)));
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

	public void terminarConexionesIdleJDBCDefault(DataSource pool) throws SQLException {
	    String SQL_TERMINATE = """
	        SELECT pg_terminate_backend(pid)
	        FROM pg_stat_activity
	        WHERE state = 'idle'
	          AND datname = 'admdocumentoupload-db'
	          AND pid <> pg_backend_pid()
	          AND application_name = 'PostgreSQL JDBC Driver'
	    """;

	    try (Connection conn = pool.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(SQL_TERMINATE);
	         ResultSet rs = stmt.executeQuery()) {

	        int count = 0;
	        while (rs.next()) {
	            boolean terminated = rs.getBoolean(1);
	            if (terminated) {
	                count++;
	            }
	        }

	        System.out.println("Conexiones 'idle' JDBC terminadas: " + count);

	    } catch (SQLException e) {
	        throw new SQLException("Error al intentar terminar conexiones 'idle' JDBC", e);
	    }
	}



}