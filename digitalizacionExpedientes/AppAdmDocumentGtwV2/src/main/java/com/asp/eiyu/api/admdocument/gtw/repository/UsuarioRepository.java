package com.asp.eiyu.api.admdocument.gtw.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.DataSource;

import javax.sql.DataSource;
public class UsuarioRepository {

	public boolean consultaUserPass(String username, String password, DataSource pool) {
	
		String SQL_SELECT = "SELECT username FROM scusuario WHERE username = ? AND password = ?";

		try (Connection conn = pool.getConnection()) {
			System.out.println("Conexión establecida, ejecutando consulta...");

			try (PreparedStatement consultaUser = conn.prepareStatement(SQL_SELECT)) {
				consultaUser.setString(1, username);
				consultaUser.setString(2, password);

				try (ResultSet rs = consultaUser.executeQuery()) {
					return rs.next(); // Retorna true si hay coincidencia
				}
				
			}
		} catch (SQLException ex) {
			return false;
		}
	}

}
