package com.asp.eiyu.api.auth.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Connection;

import javax.sql.DataSource;

import com.asp.eiyu.api.auth.entity.ResponseDTO;
import com.asp.eiyu.api.auth.entity.UsuarioAuthDTO;

public class UsuarioRepository {
	
	public ResponseDTO<UsuarioAuthDTO> findUsernameAndPassword(DataSource pool, String user, String pass) {
		String SQL_SELECT = "SELECT sc.* FROM scusuario sc WHERE sc.username = ?";
		
		ResponseDTO<UsuarioAuthDTO> res;

		try (Connection conn = pool.getConnection();
				PreparedStatement consultaDoc = conn.prepareStatement(SQL_SELECT)) {

			System.out.println("Conexión establecida. Ejecutando consulta...");

			// Asignar parámetros de forma segura
			consultaDoc.setString(1, user);
			//consultaDoc.setString(2, pass);

			try (ResultSet docResults = consultaDoc.executeQuery()) {
				if (docResults.next()) {
					Integer idPblu = obtenerIdPblu(docResults);
					UsuarioAuthDTO usuario = new UsuarioAuthDTO(
							docResults.getString("username"),
							docResults.getString("password"),
							idPblu);
					res = new ResponseDTO<>("0", "Acceso correcto.", usuario);
				} else {
					res = new ResponseDTO<>("1", "Credenciales incorrectas.", null);
				}
			
			}
			consultaDoc.close();
			conn.close();
		} catch (SQLException ex) {
			res = new ResponseDTO<>("2", "Error en la autenticación.", null);
		}
		return res;
	}

	private Integer obtenerIdPblu(ResultSet resultSet) throws SQLException {
		String[] candidatos = {"idpblu", "id_pblu", "idPblu"};
		ResultSetMetaData metadata = resultSet.getMetaData();
		for (String candidato : candidatos) {
			for (int i = 1; i <= metadata.getColumnCount(); i++) {
				if (candidato.equalsIgnoreCase(metadata.getColumnName(i))) {
					Object valor = resultSet.getObject(i);
					if (valor instanceof Number number) {
						return number.intValue();
					}
					if (valor instanceof String stringValue && !stringValue.isBlank()) {
						return Integer.parseInt(stringValue);
					}
					return null;
				}
			}
		}
		return null;
	}

}
