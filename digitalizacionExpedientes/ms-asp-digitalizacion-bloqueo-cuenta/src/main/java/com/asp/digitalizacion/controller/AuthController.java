package com.asp.digitalizacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asp.digitalizacion.model.dto.AuthRequest;
import com.asp.digitalizacion.service.AuthService;
import com.asp.digitalizacion.util.JwtUtil;


@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;
    
    @PostMapping("/token")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequest request) {

        //  Validar contra variables de entorno
        authService.validate(request.getUsername(), request.getPassword());

        //  Generar JWT
        String token = jwtUtil.generateToken(request.getUsername());

        return ResponseEntity.ok(token);
    }

}
