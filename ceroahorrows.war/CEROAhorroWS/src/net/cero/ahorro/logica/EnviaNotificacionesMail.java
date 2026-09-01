/*
 * @(#)RegistraDisposicionLinea.java 1.0 05/07/19 
 * 
 */
package net.cero.ahorro.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import net.cero.data.Atachments;

/**
 * Logica de negocio para envio de reporte.
 * @author Israel
 * @version 1.0 05/07/19
 */
@Log4j2
public class EnviaNotificacionesMail{

	private String PUERTO_SMTP = "465";
	private String HOST_CORREO = "mail.aspintegraopciones.com";
	private String USUARIO_CORREO = "procesos@aspintegraopciones.com";
	private String PASS_CORREO = "51dc26$";
	public void sendMail(String from,String to,String subject,String bodyText,List<Atachments> atachmentsList){
		try {
			Properties props = new Properties();
		    props.put("mail.transport.protocol", "smtps");

	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.socketFactory.port", PUERTO_SMTP);
	        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		    props.put("mail.smtp.auth", "true");
		    props.put("mail.smtp.host", HOST_CORREO);
		    props.put("mail.smtp.port", PUERTO_SMTP);
		    props.put("mail.smtp.ssl.checkserveridentity", "false");
		    props.put("mail.smtp.ssl.trust", "*");
		    props.put("mail.smtp.debug", "false");
		    props.put("mail.smtp.socketFactory.fallback", "false");
		    Session session = Session.getInstance(props,
		            new javax.mail.Authenticator() {
		                protected PasswordAuthentication getPasswordAuthentication() {
		                    return new PasswordAuthentication(USUARIO_CORREO, PASS_CORREO);
		                }
		            });
		    session.setDebug(true);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = {new InternetAddress(to)};
			message.setRecipients(Message.RecipientType.TO, toAddress);
			message.setSubject(subject);
			message.setSentDate(new Date());
			
			MimeBodyPart body = new MimeBodyPart();
			body.setText(bodyText);
			body.setDisposition(Part.INLINE);
			body.setHeader("Content-Type", "text/html; charset=utf-8");
	
			Multipart multipart = new MimeMultipart();
			message.setContent(multipart);
			message.setHeader("Content-Type", multipart.getContentType());
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(body);
			
			/**/////////////////////////Seccion donde se maneja los archivos adjuntos//////////////////////////////////////***/
			if(atachmentsList != null){
				if(atachmentsList.size() > 0){
					for(Atachments atachment : atachmentsList){
						
						MimeBodyPart part = new MimeBodyPart();
						part.setFileName(atachment.getTheFilename());
						part.setDisposition(Part.ATTACHMENT);
				
						DataSource dataSource = getDataSource(atachment.getTheAttachmentBytes(), atachment.getTheContentType());
						part.setDataHandler(new DataHandler(dataSource));
						part.setHeader("Content-Type", dataSource.getContentType());
						part.setHeader("Content-Transfer-Encoding", "base64");
						
						mp.addBodyPart(part);
					}
				}
			}
			/**/////////////////////////Seccion donde se maneja los archivos adjuntos//////////////////////////////////////***/
			
			message.setContent(mp);
						
			Transport transport = session.getTransport("smtps");
	        transport.send(message);
		} catch (javax.mail.MessagingException e) {
			log.error("MailerBean.sendHtmlMail", e);
			log.info("Error en sendMail:: " + e.getMessage());
		}
	}
	
	public static DataSource getDataSource(final byte[] bytes, final String theContentType) {

		return new DataSource() {
			public InputStream getInputStream()
			{
			return new ByteArrayInputStream(bytes);
			}
	
			public OutputStream getOutputStream()
			{
			throw new UnsupportedOperationException();
			}
	
			public String getContentType() {
			return theContentType;
			}
	
			public String getName() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
