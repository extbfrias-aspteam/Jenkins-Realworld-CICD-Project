package com.asp.eiyu.api.auth.service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.asp.eiyu.api.auth.entity.ResponseDTO;
import com.asp.eiyu.api.auth.entity.UsuarioAuthDTO;
import com.asp.eiyu.api.auth.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class UsuarioService {

	UsuarioRepository us = new UsuarioRepository();

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public ResponseDTO<String> auth(DataSource pool, String user, String pass, String secretKeyString) {
	
		ResponseDTO<UsuarioAuthDTO> res = us.findUsernameAndPassword(pool, user, pass);
		if (res.getEiyuResponseCode().equals("0")) {
			UsuarioAuthDTO usuario = res.getContent();
			if (usuario == null) {
				return new ResponseDTO<>("2", "Error en la autenticación.", null);
			}
			if (usuario.idPblu() == null || usuario.idPblu() <= 0) {
				return new ResponseDTO<>("1", "El usuario no tiene un idPblu configurado.", null);
			}
			if(this.verifyPassword(pass, usuario.passwordHash())) {
				res.setContent(usuario);
				return new ResponseDTO<>("0", "Acceso correcto.", generarToken(secretKeyString, usuario.username(), usuario.idPblu()));
			}else {
				return new ResponseDTO<>("1", "Contraseña incorrecta.", null);
			}
			
		}
		return new ResponseDTO<>(res.getEiyuResponseCode(), res.getDescripcion(), null);
	}

	public String generarToken(String secretKeyString, String nombreUsuario, Integer idPblu) {

		String secretString = secretKeyString;
		String encodedKey = Base64.getEncoder().encodeToString(secretString.getBytes());
		Key secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(encodedKey));
		// Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

		long expirationTime = 1000L * 60 * 60 * 24; // 1 dia en milisegundos

		// Crear el token
		@SuppressWarnings("deprecation")
		String jwt = Jwts.builder().setSubject(nombreUsuario) // Usuario al que pertenece el token
				.setIssuer("Digitalización") // Quien emite el token
				.claim("idPblu", idPblu)
				.setIssuedAt(new Date()) // Fecha de emisión
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Expiración
				.signWith(secretKey) // Firmar con la clave secreta
				.compact();

		System.out.println("Token JWT generado:");
		System.out.println(jwt);
		return jwt;
	}


	public static String hashPassword(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	public static boolean verifyPassword(String rawPassword, String hashedPassword) {
		return encoder.matches(rawPassword, hashedPassword);
	}

}
