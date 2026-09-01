
package net.cero.ahorro.spei.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for respuestaXML complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="respuestaXML">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="body" type="{http://grupoGuardar.cat.ws.izel.net/}respuestaBodyXML" minOccurs="0"/>
 *         &lt;element name="errores" type="{http://grupoGuardar.cat.ws.izel.net/}respuestaErrorXML" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaXML", propOrder = {
    "body",
    "errores"
})
public class RespuestaXML {

    protected RespuestaBodyXML body;
    protected RespuestaErrorXML errores;

    public RespuestaXML(){
		body = new RespuestaBodyXML();
		errores = new RespuestaErrorXML();
	}
    
    /**
     * Gets the value of the body property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaBodyXML }
     *     
     */
    public RespuestaBodyXML getBody() {
        return body;
    }

    /**
     * Sets the value of the body property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaBodyXML }
     *     
     */
    public void setBody(RespuestaBodyXML value) {
        this.body = value;
    }

    /**
     * Gets the value of the errores property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaErrorXML }
     *     
     */
    public RespuestaErrorXML getErrores() {
        return errores;
    }

    /**
     * Sets the value of the errores property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaErrorXML }
     *     
     */
    public void setErrores(RespuestaErrorXML value) {
        this.errores = value;
    }

}
