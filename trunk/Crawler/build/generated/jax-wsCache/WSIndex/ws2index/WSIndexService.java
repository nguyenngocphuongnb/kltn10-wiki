
package ws2index;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2-hudson-752-
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "WSIndexService", targetNamespace = "http://Index/", wsdlLocation = "http://localhost:8084/WSIndex/WSIndex?wsdl")
public class WSIndexService
    extends Service
{

    private final static URL WSINDEXSERVICE_WSDL_LOCATION;
    private final static WebServiceException WSINDEXSERVICE_EXCEPTION;
    private final static QName WSINDEXSERVICE_QNAME = new QName("http://Index/", "WSIndexService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8084/WSIndex/WSIndex?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        WSINDEXSERVICE_WSDL_LOCATION = url;
        WSINDEXSERVICE_EXCEPTION = e;
    }

    public WSIndexService() {
        super(__getWsdlLocation(), WSINDEXSERVICE_QNAME);
    }

    public WSIndexService(WebServiceFeature... features) {
        super(__getWsdlLocation(), WSINDEXSERVICE_QNAME, features);
    }

    public WSIndexService(URL wsdlLocation) {
        super(wsdlLocation, WSINDEXSERVICE_QNAME);
    }

    public WSIndexService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, WSINDEXSERVICE_QNAME, features);
    }

    public WSIndexService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public WSIndexService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns WSIndex
     */
    @WebEndpoint(name = "WSIndexPort")
    public WSIndex getWSIndexPort() {
        return super.getPort(new QName("http://Index/", "WSIndexPort"), WSIndex.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns WSIndex
     */
    @WebEndpoint(name = "WSIndexPort")
    public WSIndex getWSIndexPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://Index/", "WSIndexPort"), WSIndex.class, features);
    }

    private static URL __getWsdlLocation() {
        if (WSINDEXSERVICE_EXCEPTION!= null) {
            throw WSINDEXSERVICE_EXCEPTION;
        }
        return WSINDEXSERVICE_WSDL_LOCATION;
    }

}
