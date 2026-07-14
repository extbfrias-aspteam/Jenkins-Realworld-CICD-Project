
package net.cero.ahorro.spei.generaclabe;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.cero.ahorro.spei.data.HeaderWS;
import net.cero.ahorro.spei.data.ParametroBody;
import net.cero.ahorro.spei.data.RespuestaBodyXML;
import net.cero.ahorro.spei.data.RespuestaErrorXML;
import net.cero.ahorro.spei.data.RespuestaXML;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.spei.generaclabe package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GeneraClabeResponse_QNAME = new QName("http://generaClabe.spei.ws.izel.net/", "GeneraClabeResponse");
    private final static QName _GeneraClabeRequest_QNAME = new QName("http://generaClabe.spei.ws.izel.net/", "GeneraClabeRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.spei.generaclabe
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GeneraClabeResponse }
     * 
     */
    public GeneraClabeResponse createGeneraClabeResponse() {
        return new GeneraClabeResponse();
    }

    /**
     * Create an instance of {@link GeneraClabeRequest }
     * 
     */
    public GeneraClabeRequest createGeneraClabeRequest() {
        return new GeneraClabeRequest();
    }

    /**
     * Create an instance of {@link ParametroBody }
     * 
     */
    public ParametroBody createParametroBody() {
        return new ParametroBody();
    }

    /**
     * Create an instance of {@link HeaderWS }
     * 
     */
    public HeaderWS createHeaderWS() {
        return new HeaderWS();
    }

    /**
     * Create an instance of {@link RespuestaErrorXML }
     * 
     */
    public RespuestaErrorXML createRespuestaErrorXML() {
        return new RespuestaErrorXML();
    }

    /**
     * Create an instance of {@link RespuestaBodyXML }
     * 
     */
    public RespuestaBodyXML createRespuestaBodyXML() {
        return new RespuestaBodyXML();
    }

    /**
     * Create an instance of {@link RespuestaXML }
     * 
     */
    public RespuestaXML createRespuestaXML() {
        return new RespuestaXML();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GeneraClabeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://generaClabe.spei.ws.izel.net/", name = "GeneraClabeResponse")
    public JAXBElement<GeneraClabeResponse> createGeneraClabeResponse(GeneraClabeResponse value) {
        return new JAXBElement<GeneraClabeResponse>(_GeneraClabeResponse_QNAME, GeneraClabeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GeneraClabeRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://generaClabe.spei.ws.izel.net/", name = "GeneraClabeRequest")
    public JAXBElement<GeneraClabeRequest> createGeneraClabeRequest(GeneraClabeRequest value) {
        return new JAXBElement<GeneraClabeRequest>(_GeneraClabeRequest_QNAME, GeneraClabeRequest.class, null, value);
    }

}
