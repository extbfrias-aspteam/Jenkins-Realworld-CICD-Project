package functions.dto;

import java.util.List;

public record CuentaResumenResponse(
		boolean success,
		String message,
		CuentaResumenData data,
		List<String> errors) {
}
