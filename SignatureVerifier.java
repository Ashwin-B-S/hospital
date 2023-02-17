import java.io.*;
import java.security.*;
import java.security.spec.*;
import org.apache.commons.codec.binary.Base64;
public class SignatureVerifier {
	
	public static boolean verify(String sign,String data)throws Exception{
		byte[] bytes = data.getBytes("UTF8");
		byte[] decoded = Base64.decodeBase64(sign.getBytes());

        FileInputStream keyfis = new FileInputStream("C:/Program Files/apache-tomcat/apache-tomcat-9.0.68/webapps/hospital/SSO/publicKey");
        byte[] encKey = new byte[keyfis.available()];
        keyfis.read(encKey);
        keyfis.close();
		
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
        sig.initVerify(pubKey);
        sig.update(bytes);

        return sig.verify(decoded);
	}

}