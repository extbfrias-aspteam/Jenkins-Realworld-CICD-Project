package mx.net.asp.asp_pago_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthHandlerError implements AuthenticationEntryPoint {

	private static final String ERROR = "error";
	private static final String MESSAGE = "message";
	private static final String EXCEPTION = "exception";
	private static final String PATH = "path";
	private static final String TIMESTAMP = "timestamp";

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException {

		Map<String, Object> mapException = buildExceptionResponse(request);

		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		// Escribir la respuesta JSON
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), mapException);
	}

	private Map<String, Object> buildExceptionResponse(HttpServletRequest request) {
		Map<String, Object> mapException = new HashMap<>();
		mapException.put(ERROR, "401");
		mapException.put(MESSAGE, "No estás autorizado para acceder a este recurso");
		mapException.put(EXCEPTION, "No autorizado");
		mapException.put(PATH, request.getServletPath());
		mapException.put(TIMESTAMP, (new Date()).getTime());
		return mapException;
	}
}