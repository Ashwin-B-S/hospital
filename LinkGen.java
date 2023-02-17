
import java.io.*;
import java.util.*;
import java.net.*;
import io.jsonwebtoken.*;

public class LinkGen {
	
	private String App_ID="SDHTQBJXSFMJJOWMYPGQ";
	private String App_Secret="OhyxY[QrdRVf@ejtBDEVXT^_PKSFA[DbCUIc]eSQkrsU^jAL]Y";
	private long expsec = System.currentTimeMillis()+(1000*60*60);
	private Date dateExp = new Date(expsec);
	
	public static void main(String []args)throws Exception{
		LinkGen obj = new LinkGen();
		System.out.println(obj.generateInsertUrl("Sanjay","sanjay@abs.com","patient",String.valueOf("sanjay".hashCode())));
		System.out.println("\n"+obj.generateDeleteUrl("sanjay@abs.com"));
		System.out.println("\n"+obj.generateAlterPassUrl("nandhini@abs.com",String.valueOf("nandhini".hashCode())));
	}
	public String generateAuthUrl(String username,String password)throws Exception{
		String link = "http://192.168.10.236:8080/IdentityProvider/authorize?App_ID="+App_ID+"&SAMLRequest=";
		Requestsaml obj = new Requestsaml();
		link += obj.generateSAMLRequest(username,password);
		return link;
	}
	public String generateAuthUrl(String s_no,String name, String prod_id)throws Exception{
		String link = "http://192.168.10.236:8080/IdentityProvider/authorize?App_ID="+App_ID+"&SAMLRequest=";
		Requestsaml obj = new Requestsaml();
		link += obj.generateSAMLRequest(s_no,name,prod_id);
		return link;
	}
	
	public String generateInsertUrl(String name,String email,String role,String password){
		String link = "http://192.168.10.236:8080/IdentityProvider/adduser?App_ID="+App_ID+"&token=";
		Claims claims = Jwts.claims().setExpiration(dateExp);		
		claims.put("App_ID",App_ID);
		claims.put("name",name);
		claims.put("email",email);
		claims.put("role",role);
		claims.put("password",password);
		claims.put("Request_type","Insert");
		claims.put("Response_type","JWT");
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,App_Secret).compact();
		return link+token;
	}
	
	public String generateInsertDeviceUrl(String s_no,String email,String name, String prod_id){
		String link = "http://192.168.10.236:8080/IdentityProvider/adddevice?App_ID="+App_ID+"&token=";
		Claims claims = Jwts.claims().setExpiration(dateExp);		
		claims.put("App_ID",App_ID);
		claims.put("s_no",s_no);
		claims.put("email",email);
		claims.put("name",name);
		claims.put("prod_id",prod_id);
		claims.put("Request_type","Insert");
		claims.put("Response_type","JWT");
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,App_Secret).compact();
		return link+token;
	}
	
	public String generateRemoveDeviceUrl(String s_no,String email){
		String link = "http://192.168.10.236:8080/IdentityProvider/removedevice?App_ID="+App_ID+"&token=";
		Claims claims = Jwts.claims().setExpiration(dateExp);		
		claims.put("App_ID",App_ID);
		claims.put("s_no",s_no);
		claims.put("email",email);
		claims.put("Request_type","Delete");
		claims.put("Response_type","JWT");
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,App_Secret).compact();
		return link+token;
	}
	
	public String generateDeleteUrl(String email){		
		String link = "http://192.168.10.236:8080/IdentityProvider/remove?App_ID="+App_ID+"&token=";
		Claims claims = Jwts.claims().setExpiration(dateExp);		
		claims.put("App_ID",App_ID);
		claims.put("Request_type","Delete");
		claims.put("email",email);
		claims.put("Response_type","JWT");
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,App_Secret).compact();
		return link+token;
	}
	
	public String generateAlterRoleUrl(String email,String role){
		String link = "http://192.168.10.236:8080/IdentityProvider/edit?App_ID="+App_ID+"&token=";
		Claims claims = Jwts.claims().setExpiration(dateExp);		
		claims.put("App_ID",App_ID);
		claims.put("email",email);
		claims.put("role",role);
		claims.put("Request_type","change role");
		claims.put("Response_type","JWT");
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,App_Secret).compact();
		return link+token;
	}
	
	public String generateAlterPassUrl(String email,String password){
		String link = "http://192.168.10.236:8080/IdentityProvider/editpass?App_ID="+App_ID+"&token=";
		Claims claims = Jwts.claims().setExpiration(dateExp);		
		claims.put("App_ID",App_ID);
		claims.put("email",email);
		claims.put("password",password);
		claims.put("Request_type","change password");
		claims.put("Response_type","JWT");
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,App_Secret).compact();
		return link+token;
	}
	
	public String sendPost(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
	
}