
package net.cero.ahorro.spei.enviospei.nuevospeicero;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NuevoSpeiCeroRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NuevoSpeiCeroRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombreOrdenante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cuentaOrdenante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rfcOrdenante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoCuentaOrdenanteId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreBeneficiario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cuentaBeneficiario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rfcBeneficiario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoCuentaBeneficiarioId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="conceptoPago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoPago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="iva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="institucionBenId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="operacionId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="usuarioId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="aplicacion" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="correoElectronico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="correoBeneficiario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NuevoSpeiCeroRequest", propOrder = {
    "nombreOrdenante",
    "cuentaOrdenante",
    "rfcOrdenante",
    "tipoCuentaOrdenanteId",
    "nombreBeneficiario",
    "cuentaBeneficiario",
    "rfcBeneficiario",
    "tipoCuentaBeneficiarioId",
    "conceptoPago",
    "tipoPago",
    "monto",
    "iva",
    "institucionBenId",
    "operacionId",
    "usuarioId",
    "aplicacion",
    "correoElectronico",
    "correoBeneficiario",
    "token"
})
public class NuevoSpeiCeroRequest {

    protected String nombreOrdenante;
    protected String cuentaOrdenante;
    protected String rfcOrdenante;
    protected String tipoCuentaOrdenanteId;
    protected String nombreBeneficiario;
    protected String cuentaBeneficiario;
    protected String rfcBeneficiario;
    protected String tipoCuentaBeneficiarioId;
    protected String conceptoPago;
    protected String tipoPago;
    protected String monto;
    protected String iva;
    protected Integer institucionBenId;
    protected Integer operacionId;
    protected Long usuarioId;
    protected Integer aplicacion;
    protected String correoElectronico;
    protected String correoBeneficiario;
    protected String token;

    /**
     * Gets the value of the nombreOrdenante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreOrdenante() {
        return nombreOrdenante;
    }

    /**
     * Sets the value of the nombreOrdenante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreOrdenante(String value) {
        this.nombreOrdenante = value;
    }

    /**
     * Gets the value of the cuentaOrdenante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuentaOrdenante() {
        return cuentaOrdenante;
    }

    /**
     * Sets the value of the cuentaOrdenante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuentaOrdenante(String value) {
        this.cuentaOrdenante = value;
    }

    /**
     * Gets the value of the rfcOrdenante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcOrdenante() {
        return rfcOrdenante;
    }

    /**
     * Sets the value of the rfcOrdenante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcOrdenante(String value) {
        this.rfcOrdenante = value;
    }

    /**
     * Gets the value of the tipoCuentaOrdenanteId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCuentaOrdenanteId() {
        return tipoCuentaOrdenanteId;
    }

    /**
     * Sets the value of the tipoCuentaOrdenanteId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCuentaOrdenanteId(String value) {
        this.tipoCuentaOrdenanteId = value;
    }

    /**
     * Gets the value of the nombreBeneficiario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreBeneficiario() {
        return nombreBeneficiario;
    }

    /**
     * Sets the value of the nombreBeneficiario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreBeneficiario(String value) {
        this.nombreBeneficiario = value;
    }

    /**
     * Gets the value of the cuentaBeneficiario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuentaBeneficiario() {
        return cuentaBeneficiario;
    }

    /**
     * Sets the value of the cuentaBeneficiario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuentaBeneficiario(String value) {
        this.cuentaBeneficiario = value;
    }

    /**
     * Gets the value of the rfcBeneficiario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcBeneficiario() {
        return rfcBeneficiario;
    }

    /**
     * Sets the value of the rfcBeneficiario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcBeneficiario(String value) {
        this.rfcBeneficiario = value;
    }

    /**
     * Gets the value of the tipoCuentaBeneficiarioId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCuentaBeneficiarioId() {
        return tipoCuentaBeneficiarioId;
    }

    /**
     * Sets the value of the tipoCuentaBeneficiarioId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCuentaBeneficiarioId(String value) {
        this.tipoCuentaBeneficiarioId = value;
    }

    /**
     * Gets the value of the conceptoPago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConceptoPago() {
        return conceptoPago;
    }

    /**
     * Sets the value of the conceptoPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConceptoPago(String value) {
        this.conceptoPago = value;
    }

    /**
     * Gets the value of the tipoPago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoPago() {
        return tipoPago;
    }

    /**
     * Sets the value of the tipoPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoPago(String value) {
        this.tipoPago = value;
    }

    /**
     * Gets the value of the monto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonto() {
        return monto;
    }

    /**
     * Sets the value of the monto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonto(String value) {
        this.monto = value;
    }

    /**
     * Gets the value of the iva property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIva() {
        return iva;
    }

    /**
     * Sets the value of the iva property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIva(String value) {
        this.iva = value;
    }

    /**
     * Gets the value of the institucionBenId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInstitucionBenId() {
        return institucionBenId;
    }

    /**
     * Sets the value of the institucionBenId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInstitucionBenId(Integer value) {
        this.institucionBenId = value;
    }

    /**
     * Gets the value of the operacionId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOperacionId() {
        return operacionId;
    }

    /**
     * Sets the value of the operacionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOperacionId(Integer value) {
        this.operacionId = value;
    }

    /**
     * Gets the value of the usuarioId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getUsuarioId() {
        return usuarioId;
    }

    /**
     * Sets the value of the usuarioId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setUsuarioId(Long value) {
        this.usuarioId = value;
    }

    /**
     * Gets the value of the aplicacion property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAplicacion() {
        return aplicacion;
    }

    /**
     * Sets the value of the aplicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAplicacion(Integer value) {
        this.aplicacion = value;
    }

    /**
     * Gets the value of the correoElectronico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * Sets the value of the correoElectronico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorreoElectronico(String value) {
        this.correoElectronico = value;
    }

    /**
     * Gets the value of the correoBeneficiario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorreoBeneficiario() {
        return correoBeneficiario;
    }

    /**
     * Sets the value of the correoBeneficiario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorreoBeneficiario(String value) {
        this.correoBeneficiario = value;
    }

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

}
