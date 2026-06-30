package com.asp.digitalizacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asp.digitalizacion.model.dto.BloqueoCuentaEIYURequest;
import com.asp.digitalizacion.model.dto.BloqueoCuentaRequest;
import com.asp.digitalizacion.model.dto.DigitalizacionAPIResponse;
import com.asp.digitalizacion.service.ICuentasService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/digitalizacion/cuentas")
@RequiredArgsConstructor
public class CuentasController {
	
	@Autowired
	private ICuentasService cuentasService;
	
	
	@PutMapping("/bloqueo")
    public ResponseEntity<DigitalizacionAPIResponse<?>>  bloqueo(@RequestBody @Valid BloqueoCuentaRequest request){

		DigitalizacionAPIResponse<?>response = cuentasService.bloquearCuenta(request);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
	
	@PostMapping("/activar")
    public ResponseEntity<DigitalizacionAPIResponse<?>>  activar(@RequestBody @Valid BloqueoCuentaEIYURequest request){

		DigitalizacionAPIResponse<?>response = cuentasService.activarCuentaEIYU(request);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
	
	
	

}
