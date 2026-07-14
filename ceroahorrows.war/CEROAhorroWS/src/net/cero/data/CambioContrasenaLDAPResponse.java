package net.cero.data;

public class CambioContrasenaLDAPResponse {
	private String codeEstatus;
	private String messageStatus;
	
	public String getCodeEstatus() {
		return codeEstatus;
	}
	public void setCodeEstatus(String codeEstatus) {
		this.codeEstatus = codeEstatus;
	}
	public String getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}
	@Override
	public String toString() {
		return "CambioContrasenaLDAPResponse [codeEstatus=" + codeEstatus + ", messageStatus=" + messageStatus + "]";
	}
}
