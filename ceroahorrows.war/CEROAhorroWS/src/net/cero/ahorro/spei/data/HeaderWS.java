
package net.cero.ahorro.spei.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for headerWS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="headerWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idEmpresa" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="idResponsabilidad" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="idSesion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUsuario" type="{http://www.w3.org/2001/XMLSchema}long"/>        
 *         &lt;element name="ipHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
  *        &lt;element name="nameHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuarioClave" type="{http://www.w3.org/2001/XMLSchema}long"/>        
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "headerWS", propOrder = {
    "idEmpresa",
    "idResponsabilidad",
    "idSesion",
    "idUsuario",
    "idClaseCanalAtencion",
    "idCanalAtencion",
    "idPuntoAtencion",
    "idUbicacion",
    "idSucursal",
    "ipHost",
    "nameHost",
    "usuarioClave"
})
public class HeaderWS {

    protected long idEmpresa;
    protected long idResponsabilidad;
    protected String idSesion;
    protected String usuarioClave;
    protected long idUsuario;
	protected long idClaseCanalAtencion;
    protected long idCanalAtencion;
    protected long idPuntoAtencion;
    protected long idUbicacion;
    protected long idSucursal;
    protected String ipHost;
    protected String nameHost;
    
    /**
     * Gets the value of the idEmpresa property.
     * 
     */
    public long getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * Sets the value of the idEmpresa property.
     * 
     */
    public void setIdEmpresa(long value) {
        this.idEmpresa = value;
    }

    /**
     * Gets the value of the idResponsabilidad property.
     * 
     */
    public long getIdResponsabilidad() {
        return idResponsabilidad;
    }

    /**
     * Sets the value of the idResponsabilidad property.
     * 
     */
    public void setIdResponsabilidad(long value) {
        this.idResponsabilidad = value;
    }

    /**
     * Gets the value of the idSesion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdSesion() {
        return idSesion;
    }

    /**
     * Sets the value of the idSesion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdSesion(String value) {
        this.idSesion = value;
    }
    
    public String getUsuarioClave() {
		return usuarioClave;
	}

	public void setUsuarioClave(String usuarioClave) {
		this.usuarioClave = usuarioClave;
	}

    /**
     * Gets the value of the idUsuario property.
     * 
     */
    public long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Sets the value of the idUsuario property.
     * 
     */
    public void setIdUsuario(long value) {
        this.idUsuario = value;
    }

	public long getIdClaseCanalAtencion() {
		return idClaseCanalAtencion;
	}

	public void setIdClaseCanalAtencion(long idClaseCanalAtencion) {
		this.idClaseCanalAtencion = idClaseCanalAtencion;
	}

	public long getIdCanalAtencion() {
		return idCanalAtencion;
	}

	public void setIdCanalAtencion(long idCanalAtencion) {
		this.idCanalAtencion = idCanalAtencion;
	}

	public long getIdPuntoAtencion() {
		return idPuntoAtencion;
	}

	public void setIdPuntoAtencion(long idPuntoAtencion) {
		this.idPuntoAtencion = idPuntoAtencion;
	}

	public long getIdUbicacion() {
		return idUbicacion;
	}

	public void setIdUbicacion(long idUbicacion) {
		this.idUbicacion = idUbicacion;
	}

	public long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getIpHost() {
		return ipHost;
	}

	public void setIpHost(String ipHost) {
		this.ipHost = ipHost;
	}

	public String getNameHost() {
		return nameHost;
	}

	public void setNameHost(String nameHost) {
		this.nameHost = nameHost;
	}

}

/**  !HeaderWS.java */



