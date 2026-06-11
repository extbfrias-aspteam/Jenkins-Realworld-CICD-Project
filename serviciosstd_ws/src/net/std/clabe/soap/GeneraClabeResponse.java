
package net.std.clabe.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import net.cero.ws.data.RespuestaXML;




/**
 * <p>Java class for GeneraClabeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeneraClabeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://generaClabe.spei.ws.izel.net/}respuestaXML" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeneraClabeResponse", propOrder = {
    "_return"
})
public class GeneraClabeResponse {

    @XmlElement(name = "return")
    protected RespuestaXML _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaXML }
     *     
     */
    public RespuestaXML getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaXML }
     *     
     */
    public void setReturn(RespuestaXML value) {
        this._return = value;
    }

}
