
package net.cero.ahorro.spei.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for respuestaErrorXML complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="respuestaErrorXML">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoError" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="descError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errores" type="{http://grupoGuardar.cat.ws.izel.net/}respuestaErrorXML" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaErrorXML", propOrder = {
    "codigoError",
    "descError",
    "errores"
})
public class RespuestaErrorXML {

    protected long codigoError;
    private String descError;
    @XmlElement(nillable = true)
    protected List<RespuestaErrorXML> errores;

    
    public RespuestaErrorXML(){
		codigoError = 0L;
		descError = "";
		errores = new ArrayList<RespuestaErrorXML>();
	}
	
	public RespuestaErrorXML(Long id){
		codigoError = id;
	}
    
    public RespuestaErrorXML(Long id, String descError){
		this.codigoError = id;
		this.descError = descError;
	}
    
    /**
     * Gets the value of the codigoError property.
     * 
     */
   
    public long getCodigoError() {
        return codigoError;
    }

    /**
     * Sets the value of the codigoError property.
     * 
     */
    public void setCodigoError(long value) {
        this.codigoError = value;
    }

    /**
     * Gets the value of the errores property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errores property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrores().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RespuestaErrorXML }
     * 
     * 
     */
    public List<RespuestaErrorXML> getErrores() {
        if (errores == null) {
            errores = new ArrayList<RespuestaErrorXML>();
        }
        return this.errores;
    }
    
	public void addCodigo( long id, String descripcion ) {
		if(errores == null){
			errores = new ArrayList<RespuestaErrorXML>();
		}
		codigoError = id;
		errores.add(new RespuestaErrorXML(id,descripcion));
	}
    
    public Long obtenerUltimoError() {
    	if(errores == null || errores.isEmpty())
    		return null;
    	
    	return errores.get(0).codigoError;
    }

    public String obtenerDescUltimoError(){
    	if(errores == null || errores.isEmpty())
    		return null;
    	
    	return errores.get(0).descError;
    }
    
	public String getDescError() {
		return descError;
	}

	public void setDescError(String descError) {
		this.descError = descError;
	}

}
