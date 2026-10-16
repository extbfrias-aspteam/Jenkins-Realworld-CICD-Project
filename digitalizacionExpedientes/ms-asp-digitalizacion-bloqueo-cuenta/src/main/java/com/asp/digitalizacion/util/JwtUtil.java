package com.asp.digitalizacion.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.asp.digitalizacion.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;
	
//	@Value("${eiyu.jwt.secret}")
//	private String secretEiyu;
//	
//	@Value("${eiyu.jwt.issuer}")
//	private String issuertEiyu;

	@Value("${jwt.expiration}")
	private long expiration;
	
//	@Value("${eiyu.jwt.audience}")
//	private String audiencetEiyu;
	
	private final JwtProperties props;
	
	
//	private static String secretEiyuStatic;
//	
//	private static String issuertEiyuStatic;
//	
//	private static String audiencetEiyuStatic;
	
	
	 public JwtUtil(JwtProperties props) {
	        this.props = props;
	    }
	
	

	private Key getKey() {
	    byte[] keyBytes = Decoders.BASE64.decode(secret);
	    return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuer("ms-digitalizacion").setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getKey(), SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	public boolean isTokenValid(String token) {
		try {
			getClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
	}
	
	 public String generarToken() {
	        return Jwts.builder()
	                .setIssuer(props.getIssuertEiyu())
	                .setAudience(props.getAudiencetEiyu())
	                .setSubject(props.getAudiencetEiyu()) // opcional
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 hora
	                .signWith(SignatureAlgorithm.HS256, props.getSecretEiyu().getBytes())
	                .compact();
	    }
}
