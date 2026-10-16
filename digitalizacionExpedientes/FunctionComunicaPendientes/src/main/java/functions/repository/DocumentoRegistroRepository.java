package functions.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.asp.eiyu.api.admdocument.entity.SCbitacora;

import functions.dto.ValidacionesDocumentosByNivel;

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
	public List<ValidacionesDocumentosByNivel> consultarDoscumentosPendientes(String cuenta_referencia,
			String t_persona, DataSource pool) throws SQLException {
		System.out.println("cuenta_referencia>> " + cuenta_referencia);
		System.out.println("t_persona>> " + t_persona);
		String tipoPersonaNormalizado = normalizarTipoPersona(t_persona);

		String SQL_SELECT = "SELECT DISTINCT cd.clave, cd.descripcion \n"
				+ "  FROM scctacumentos ncd \n"
				+ "  LEFT JOIN SCCATDOCUMENTO cd ON ncd.documento_id = cd.id \n"
				+ " WHERE ncd.t_persona = ? \n"
				+ "   AND ncd.nivel_cuenta_ahorro_id IN ( \n"
				+ "         SELECT id FROM SCCATNIVCTAHORRO cnca \n"
				+ "          WHERE cnca.clave IN ( \n"
				+ "                SELECT nivel FROM SCREFEIYU WHERE codigo = ? \n"
				+ "          ) \n"
				+ "   ) \n"
				+ "   AND NOT EXISTS ( \n"
				+ "         SELECT 1 \n"
				+ "           FROM SCARCHIVODIGITAL ad \n"
				+ "          WHERE ad.codigo = ? \n"
				+ "            AND ad.tipodocumento = cd.clave \n"
				+ "   ) \n"
				+ " ORDER BY cd.clave \n";
		System.out.println("query::::::: " + SQL_SELECT);
		List<ValidacionesDocumentosByNivel> listaDocumento = new ArrayList<ValidacionesDocumentosByNivel>();
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			// PreparedStatements are compiled by the database immediately and executed at a
			// later date.
			// Most databases cache previously compiled queries, which improves efficiency.
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				consultaDoc.setString(1, tipoPersonaNormalizado);
				consultaDoc.setString(2, cuenta_referencia);
				consultaDoc.setString(3, cuenta_referencia);
				// Execute the statement
				ResultSet docResults = consultaDoc.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					listaDocumento.add(new ValidacionesDocumentosByNivel(docResults.getString(1),
							"SIN CARGA", docResults.getString(2)));
				}
			}
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

	/**
	 * Metodo el cual se utiliza para consultar la existencia de un registro en la
	 * tabla usando los siguientes parametros
	 * 
	 * @param tipo   - tipo de documento
	 * @param codigo - idusuario, cuenta
	 * @return retorna una lista de datos existentes en la base de datos
	 */
	public List<ValidacionesDocumentosByNivel> consultarDoscumentosErrores(String cuenta_referencia, String t_persona,
			DataSource pool) throws SQLException {
		System.out.println("cuenta_referencia>> " + cuenta_referencia);
		System.out.println("t_persona>> " + t_persona);

		String SQL_SELECT = "SELECT DISTINCT ON (rv.tipo_documento) rv.tipo_documento, cd.descripcion, '' \n"
				+ "            FROM SCRESVALIDACION rv \n"
				+ "       LEFT JOIN SCCATDOCUMENTO cd \n"
				+ "              ON rv.tipo_documento = cd.clave \n"
				+ "           WHERE rv.codigo = ? \n"
				+ "             AND (rv.estatus <> 2 OR rv.esvalidonubarium <> 1 OR rv.esvalidocore <> 1) \n"
				+ "        ORDER BY rv.tipo_documento, rv.fecha_creacion DESC, rv.item DESC \n";
		System.out.println("Query:::: " + SQL_SELECT);
		List<ValidacionesDocumentosByNivel> listaDocumento = new ArrayList<ValidacionesDocumentosByNivel>();
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				consultaDoc.setString(1, cuenta_referencia);
				ResultSet docResults = consultaDoc.executeQuery();
				while (docResults.next()) {
					listaDocumento.add(new ValidacionesDocumentosByNivel(docResults.getString(1),
							"CARGADO CON ERRORES", docResults.getString(2)));
				}
			}
		} catch (SQLException ex) {
			throw new SQLException(
					"Unable to successfully connect to the database in the consultaExistencia. Please check the "
							+ "steps in the README and try again.",
					ex);
		}
		return listaDocumento;
	}

	public void guardarBitacora(SCbitacora document, DataSource pool) throws SQLException {
		String SQL_INSERT = "INSERT INTO scbitacora (codsistema,documentos_id,persona_id,estatus,fecha_creacion) "
				+ " VALUES (?,?,?,?,current_timestamp)";
		System.out.println("Dato Contenido:: " + document);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, document.codsistema());
				insertDoc.setInt(2, document.documentos_id());
				insertDoc.setString(3, document.persona_id());
				insertDoc.setString(4, document.estado());
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
