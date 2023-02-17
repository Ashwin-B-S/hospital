import java.io.*;
import java.util.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import javax.crypto.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class ParseClaimsFromSAMLResponse {
    public static void main(String[] args) throws Exception {
        String samlResponse = "<samlp:Response xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\"><samlp:Status><samlp:StatusCode Value=\"urn:oasis:names:tc:SAML:2.0:status:Success\"/></samlp:Status><saml:Assertion xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\" Version=\"2.0\"><saml:AttributeStatement><saml:Attribute Name=\"email\"><saml:AttributeValue xsi:type=\"xs:string\">ashwin@abs.com</saml:AttributeValue></saml:Attribute><saml:Attribute Name=\"hashvalue\"><saml:AttributeValue xsi:type=\"xs:string\">-1695509922</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion></samlp:Response>";
        System.out.println(readSAMLResponse(samlResponse));
    }
    public static String readSAMLResponse(String samlResponse) throws Exception {
        String token="",digest="",time="",sign="",decoded="";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(samlResponse));
        Document doc = db.parse(is);
        NodeList attributeNodes = doc.getElementsByTagName("saml:Attribute");
        Map<String, String> claims = new HashMap<>();
        for (int i = 0; i < attributeNodes.getLength(); i++) {
            Element attributeElement = (Element) attributeNodes.item(i);
            String name = attributeElement.getAttribute("Name");
            String value = attributeElement.getTextContent();
            claims.put(name, value);
        }
        for (Map.Entry<String, String> entry : claims.entrySet()) {
            if(entry.getKey().equals("JWT"))token = entry.getValue();
            if(entry.getKey().equals("digest"))digest = entry.getValue();
            if(entry.getKey().equals("Signature"))sign = entry.getValue();
            if(entry.getKey().equals("notOnorAfter"))time = entry.getValue();
        }
		long millisec = Long.parseLong(time);
		System.out.println("\nJWT obtained from SAML response : "+token);
		System.out.println("\nencrypted hashvalue : "+digest);
		boolean verified = false;
		try{
			sign = sign.replace(" ","+");
			PublicKey publicKey = loadPublicKey();
			digest = digest.replace(" ","+");
			decoded = decrypt(publicKey,digest);
			System.out.println("\ndecrypted hashvalue : "+decoded);
			verified = SignatureVerifier.verify(sign,(token+time));
		}catch(Exception e){
			System.out.println(e);
		}
		System.out.println(millisec>System.currentTimeMillis());
		if(verified && decoded.equals(String.valueOf((token+time).hashCode())) && (millisec>System.currentTimeMillis())){
			System.out.println("True Sign verified!!!");
			return token;
		}
		return "";
    }
	public static PublicKey loadPublicKey() {
		String algorithm = "RSA";
        FileInputStream fis = null;
        PublicKey publicKey = null;
        try {
            File file = new File("C:/Program Files/apache-tomcat/apache-tomcat-9.0.68/webapps/HospitalManagement/public.key");
            fis = new FileInputStream(file);
            byte[] encodedPublicKey = new byte[(int) file.length()];
            fis.read(encodedPublicKey);

            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return publicKey;
    }
	
	
    public static String decrypt(PublicKey publicKey, String cipherText) {
        if (cipherText == null || cipherText.isEmpty()) {
            System.out.println("No data to decrypt!");
            return cipherText;
        }
        String decryptedString = "";
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");            
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] encryptedText = Base64.getDecoder().decode(cipherText.getBytes());           
            decryptedString = new String(cipher.doFinal(encryptedText));
        } catch (Exception ex) {
            System.out.println("Exception caught while decrypting : " + ex);
        }
        return decryptedString;
    }
}