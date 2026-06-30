package com.asp.digitalizacion.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.asp.digitalizacion.exception.DigitalizacionException;
import com.asp.digitalizacion.model.dto.ApiEIYUResponse;
import com.asp.digitalizacion.model.dto.BloqueoCuentaEIYURequest;
import com.asp.digitalizacion.model.dto.BloqueoCuentaRequest;
import com.asp.digitalizacion.model.dto.BloqueoCuentaResponse;
import com.asp.digitalizacion.model.dto.DigitalizacionAPIResponse;
import com.asp.digitalizacion.model.dto.ErrorBloqueoCuentaDTO;
import com.asp.digitalizacion.model.entity.CuentaEntity;
import com.asp.digitalizacion.repository.ICuentaRepository;
import com.asp.digitalizacion.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CuentaService implements ICuentasService {

	@Autowired
	private ICuentaRepository cuentaRepository;

	@Value("${eiyu.url.activa.cuenta}")
	private String urlEIYUActivaCuenta;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	@Transactional
	public DigitalizacionAPIResponse<?> bloquearCuenta(BloqueoCuentaRequest request) {

		if (request.getCuentas().size() > 100) {
			throw new DigitalizacionException("El número máximo de cuentas a bloquear es 100", HttpStatus.BAD_REQUEST);
		}

		List<ErrorBloqueoCuentaDTO> errores = new ArrayList<>();
		Integer cuentasActualizadas = 0;

		for (String cuenta : request.getCuentas()) {
			if (cuenta.isBlank() || cuenta.length() != 18) {
				ErrorBloqueoCuentaDTO error = new ErrorBloqueoCuentaDTO();
				error.setCuenta(cuenta);
				error.setMensaje("Número de cuenta inválido");
				errores.add(error);
				continue;
			}
			Optional<CuentaEntity> cuentaOpt = Optional.empty();
			try {
				cuentaOpt = cuentaRepository.findByClabeAndActivo(cuenta, true);
			} catch (DataAccessException e) {
				log.error("Error al accesar a la BD {}: {}", cuenta, e.getMessage());
				throw new DigitalizacionException("Error al acceder a la base de datos",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			if (cuentaOpt.isEmpty()) {
				ErrorBloqueoCuentaDTO error = new ErrorBloqueoCuentaDTO();
				error.setCuenta(cuenta);
				error.setMensaje("Cuenta no existe o no se encuentra activa.");
				errores.add(error);
				continue;
			}

			CuentaEntity cuentaEntity = cuentaOpt.get();
			cuentaEntity.setBloqueo(request.getTipoBloqueo());
			cuentaEntity.setFechaModificacion(LocalDateTime.now());

			try {
				cuentaRepository.save(cuentaEntity);
				cuentasActualizadas++;
			} catch (DataAccessException e) {
				log.error("Error al actualizar la cuenta {}: {}", cuenta, e.getMessage());
				ErrorBloqueoCuentaDTO error = new ErrorBloqueoCuentaDTO();
				error.setCuenta(cuenta);
				error.setMensaje("Error al actualizar la cuenta");
				errores.add(error);
			}
		}

		BloqueoCuentaResponse bloqueoResponse = new BloqueoCuentaResponse();
		bloqueoResponse.setCuentasActualizadas(cuentasActualizadas);
		bloqueoResponse.setErrores(errores);

		DigitalizacionAPIResponse<BloqueoCuentaResponse> response = new DigitalizacionAPIResponse<>();
		response.setCodigo("OK");
		response.setContenido(bloqueoResponse);
		response.setMensaje((bloqueoResponse.getErrores().isEmpty()) ? "Operación realizada con éxito"
				: "Operación actualizada con errores");

		return response;
	}

	@Override
	public DigitalizacionAPIResponse<?> activarCuentaEIYU(BloqueoCuentaEIYURequest request) {

		ResponseEntity<ApiEIYUResponse> apiResponse = null;
		try {
			String token = jwtUtil.generarToken();
			log.info("Token generado para EIYU: " + token);
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(token);
			
			Map<String, String> body = new HashMap<>();
			body.put("clabe", request.getClabe());

			HttpEntity<Map<String, String>> requestHttp = new HttpEntity<>(body, headers);

			log.info("Url: " + urlEIYUActivaCuenta);
			apiResponse = restTemplate.exchange(urlEIYUActivaCuenta, HttpMethod.POST, requestHttp,
					ApiEIYUResponse.class);
		} catch (Exception ex) {
			log.error("Ha ocurrido un error inesperado. Exception {} {}", ex.getMessage() + " " + ex,
					ex.getStackTrace());
			throw new DigitalizacionException("Hubo un error al consumir el servicio EIYU Activar Cuenta: " + ex.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		log.info("Estatus respuesta: " + apiResponse.getStatusCode());

		if (!apiResponse.getStatusCode().is2xxSuccessful()) {

			throw new DigitalizacionException("No se pudo obtener una respuesta exitosa del servicio EIYU Activar Cuenta",
					HttpStatus.BAD_REQUEST);
		}

		ApiEIYUResponse respuestaEIYU = apiResponse.getBody();
		
		DigitalizacionAPIResponse<ApiEIYUResponse> response = new DigitalizacionAPIResponse<>();
		response.setCodigo((respuestaEIYU.getSuccess()) ? "OK": "ERROR");
		response.setContenido(respuestaEIYU);
		response.setMensaje((respuestaEIYU.getSuccess()) ? "Operación realizada con éxito"
				: "No se pudo activar la cuenta en EIYU");

		return response;

	}

}
