package com.asp.digitalizacion.exception;

import java.util.ArrayList;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.asp.digitalizacion.model.dto.DigitalizacionAPIResponse;






@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<DigitalizacionAPIResponse<?>> excepcionGeneral(Exception ex){
		DigitalizacionAPIResponse response = new DigitalizacionAPIResponse<>();
		response.setCodigo("ERROR");
		response.setMensaje("Error general en el sistema: " +ex.getMessage());
		response.setContenido(new ArrayList<>());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<DigitalizacionAPIResponse<?>> noHandlerFoundException(NoHandlerFoundException ex){
		DigitalizacionAPIResponse response = new DigitalizacionAPIResponse<>();
		response.setCodigo("ERROR");
		response.setMensaje("Error general en el sistema: " +ex.getMessage());
		response.setContenido(new ArrayList<>());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<DigitalizacionAPIResponse<?>> constraintViolationException(ConstraintViolationException ex){
		DigitalizacionAPIResponse response = new DigitalizacionAPIResponse<>();
		response.setCodigo("ERROR");
		response.setMensaje("Error general en el sistema: " +ex.getMessage());
		response.setContenido(new ArrayList<>());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<DigitalizacionAPIResponse<?>> methodArgumentNotValidException(MethodArgumentNotValidException ex){
		DigitalizacionAPIResponse response = new DigitalizacionAPIResponse<>();
		String errores = "";
		
		for(ObjectError error : ex.getBindingResult().getAllErrors()) {
			errores = errores +error.getDefaultMessage()+ " ";
		}

		response.setCodigo("ERROR");
		response.setMensaje("Se encontraron los siguientes errores en los datos de entrada: " + errores);
		response.setContenido(new ArrayList<>());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DigitalizacionException.class)
	public ResponseEntity<DigitalizacionAPIResponse<?>> cOASBussinesException(DigitalizacionException ex){
		DigitalizacionAPIResponse response = new DigitalizacionAPIResponse<>();
		response.setCodigo("ERROR");
		response.setMensaje(ex.getMensaje());
		response.setContenido(new ArrayList<>());
		return new ResponseEntity<>(response, ex.getHttpStatus());
	}
}
