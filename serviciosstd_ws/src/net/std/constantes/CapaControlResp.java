package net.std.constantes;

import java.io.Serializable;

public class CapaControlResp implements Serializable{
	private static final long serialVersionUID = 1L;
	private String codeStatus;
	private String messageStatus;
	private String seControlStatus;
	private String seControlMsgStatus;
	private String seSeguridadStatus;
	private String seSeguridadMsgStatus;
	private String seDisponibilidadStatus;
	private String seDisponibilidadMsgStatus;
	private String seAuditoriaMessage;
	/**
	 * @return the codeStatus
	 */
	public String getCodeStatus() {
		return codeStatus;
	}
	/**
	 * @param codeStatus the codeStatus to set
	 */
	public void setCodeStatus(String codeStatus) {
		this.codeStatus = codeStatus;
	}
	/**
	 * @return the messageStatus
	 */
	public String getMessageStatus() {
		return messageStatus;
	}
	/**
	 * @param messageStatus the messageStatus to set
	 */
	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}
	/**
	 * @return the seControlStatus
	 */
	public String getSeControlStatus() {
		return seControlStatus;
	}
	/**
	 * @param seControlStatus the seControlStatus to set
	 */
	public void setSeControlStatus(String seControlStatus) {
		this.seControlStatus = seControlStatus;
	}
	/**
	 * @return the seControlMsgStatus
	 */
	public String getSeControlMsgStatus() {
		return seControlMsgStatus;
	}
	/**
	 * @param seControlMsgStatus the seControlMsgStatus to set
	 */
	public void setSeControlMsgStatus(String seControlMsgStatus) {
		this.seControlMsgStatus = seControlMsgStatus;
	}
	/**
	 * @return the seSeguridadStatus
	 */
	public String getSeSeguridadStatus() {
		return seSeguridadStatus;
	}
	/**
	 * @param seSeguridadStatus the seSeguridadStatus to set
	 */
	public void setSeSeguridadStatus(String seSeguridadStatus) {
		this.seSeguridadStatus = seSeguridadStatus;
	}
	/**
	 * @return the seSeguridadMsgStatus
	 */
	public String getSeSeguridadMsgStatus() {
		return seSeguridadMsgStatus;
	}
	/**
	 * @param seSeguridadMsgStatus the seSeguridadMsgStatus to set
	 */
	public void setSeSeguridadMsgStatus(String seSeguridadMsgStatus) {
		this.seSeguridadMsgStatus = seSeguridadMsgStatus;
	}
	/**
	 * @return the seDisponibilidadStatus
	 */
	public String getSeDisponibilidadStatus() {
		return seDisponibilidadStatus;
	}
	/**
	 * @param seDisponibilidadStatus the seDisponibilidadStatus to set
	 */
	public void setSeDisponibilidadStatus(String seDisponibilidadStatus) {
		this.seDisponibilidadStatus = seDisponibilidadStatus;
	}
	/**
	 * @return the seDisponibilidadMsgStatus
	 */
	public String getSeDisponibilidadMsgStatus() {
		return seDisponibilidadMsgStatus;
	}
	/**
	 * @param seDisponibilidadMsgStatus the seDisponibilidadMsgStatus to set
	 */
	public void setSeDisponibilidadMsgStatus(String seDisponibilidadMsgStatus) {
		this.seDisponibilidadMsgStatus = seDisponibilidadMsgStatus;
	}
	/**
	 * @return the seAuditoriaMessage
	 */
	public String getSeAuditoriaMessage() {
		return seAuditoriaMessage;
	}
	/**
	 * @param seAuditoriaMessage the seAuditoriaMessage to set
	 */
	public void setSeAuditoriaMessage(String seAuditoriaMessage) {
		this.seAuditoriaMessage = seAuditoriaMessage;
	}
}
