
package net.cero.ahorro.spei.generaclabe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import net.cero.ahorro.spei.data.HeaderWS;


/**
 * <p>Java class for GeneraClabeRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeneraClabeRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://generaClabe.spei.ws.izel.net/}headerWS" minOccurs="0"/>
 *         &lt;element name="productoId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="claveAplicacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referencia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="claveRegion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="obtenerClabe" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeneraClabeRequest", propOrder = {
    "header",
    "productoId",
    "claveAplicacion",
    "referencia",
    "claveRegion",
    "obtenerClabe"
})
public class GeneraClabeRequest {

    protected HeaderWS header;
    protected Long productoId;
    protected String claveAplicacion;
    protected String referencia;
    protected String claveRegion;
    protected Integer obtenerClabe;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderWS }
     *     
     */
    public HeaderWS getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderWS }
     *     
     */
    public void setHeader(HeaderWS value) {
        this.header = value;
    }

    /**
     * Gets the value of the productoId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProductoId() {
        return productoId;
    }

    /**
     * Sets the value of the productoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProductoId(Long value) {
        this.productoId = value;
    }

    /**
     * Gets the value of the claveAplicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaveAplicacion() {
        return claveAplicacion;
    }

    /**
     * Sets the value of the claveAplicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaveAplicacion(String value) {
        this.claveAplicacion = value;
    }

    /**
     * Gets the value of the referencia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * Sets the value of the referencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencia(String value) {
        this.referencia = value;
    }

    /**
     * Gets the value of the claveRegion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaveRegion() {
        return claveRegion;
    }

    /**
     * Sets the value of the claveRegion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaveRegion(String value) {
        this.claveRegion = value;
    }

    /**
     * Gets the value of the obtenerClabe property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getObtenerClabe() {
        return obtenerClabe;
    }

    /**
     * Sets the value of the obtenerClabe property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setObtenerClabe(Integer value) {
        this.obtenerClabe = value;
    }

}
