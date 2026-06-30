package com.asp.digitalizacion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService implements IAuthService {

	@Value("${auth.user}")
	private String validUser;

	@Value("${auth.password}")
	private String validPassword;

	@Override
	public void validate(String username, String password) {
		if (!validUser.equals(username) || !validPassword.equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

	}

}
