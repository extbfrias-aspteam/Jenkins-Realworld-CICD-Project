package net.cero.data;

import java.io.Serializable;

public class ConsultaSaldoRespuesta implements Serializable{
	
		private BodyConsultaSaldoRespuesta body;
		
		public BodyConsultaSaldoRespuesta getBody() {
			return body;
		}
		public void setBody(BodyConsultaSaldoRespuesta body) {
			this.body = body;
		}

	@Override
	public String toString() {
		return "ConsultaSaldoRespuesta{" +
				"body=" + body +
				'}';
	}
}

