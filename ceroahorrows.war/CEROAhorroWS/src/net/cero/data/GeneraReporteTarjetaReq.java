package net.cero.data;

public class GeneraReporteTarjetaReq {

	private String mailFrom;
	private String mailTo;
	private String subject;
	private String mailBody;
	private String cuentaAhorro;
	private Integer accesoId;
	
	/**
	 * @return the mailFrom
	 */
	public String getMailFrom() {
		return mailFrom;
	}
	/**
	 * @param mailFrom the mailFrom to set
	 */
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	/**
	 * @return the mailTo
	 */
	public String getMailTo() {
		return mailTo;
	}
	/**
	 * @param mailTo the mailTo to set
	 */
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the mailBody
	 */
	public String getMailBody() {
		return mailBody;
	}
	/**
	 * @param mailBody the mailBody to set
	 */
	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}
	/**
	 * @return the cuentaAhorro
	 */
	public String getCuentaAhorro() {
		return cuentaAhorro;
	}
	/**
	 * @param cuentaAhorro the cuentaAhorro to set
	 */
	public void setCuentaAhorro(String cuentaAhorro) {
		this.cuentaAhorro = cuentaAhorro;
	}
	/**
	 * @return the accesoId
	 */
	public Integer getAccesoId() {
		return accesoId;
	}
	/**
	 * @param accesoId the accesoId to set
	 */
	public void setAccesoId(Integer accesoId) {
		this.accesoId = accesoId;
	}
	
}
