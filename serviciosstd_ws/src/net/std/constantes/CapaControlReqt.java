package net.std.constantes;

import net.cero.ws.data.HeaderWS;

public class CapaControlReqt {
	private HeaderWS header;
	private String cuerpo;
	/**
	 * @return the cuerpo
	 */
	public String getCuerpo() {
		return cuerpo;
	}
	/**
	 * @param cuerpo the cuerpo to set
	 */
	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
	/**
	 * @return the header
	 */
	public HeaderWS getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(HeaderWS header) {
		this.header = header;
	}
}
