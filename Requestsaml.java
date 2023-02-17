 
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.zip.DeflaterOutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.codec.binary.Base64;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.w3c.dom.Element;
import io.jsonwebtoken.*;
import java.util.*;

public class Requestsaml {
	
	private String app_id = "SDHTQBJXSFMJJOWMYPGQ";
	private String app_sec = "OhyxY[QrdRVf@ejtBDEVXT^_PKSFA[DbCUIc]eSQkrsU^jAL]Y";
	private String domain = "http://localhost:8080";
	
	public String generateSAMLRequest(String s_no,String name,String prod_id) throws Exception{
		DefaultBootstrap.bootstrap();
		AuthnRequest request = (AuthnRequest)((SAMLObjectBuilder)Configuration.getBuilderFactory().getBuilder(AuthnRequest.DEFAULT_ELEMENT_NAME)).buildObject();
		request.setAssertionConsumerServiceURL("http://localhost:8080/HospitalManagement/SSOcallback");
		request.setID(generate(s_no,name,prod_id));
		request.setProviderName(app_id);
		request.setVersion(SAMLVersion.VERSION_20);
		request.setIssueInstant(new org.joda.time.DateTime());
		request.setProtocolBinding("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
		request.setIssuer(buildIssuer(app_id));
		String base64EncodedRequest = base64EncodeXMLObject(request);
		
		return base64EncodedRequest;
	}
		
    public String generateSAMLRequest(String username,String password) throws Exception{
		DefaultBootstrap.bootstrap();
		AuthnRequest request = (AuthnRequest)((SAMLObjectBuilder)Configuration.getBuilderFactory().getBuilder(AuthnRequest.DEFAULT_ELEMENT_NAME)).buildObject();
		request.setAssertionConsumerServiceURL("http://localhost:8080/HospitalManagement/SSOcallback");
		request.setID(generate(username,password));
		request.setProviderName(app_id);
		request.setVersion(SAMLVersion.VERSION_20);
		request.setIssueInstant(new org.joda.time.DateTime());
		request.setProtocolBinding("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
		request.setIssuer(buildIssuer(app_id));
		String base64EncodedRequest = base64EncodeXMLObject(request);
		
		return base64EncodedRequest;
    }

    private Issuer buildIssuer(String issuerValue)throws Exception {
		Issuer issuer = (Issuer)((SAMLObjectBuilder) Configuration.getBuilderFactory().getBuilder(Issuer.DEFAULT_ELEMENT_NAME)).buildObject();
		issuer.setValue(issuerValue);
		return issuer;
    }

    private String base64EncodeXMLObject(XMLObject xmlObject)throws Exception {
		MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();
		Marshaller marshaller = marshallerFactory.getMarshaller(xmlObject);
		Element samlObjectElement = marshaller.marshall(xmlObject);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(samlObjectElement);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		String urlEncodedMessage = URLEncoder.encode(xmlString, "UTF-8");
		return urlEncodedMessage;
	}
	
	private String generate(String username,String password){
		long millisec = System.currentTimeMillis();
		long expsec = millisec+(60*1000);
		Date dateIss = new Date(millisec);
		Date dateExp = new Date(expsec);
		Claims claims = Jwts.claims().setExpiration(dateExp);		
		claims.put("Domain",domain);
		claims.put("Username",username);
		claims.put("password",password);
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,app_sec).compact();
	}
	
	private String generate(String s_no,String name,String prod_id){
		long millisec = System.currentTimeMillis();
		long expsec = millisec+(60*1000);
		Date dateIss = new Date(millisec);
		Date dateExp = new Date(expsec);
		Claims claims = Jwts.claims().setExpiration(dateExp);		
		claims.put("Domain",domain);
		claims.put("s_no",s_no);
		claims.put("name",name);
		claims.put("prod_id",prod_id);
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,app_sec).compact();
	}
}