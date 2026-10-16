
package net.cero.ahorro.spei.enviospei.nuevospeicero;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.spei.nuevospeicero package. 
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

    private final static QName _NuevoSpeiCeroResponse_QNAME = new QName("http://nuevoSpeiCero.spei.ws.izel.net/", "NuevoSpeiCeroResponse");
    private final static QName _NuevoSpeiCeroRequest_QNAME = new QName("http://nuevoSpeiCero.spei.ws.izel.net/", "NuevoSpeiCeroRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.spei.nuevospeicero
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NuevoSpeiCeroRequest }
     * 
     */
    public NuevoSpeiCeroRequest createNuevoSpeiCeroRequest() {
        return new NuevoSpeiCeroRequest();
    }

    /**
     * Create an instance of {@link NuevoSpeiCeroResponse }
     * 
     */
    public NuevoSpeiCeroResponse createNuevoSpeiCeroResponse() {
        return new NuevoSpeiCeroResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NuevoSpeiCeroResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nuevoSpeiCero.spei.ws.izel.net/", name = "NuevoSpeiCeroResponse")
    public JAXBElement<NuevoSpeiCeroResponse> createNuevoSpeiCeroResponse(NuevoSpeiCeroResponse value) {
        return new JAXBElement<NuevoSpeiCeroResponse>(_NuevoSpeiCeroResponse_QNAME, NuevoSpeiCeroResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NuevoSpeiCeroRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nuevoSpeiCero.spei.ws.izel.net/", name = "NuevoSpeiCeroRequest")
    public JAXBElement<NuevoSpeiCeroRequest> createNuevoSpeiCeroRequest(NuevoSpeiCeroRequest value) {
        return new JAXBElement<NuevoSpeiCeroRequest>(_NuevoSpeiCeroRequest_QNAME, NuevoSpeiCeroRequest.class, null, value);
    }

}
