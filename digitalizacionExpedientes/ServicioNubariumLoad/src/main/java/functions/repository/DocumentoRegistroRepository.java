package functions.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.google.cloud.storage.Storage;

import functions.entity.ActualizaEstatusFlujo;
import functions.entity.CatDocumento;
import functions.entity.DocumentoRegistroEntity;
import functions.entity.SCbitacora;
import functions.entity.SCcorePersona;
import functions.entity.SCrefeiyu;

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
public class DocumentoRegistroRepository {

	/**
	 * Metodo el cual se utiliza para consultar la existencia de un registro en la
	 * tabla usando los siguientes parametros
	 * 
	 * @param tipo   - tipo de documento
	 * @param codigo - idusuario, cuenta
	 * @return retorna una lista de datos existentes en la base de datos
	 */
	public List<DocumentoRegistroEntity> consultarDoscumentosINE(String ruta_storage, DataSource pool)
			throws SQLException {
		System.out.println("uid>> " + ruta_storage);
		String SQL_SELECT = "SELECT id, codsistema, persona_id, codigo, ruta_storage,estado, fechahoracarga, tipodocumento, rutanotificacion, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion "
				+ "       FROM  scarchivodigital where id in (SELECT id FROM scarchivodigital WHERE ruta_storage= ?)  and tipodocumento in ('INE_FRONTAL','INE_REVERSO') and estado = 1  ";

		List<DocumentoRegistroEntity> listaDocumento = new ArrayList<DocumentoRegistroEntity>();
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			// PreparedStatements are compiled by the database immediately and executed at a
			// later date.
			// Most databases cache previously compiled queries, which improves efficiency.
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				consultaDoc.setString(1, ruta_storage);
				// Execute the statement
				ResultSet docResults = consultaDoc.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					listaDocumento.add(new DocumentoRegistroEntity(docResults.getInt(1), docResults.getString(2),
							docResults.getString(3), docResults.getString(4), docResults.getString(5),
							docResults.getInt(6), docResults.getDate(7), docResults.getString(8),
							docResults.getString(9), docResults.getDate(10), null, docResults.getString(12), null));
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

	/**
	 * Metodo el cual se utiliza para consultar la existencia de un registro en la
	 * tabla usando los siguientes parametros
	 * 
	 * @param tipo   - tipo de documento
	 * @param codigo - idusuario, cuenta
	 * @return retorna una lista de datos existentes en la base de datos
	 */
	public List<DocumentoRegistroEntity> consultarDoscumentosNotINE(String ruta_storage, DataSource pool)
			throws SQLException {
		System.out.println("uid>> " + ruta_storage);
		String SQL_SELECT = "SELECT id, codsistema, persona_id, codigo, ruta_storage,estado, fechahoracarga, tipodocumento, rutanotificacion, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion "
				+ "       FROM  scarchivodigital where id in (SELECT id FROM scarchivodigital WHERE ruta_storage= ?)  and tipodocumento not in ('INE_FRONTAL','INE_REVERSO') and estado = 1  ";

		List<DocumentoRegistroEntity> listaDocumento = new ArrayList<DocumentoRegistroEntity>();
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			// PreparedStatements are compiled by the database immediately and executed at a
			// later date.
			// Most databases cache previously compiled queries, which improves efficiency.
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				consultaDoc.setString(1, ruta_storage);
				// Execute the statement
				ResultSet docResults = consultaDoc.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					listaDocumento.add(new DocumentoRegistroEntity(docResults.getInt(1), docResults.getString(2),
							docResults.getString(3), docResults.getString(4), docResults.getString(5),
							docResults.getInt(6), docResults.getDate(7), docResults.getString(8),
							docResults.getString(9), docResults.getDate(10), null, docResults.getString(12), null));
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
	
	/**
	 * Metodo el cual se utiliza para consultar la existencia de un registro en la
	 * tabla usando los siguientes parametros
	 * 
	 * @param tipo   - tipo de documento
	 * @param codigo - idusuario, cuenta
	 * @return retorna una lista de datos existentes en la base de datos
	 */
	public DocumentoRegistroEntity consultarDoscumentosNoIne(String uid, DataSource pool) throws SQLException {
		System.out.println("uid>> " + uid);
		String SQL_SELECT = "SELECT id, codsistema, persona_id, codigo, ruta_storage,estado, fechahoracarga, tipodocumento, rutanotificacion, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion "
				+ "            FROM scarchivodigital "
				+ "           WHERE ruta_storage = ? AND tipodocumento NOT IN ('INE_FRONTAL','INE_REVERSO') ";

		DocumentoRegistroEntity documento = null;
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			// PreparedStatements are compiled by the database immediately and executed at a
			// later date.
			// Most databases cache previously compiled queries, which improves efficiency.
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				consultaDoc.setString(1, uid);
				// Execute the statement
				ResultSet docResults = consultaDoc.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					documento = new DocumentoRegistroEntity(docResults.getInt(1), docResults.getString(2),
							docResults.getString(3), docResults.getString(4), docResults.getString(5),
							docResults.getInt(6), docResults.getDate(7), docResults.getString(8),
							docResults.getString(9), docResults.getDate(10), null, docResults.getString(12), null);
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
		return documento;
	}

	/**
	 * Metodo el cual se utiliza para consultar la existencia de un registro en la
	 * tabla usando los siguientes parametros
	 * 
	 * @param tipo   - tipo de documento
	 * @param codigo - idusuario, cuenta
	 * @return retorna una lista de datos existentes en la base de datos
	 */
	public CatDocumento consultarDescripcionDocumento(String claveDocumento, DataSource pool) throws SQLException {
		System.out.println("uid>> " + claveDocumento);
		String SQL_SELECT = "SELECT clave, descripcion FROM scctacumentos WHERE clave = ?";

		CatDocumento documento = null;
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			// PreparedStatements are compiled by the database immediately and executed at a
			// later date.
			// Most databases cache previously compiled queries, which improves efficiency.
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				consultaDoc.setString(1, claveDocumento);
				// Execute the statement
				ResultSet docResults = consultaDoc.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					documento = new CatDocumento(docResults.getString(1), docResults.getString(2));
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
		return documento;
	}

	public void insertarScnubvalidaine(ActualizaEstatusFlujo ocrInfo, DataSource pool, Storage storage) {

		String SQL_INSERT = "INSERT INTO scnubvalidaine (fecha_hora,estado_consulta,scarchivodigital_id,json_solicitud,json_rpta,estatus, clave_mensaje, vigencia, Clave_elector, fecha_creacion,usuario_creacion) "
				+ " VALUES (current_timestamp,?,?,?,?,?,?,?,?,current_timestamp,?)";
		System.out.println("Dato Contenido:: " + ocrInfo);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, ocrInfo.estado_consulta());
				insertDoc.setInt(2, ocrInfo.scarchivodigital_id());
				insertDoc.setString(3, ocrInfo.json_solicitud());
				insertDoc.setString(4, ocrInfo.json_rpta());
				insertDoc.setInt(5, ocrInfo.estatus());
				insertDoc.setString(6, ocrInfo.clave_mensaje_ine());
				insertDoc.setString(7, ocrInfo.vigencia_ine());
				insertDoc.setString(8, ocrInfo.clave_elector_ine());
				insertDoc.setString(9, ocrInfo.usuario_creacion());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void actualizarScnubvalidaine(ActualizaEstatusFlujo ocrInfo, DataSource pool, Storage storage) {

		String SQL_INSERT = "UPDATE scnubvalidaine set estado_consulta = ?, json_rpta = ?,estatus= ?, clave_mensaje= ?, vigencia= ?, Clave_elector= ?, fecha_modificacion= current_timestamp, usuario_modificacion= ? where scarchivodigital_id = ? ";
		System.out.println("Dato Contenido:: " + ocrInfo);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, ocrInfo.estado_consulta());
				insertDoc.setString(2, ocrInfo.json_rpta());
				insertDoc.setInt(3, ocrInfo.estatus());
				insertDoc.setString(4, ocrInfo.clave_mensaje_ine());
				insertDoc.setString(5, ocrInfo.vigencia_ine());
				insertDoc.setString(6, ocrInfo.clave_elector_ine());
				insertDoc.setString(7, ocrInfo.usuario_creacion());
				insertDoc.setInt(8, ocrInfo.scarchivodigital_id());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void insertarScnubocrine(ActualizaEstatusFlujo ocrInfo, DataSource pool, Storage storage) {

		String SQL_INSERT = "INSERT INTO SCNUBOCRINE (fecha_hora,estado_consulta,scarchivodigital_id,json_solicitud,json_rpta,estatus, fecha_creacion,usuario_creacion) "
				+ " VALUES (current_timestamp,?,?,?,?,?,current_timestamp,?)";
		System.out.println("Dato Contenido:: " + ocrInfo);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, ocrInfo.estado_consulta());
				insertDoc.setInt(2, ocrInfo.scarchivodigital_id());
				insertDoc.setString(3, ocrInfo.json_solicitud());
				insertDoc.setString(4, ocrInfo.json_rpta());
				insertDoc.setInt(5, ocrInfo.estatus());
				insertDoc.setString(6, ocrInfo.usuario_creacion());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void actualizarScnubocrine(ActualizaEstatusFlujo ocrInfo, DataSource pool, Storage storage) {

		String SQL_INSERT = "UPDATE SCNUBOCRINE SET estado_consulta = ?, json_rpta= ?, estatus= ?, fecha_modificacion = current_timestamp, usuario_modificacion= ? WHERE scarchivodigital_id = ? ";
		System.out.println("Dato Contenido:: " + ocrInfo);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, ocrInfo.estado_consulta());
				insertDoc.setString(2, ocrInfo.json_rpta());
				insertDoc.setInt(3, ocrInfo.estatus());
				insertDoc.setString(4, ocrInfo.usuario_creacion());
				insertDoc.setInt(5, ocrInfo.scarchivodigital_id());
				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void insertarScnubvalidarfcPersona(ActualizaEstatusFlujo ocrInfo, DataSource pool, Storage storage) {

		String SQL_INSERT = "INSERT INTO SCNUBVALIDARFC (fecha_hora,estado_consulta,scarchivodigital_id,json_sol_persona,json_rsp_persona,estatus,fecha_creacion,usuario_creacion) "
				+ " VALUES (current_timestamp,?,?,?,?,?,current_timestamp,?)";
		System.out.println("Dato Contenido:: " + ocrInfo);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, ocrInfo.estado_consulta());
				insertDoc.setInt(2, ocrInfo.scarchivodigital_id());
				insertDoc.setString(3, ocrInfo.json_solicitud());
				insertDoc.setString(4, ocrInfo.json_rpta());
				insertDoc.setInt(5, ocrInfo.estatus());
				insertDoc.setString(6, ocrInfo.usuario_creacion());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void actualizarScnubvalidarfcPersona(ActualizaEstatusFlujo ocrInfo, DataSource pool, Storage storage) {

		String SQL_INSERT = "UPDATE SCNUBVALIDARFC SET json_rsp_persona = ? where scarchivodigital_id = ? ";
		System.out.println("Dato Contenido:: " + ocrInfo);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, ocrInfo.json_rpta());
				insertDoc.setInt(2, ocrInfo.scarchivodigital_id());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void actualizarScarchivodigital(String personaId, int id, DataSource pool, Storage storage) {
		String SQL_INSERT = "UPDATE SCARCHIVODIGITAL SET persona_id = ? where id = ? ";
		System.out.println("Dato Contenido personaId:: " + personaId);
		System.out.println("Dato Contenido id:: " + id);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, personaId);
				insertDoc.setInt(2, id);
				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void updateScnubvalidarfc(ActualizaEstatusFlujo ocrInfo, DataSource pool, Storage storage) {

		String SQL_INSERT = "UPDATE SCNUBVALIDARFC SET estado_consulta = ?, json_solicitud = ?, json_rpta =?, estatus= ? where scarchivodigital_id =? ";
		System.out.println("Dato Contenido:: " + ocrInfo);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, ocrInfo.estado_consulta());
				insertDoc.setString(2, ocrInfo.json_solicitud());
				insertDoc.setString(3, ocrInfo.json_rpta());
				insertDoc.setInt(4, ocrInfo.estatus());
				insertDoc.setInt(5, ocrInfo.scarchivodigital_id());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void insertarScnubvalidacurp(ActualizaEstatusFlujo ocrInfo, DataSource pool, Storage storage) {

		String SQL_INSERT = "INSERT INTO SCNUBVALIDACURP (fecha_hora,estado_consulta,scarchivodigital_id,json_solicitud,json_rpta,estatus,fecha_creacion,usuario_creacion) "
				+ " VALUES (current_timestamp,?,?,?,?,?,current_timestamp,?)";
		System.out.println("Dato Contenido:: " + ocrInfo);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, ocrInfo.estado_consulta());
				insertDoc.setInt(2, ocrInfo.scarchivodigital_id());
				insertDoc.setString(3, ocrInfo.json_solicitud());
				insertDoc.setString(4, ocrInfo.json_rpta());
				insertDoc.setInt(5, ocrInfo.estatus());
				insertDoc.setString(6, ocrInfo.usuario_creacion());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void actualizarScnubvalidacurp(ActualizaEstatusFlujo ocrInfo, DataSource pool, Storage storage) {

		String SQL_INSERT = "UPDATE SCNUBVALIDACURP SET estado_consulta=?,json_rpta=?,estatus=?,fecha_modificacion=current_timestamp,usuario_modificacion=?  where scarchivodigital_id = ?";
		System.out.println("Dato Contenido:: " + ocrInfo);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, ocrInfo.estado_consulta());
				insertDoc.setString(2, ocrInfo.json_rpta());
				insertDoc.setInt(3, ocrInfo.estatus());
				insertDoc.setString(4, ocrInfo.usuario_creacion());
				insertDoc.setInt(5, ocrInfo.scarchivodigital_id());

				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void insertarSCrefeiyuPersona(SCrefeiyu screfeiyu, DataSource pool, Storage storage) {

		String SQL_INSERT = "INSERT INTO SCREFEIYU (cuenta_id, nivel, codigo, participante_id, fecha_hora, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion) "
				+ "  VALUES (?,?,?,?,current_timestamp,current_timestamp,current_timestamp,?,?) " + "      ON CONFLICT (cuenta_id) "
				+ "      DO "
				+ " UPDATE SET nivel = EXCLUDED.nivel, codigo = EXCLUDED.codigo, participante_id = EXCLUDED.participante_id, "
				+ " fecha_modificacion = current_timestamp, usuario_modificacion = EXCLUDED.usuario_modificacion";
		System.out.println("Dato Contenido:: " + screfeiyu);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, screfeiyu.cuenta_id());
				insertDoc.setString(2, screfeiyu.nivel());
				insertDoc.setString(3, screfeiyu.codigo());
				insertDoc.setString(4, screfeiyu.participante_id());
				insertDoc.setString(5, screfeiyu.usuario_creacion());
				insertDoc.setString(6, screfeiyu.usuario_modificacion());
				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
		}
	}

	public void insertarScnubvalidarfcPersona(SCcorePersona scorePersona, DataSource pool, Storage storage) {

		String SQL_INSERT = "INSERT INTO SCCOREPERSONA (persona_id,fecha_hora,curp,rfc,nombres,paterno,materno,fecha_nacimiento,estado_nacimiento,tipo_persona,fecha_creacion,fecha_modificacion,usuario_creacion,usuario_modificacion) "
				+ "     VALUES (?,current_timestamp,?,?,?,?,?,?,?,?,current_timestamp,current_timestamp,?,?)"
				+ "         ON CONFLICT (persona_id) " + "         DO "
				+ " UPDATE SET curp = EXCLUDED.curp, rfc = EXCLUDED.rfc, nombres = EXCLUDED.nombres, paterno = EXCLUDED.paterno, "
				+ " materno = EXCLUDED.materno, fecha_nacimiento = EXCLUDED.fecha_nacimiento, estado_nacimiento = EXCLUDED.estado_nacimiento, "
				+ " tipo_persona = EXCLUDED.tipo_persona, fecha_modificacion = current_timestamp, usuario_modificacion = EXCLUDED.usuario_modificacion";
		System.out.println("Dato Contenido:: " + scorePersona);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement insertDoc = conn.prepareStatement(SQL_INSERT)) {
				insertDoc.setString(1, scorePersona.persona_id());
				insertDoc.setString(2, scorePersona.curp());
				insertDoc.setString(3, scorePersona.rfc());
				insertDoc.setString(4, scorePersona.nombres());
				insertDoc.setString(5, scorePersona.paterno());
				insertDoc.setString(6, scorePersona.materno());
				insertDoc.setString(7, scorePersona.fecha_nacimiento());
				insertDoc.setString(8, scorePersona.estado_nacimiento());
				insertDoc.setString(9, scorePersona.tipo_persona());
				insertDoc.setString(10, scorePersona.usuario_creacion());
				insertDoc.setString(11, scorePersona.usuario_modificacion());
				// Execute the statement
				int row = insertDoc.executeUpdate();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				// rows affected
				System.out.println("Archivo registrado >>>>>>>> " + row); // 1
			}
		} catch (SQLException ex) {
			// If something goes wrong, the application needs to react appropriately. This
			// might mean
			// getting a new connection and executing the query again, or it might mean
			// redirecting the
			// user to a different page to let them know something went wrong.
			ex.printStackTrace();
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
	
	public boolean banderaFlujo(DataSource pool) throws SQLException {
		String SQL_SELECT = "SELECT valor FROM scparametria WHERE id = 'ACTIVAR_VALIDACION_NUBARIUM'";
		boolean nubariumActivo = false;
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			// PreparedStatements are compiled by the database immediately and executed at a
			// later date.
			// Most databases cache previously compiled queries, which improves efficiency.
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				// Execute the statement
				ResultSet docResults = consultaDoc.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					nubariumActivo = docResults.getString(1).equals("1");
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
		return nubariumActivo;
	}
	
	
	public boolean validacionNewBank(DataSource pool) throws SQLException {
		String SQL_SELECT = "SELECT valor FROM scparametria WHERE id = 'VALIDACION_NUBARIUM_NEW_BANK'";
		boolean nubariumActivo = false;
		try (Connection conn = pool.getConnection()) {
			System.out.println("Encontro Conexion y consulta la informacion.....");
			// PreparedStatements are compiled by the database immediately and executed at a
			// later date.
			// Most databases cache previously compiled queries, which improves efficiency.
			try (PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {
				// Execute the statement
				ResultSet docResults = consultaDoc.executeQuery();
				// Convert a ResultSet into DocumentoRegistroEntity objects
				while (docResults.next()) {
					nubariumActivo = docResults.getString(1).equals("1");
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
		return nubariumActivo;
	}
}
