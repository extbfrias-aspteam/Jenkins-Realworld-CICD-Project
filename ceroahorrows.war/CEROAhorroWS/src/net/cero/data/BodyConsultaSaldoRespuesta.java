package net.cero.data;

import java.util.Map;

public class BodyConsultaSaldoRespuesta {

	private Map<String, Object> params;

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "BodyConsultaSaldoRespuesta{" +
				"params=" + params +
				'}';
	}
}
