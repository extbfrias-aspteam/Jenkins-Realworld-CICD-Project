package com.asp.digitalizacion.service;

import com.asp.digitalizacion.model.dto.BloqueoCuentaEIYURequest;
import com.asp.digitalizacion.model.dto.BloqueoCuentaRequest;
import com.asp.digitalizacion.model.dto.DigitalizacionAPIResponse;

public interface ICuentasService {
	
	DigitalizacionAPIResponse<?> bloquearCuenta(BloqueoCuentaRequest request);
	
	DigitalizacionAPIResponse<?> activarCuentaEIYU(BloqueoCuentaEIYURequest request);

}
